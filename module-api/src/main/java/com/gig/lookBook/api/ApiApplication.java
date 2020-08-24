package com.gig.lookBook.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gig.lookBook.core.CoreConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

@SpringBootApplication
@Import(CoreConfiguration.class)
public class ApiApplication {

    @Autowired
    private ObjectMapper objectMapper;

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ApiApplication.class);
    }

    @PostConstruct
    public void setUp() { objectMapper.registerModule(new JavaTimeModule()); }
}
