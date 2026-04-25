package com.reger.infrastructure.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.reger.infrastructure.persistence"
)
@EntityScan(
        basePackages = "com.reger.domain.entity"
)
public class PersistenceConfig {


}