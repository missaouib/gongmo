package com.gig.lookBook.admin.config;

import com.gig.lookBook.core.model.types.MenuType;
import com.gig.lookBook.core.security.AbstractSecurityConfiguration;
import com.gig.lookBook.core.security.component.GFFilterInvocationSecurityMetadataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfiguration extends AbstractSecurityConfiguration {

    @Override
    public FilterInvocationSecurityMetadataSource gfFilterInvocationSecurityMetadataSource() {
        return new GFFilterInvocationSecurityMetadataSource(MenuType.AdminConsole);
    }

    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity.ignoring().antMatchers("/modules/**", "/js/**", "/css/**", "/img/**", "/fonts/**", "/healthcheck.html");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        log.info("SecurityConfiguration Admin HttpSecurity !");
        http
                .csrf().ignoringAntMatchers("/h2-console/**")
                .and()
                .headers().frameOptions().sameOrigin();

        http.authorizeRequests()
                .anyRequest().authenticated();


    }
}
