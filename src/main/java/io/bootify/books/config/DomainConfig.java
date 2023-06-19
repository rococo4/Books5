package io.bootify.books.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("io.bootify.books.domain")
@EnableJpaRepositories("io.bootify.books.repos")
@EnableTransactionManagement
public class DomainConfig {
}
