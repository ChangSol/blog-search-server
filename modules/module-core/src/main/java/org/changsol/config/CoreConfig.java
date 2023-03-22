package org.changsol.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;

/**
 * CoreConfig Class
 */
@Configuration
@EnableJpaAuditing // Audit 사용
@EnableRetry // Retry 사용
public class CoreConfig {

}