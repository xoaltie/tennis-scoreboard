package com.github.xoaltie.tennisscoreboard.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(PersistenceConfig.class)
@ComponentScan(basePackages = {"com.github.xoaltie.tennisscoreboard.service", "com.github.xoaltie.tennisscoreboard.repository"})
public class RootConfig {
}
