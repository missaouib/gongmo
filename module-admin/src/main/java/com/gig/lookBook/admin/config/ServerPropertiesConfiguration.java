package com.gig.lookBook.admin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.TomcatServletWebServerFactoryCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.http.HttpStatus;

/**
 * 서버 환경설정(web.xml)
 */
//@Configuration
public class ServerPropertiesConfiguration extends TomcatServletWebServerFactoryCustomizer {

    @Value("${spring.profiles.active}")
    String activeProfiles;

    public ServerPropertiesConfiguration(ServerProperties serverProperties) {
        super(serverProperties);
    }

    @Override
    public int getOrder() {
        return super.getOrder();
    }

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        super.customize(factory);
        factory.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/error/403.html"));
        factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error/404.html"));
        factory.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500.html"));
        factory.addErrorPages(new ErrorPage("/error/error.html"));
    }



}