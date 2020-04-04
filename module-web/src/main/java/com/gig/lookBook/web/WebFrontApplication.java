package com.gig.lookBook.web;

import com.gig.lookBook.core.CoreConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(CoreConfiguration.class)
public class WebFrontApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebFrontApplication.class, args);
    }
}
