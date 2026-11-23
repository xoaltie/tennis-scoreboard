# Tennis Scoreboard

REST API для ведения счёта теннисного матча в реальном времени и хранения результатов завершённых матчей. Проект
реализован на Java 25 и Spring Web MVC 7 без Spring Boot и упаковывается в WAR-архив для развёртывания в
сервлет-контейнере (Apache Tomcat 11).

## Содержание

- [Технологический стек](#технологический-стек)
- [Архитектура и правила подсчёта счёта](#архитектура-и-правила-подсчёта-счёта)
- [Схема базы данных](#схема-базы-данных)
- [REST API](#rest-api)
- [Настройка PostgreSQL и локальный запуск](#настройка-postgresql-и-локальный-запуск)
- [Тестирование](#тестирование)

---

## Технологический стек

- **Язык и сборка:** Java 25 (Gradle Toolchain), Gradle Wrapper (`./gradlew`), упаковка `war`
- **Web и контейнер:** Spring Web MVC 7.0.10 (Java Config), Gretty 5.1.0 (Tomcat 11)
- **База данных и ORM:** PostgreSQL, Hibernate ORM 7.4.15.Final, Spring ORM 7.0.10, HikariCP 7.1.0
- **Миграции:** Flyway 13.12.1
- **Валидация и маппинг:** Hibernate Validator 9.1.6.Final (`jakarta.validation`), MapStruct 1.6.3, Lombok 1.18.48,
  Jackson Databind 3.3.0
- **Логирование и тесты:** SLF4J + Logback Classic 1.6.6, JUnit 6 (BOM 6.1.3), AssertJ 3.27.7

---

## Архитектура и правила подсчёта счёта

### Слои приложения

- `controller/` — REST-контроллеры (`MatchController`), обрезка пробелов строковых параметров (`GlobalBinderAdvice`) и
  централизованная обработка ошибок (`GlobalExceptionHandler`).
- `service/` — сервисы приложения в стиле CQRS-lite (`command/`, `result/`). Активные матчи хранятся в памяти в
  `ConcurrentHashMap<UUID, OngoingMatch>` с синхронизацией по объекту конкретного матча (`synchronized (match)`),
  завершённые сохраняются в PostgreSQL.
- `domain/` — доменная модель с правилами теннисного счёта, не зависящая от Spring и БД.
- `mapper/` — MapStruct-мапперы для изоляции HTTP DTO, сервисных контрактов и JPA-сущностей.
- `repository/` и `entity/` — слой доступа к данным на базе `SessionFactory` Hibernate.

### Правила теннисного счёта

- **Матч (`OngoingMatch`):** играется до двух выигранных сетов.
- **Сет (`Set`):** победа присуждается игроку, набравшему 6 геймов с отрывом минимум в 2 гейма (`6:0`–`6:4`) либо при
  счёте `7:5`. При счёте `6:6` текущий гейм автоматически переключается на тайбрейк (`7:6`).
- **Обычный гейм (`RegularGame`):** очки `0` → `15` → `30` → `40`. При счёте `40:40` (deuce) очко даёт преимущество
  (`AD`), следующее очко того же игрока приносит победу в гейме; при счёте против `AD` счёт возвращается к `40:40`.
- **Тайбрейк (`Tiebreak`):** очки считаются целыми числами (`0, 1, 2, ...`). Для победы нужно набрать минимум 7 очков с
  отрывом от соперника минимум в 2 очка (`7:5`, `8:6` и т.д.).

---

## Схема базы данных

Схема управляется миграциями Flyway (`src/main/resources/db/migration/`):

1. `V202611121958__create_players_table.sql`:
    - подключает расширение PostgreSQL `citext` (`CREATE EXTENSION IF NOT EXISTS citext;`);
    - создаёт таблицу `players` (`id` identity, `name citext UNIQUE NOT NULL`) — регистронезависимая уникальность и
      поиск по имени.
2. `V202611122009__create_matches_table.sql`:
    - создаёт таблицу `matches` (`id` identity, `player1_id`, `player2_id`, `winner_id` с внешними ключами на
      `players(id)`);
    - добавляет индексы `idx_match_player1_id` и `idx_match_player2_id`.

---

## REST API

По умолчанию при локальном запуске через Gretty context path равен `/api` (`http://localhost:8080/api`).

### 1. Создать новый матч

`POST /api/matches`

Тело запроса:

```json
{
  "firstPlayerName": "Roger Federer",
  "secondPlayerName": "Rafael Nadal"
}
```

Ответ (`201 Created`):

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000"
}
```

Правила валидации: имена обязательны, не должны совпадать (регистронезависимо), длина от 1 до 50 символов; допустимы
только буквы, пробелы, дефисы и апострофы, должна присутствовать минимум одна буква (`@PlayerName`). Лидирующие и
завершающие пробелы обрезаются автоматически.

### 2. Начислить очко игроку

`POST /api/matches/{uuid}/point`

Тело запроса:

```json
{
  "name": "Roger Federer"
}
```

Ответ (`200 OK`) во время обычного гейма:

```json
{
  "firstPlayer": {
    "name": "Roger Federer",
    "points": "15",
    "games": 0,
    "sets": 0,
    "tieBreakPoints": null
  },
  "secondPlayer": {
    "name": "Rafael Nadal",
    "points": "0",
    "games": 0,
    "sets": 0,
    "tieBreakPoints": null
  },
  "winnerName": null
}
```

Во время тайбрейка поле `points` принимает значение `null`, а `tieBreakPoints` — текущее число очков тайбрейка (`0`,
`1`, `2`, ...). Когда один из игроков выигрывает 2 сета, `winnerName` заполняется его именем, матч сохраняется в
PostgreSQL и удаляется из списка активных матчей в памяти.

### 3. Получить список завершённых матчей

`GET /api/matches`

Параметры запроса (опциональные):

- `player_name` — фильтр по имени игрока (подстрока, без учёта регистра за счёт `citext`);
- `page` — номер страницы (начиная с `1`; при отсутствии или значении `< 1` используется `1`). Размер страницы
  фиксирован — 10 записей.

Ответ (`200 OK`):

```json
{
  "matches": [
    {
      "firstPlayerName": "Magnus",
      "secondPlayerName": "Hikaru",
      "winnerName": "Magnus"
    }
  ],
  "currentPage": 1,
  "totalPages": 2
}
```

### Формат ошибок

Все ошибки возвращаются в формате `ErrorResponseDto`:

```json
{
  "message": "match with uuid '...' not found"
}
```

Поддерживаются статусы `400 Bad Request` (валидация полей, совпадающие имена игроков, игрок не участвует в матче,
невалидный JSON или параметр), `404 Not Found` (матч не найден или несуществующий маршрут), `405 Method Not Allowed` и
`500 Internal Server Error`.

---

## Настройка PostgreSQL и локальный запуск

### 1. Требования

- JDK 25
- PostgreSQL (с доступным расширением `citext` — входит в стандартную поставку `postgresql-contrib` / основной пакет в
  большинстве дистрибутивов)

### 2. Подготовка базы данных PostgreSQL

Подключитесь к PostgreSQL под суперпользователем (например, `postgres`) и создайте базу данных (при желании — отдельного
пользователя):

```sql
CREATE DATABASE tennis_scoreboard;
```

Если приложение и Flyway будут подключаться не от имени суперпользователя, заранее создайте расширение `citext` в новой
базе от имени администратора:

```sql
\c tennis_scoreboard
CREATE EXTENSION IF NOT EXISTS citext;
```

*(Если подключение идёт под суперпользователем, миграция `V202611121958__create_players_table.sql` создаст расширение
самостоятельно.)*

### 3. Переменные окружения и конфигурация Flyway

Настройки подключения к БД описаны в `src/main/resources/application.properties` с поддержкой переменных окружения:

| Переменная    | Значение по умолчанию                                | Описание                |
|:--------------|:-----------------------------------------------------|:------------------------|
| `DB_URL`      | `jdbc:postgresql://localhost:5432/tennis_scoreboard` | JDBC-строка подключения |
| `DB_USER`     | `postgres`                                           | Пользователь БД         |
| `DB_PASSWORD` | `1`                                                  | Пароль пользователя БД  |

Для локальной работы:

1. Скопируйте шаблоны конфигурации (рабочие файлы уже добавлены в `.gitignore`):

```bash
cp .env.example .env.local
cp flyway.conf.example flyway.conf
```

2. Укажите актуальные реквизиты PostgreSQL в `.env.local` и `flyway.conf` (`flyway.url`, `flyway.user`,
   `flyway.password`).

### 4. Миграции и тестовые данные

Примените миграции Flyway:

```bash
./gradlew flywayMigrate
./gradlew flywayInfo
```

### 5. Локальный запуск через Gretty (Tomcat 11)

Экспортируйте переменные окружения (например, из `.env.local`) и запустите встроенный Tomcat 11:

```bash
set -a && source .env.local && set +a
./gradlew appRun
```

Приложение будет доступно по адресу `http://localhost:8080/api`.

---

## Тестирование

Запуск модульных тестов (JUnit 6 + AssertJ, без необходимости поднимать PostgreSQL или Tomcat):

```bash
./gradlew test
```
