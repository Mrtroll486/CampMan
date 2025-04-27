package org.guan.campman.Config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "org.guan.campman.Repository")
@EntityScan(basePackages = "org.guan.campman.model")
public class JpaConfig {
}
