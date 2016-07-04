package com.dima.converter.repository;

import com.dima.converter.model.jpa.History;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Context configuration for integration tests in the repository layer.
 *
 * @author Sam Brannen
 * @since 1.0
 */
@Configuration
@EnableJpaRepositories(basePackageClasses = HistoryRepository.class)
@EntityScan(basePackageClasses = History.class)
@Import({ DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class TestHistoryRepositoryConfig {
}