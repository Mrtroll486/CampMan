package org.guan.campman.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "org.guan.campman.repository")
@EntityScan(basePackages = "org.guan.campman.model")
public class JpaConfig {
}
