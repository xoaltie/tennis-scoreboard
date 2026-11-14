CREATE TABLE IF NOT EXISTS matches
(
    id         bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    player1_id bigint NOT NULL REFERENCES players (id),
    player2_id bigint NOT NULL REFERENCES players (id),
    winner_id  bigint NOT NULL REFERENCES players (id)
);

CREATE INDEX idx_match_player1_id ON matches (player1_id);
CREATE INDEX idx_match_player2_id ON matches (player2_id);