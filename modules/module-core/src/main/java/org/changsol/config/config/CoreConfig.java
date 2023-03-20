package org.changsol.config.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * CoreConfig Class
 */
@Configuration
@EnableJpaAuditing // Audit 사용
@SuppressWarnings("unused")
public class CoreConfig {
}