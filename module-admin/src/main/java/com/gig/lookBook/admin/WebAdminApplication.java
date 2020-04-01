package com.gig.lookBook.admin;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gig.lookBook.core.CoreConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

@SpringBootApplication
@Import(CoreConfiguration.class)
public class WebAdminApplication extends SpringBootServletInitializer {

    @Autowired
    private ObjectMapper objectMapper;

    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(WebAdminApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(WebAdminApplication.class, args);
    }

    @PostConstruct
    public void setUp() { objectMapper.registerModule(new JavaTimeModule()); }
}
