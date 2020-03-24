package com.gig.lookBook.core;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("com.gig.lookBook.core.model")
@ComponentScan({"com.gig.lookBook.core.service","com.gig.lookBook.core.components", "com.gig.lookBook.core.security"})
@EnableJpaRepositories("com.gig.lookBook.core.repository")
public class CoreConfiguration {
}
