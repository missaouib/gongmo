package com.gig.lookBook.api.config;

import com.gig.lookBook.core.model.types.MenuType;
import com.gig.lookBook.core.security.AbstractSecurityConfiguration;
import com.gig.lookBook.core.security.UserDetailsServiceImpl;
import com.gig.lookBook.core.security.component.GFFilterInvocationSecurityMetadataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class SecurityConfiguration extends AbstractSecurityConfiguration {

    private UserDetailsService userDetailsService;
    private final DataSource dataSource;

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }


    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity.ignoring().antMatchers("/modules/**", "/js/**", "/css/**", "/img/**", "/fonts/**", "/healthcheck.html");
    }

    @Override
    public FilterInvocationSecurityMetadataSource gfFilterInvocationSecurityMetadataSource() {
        return new GFFilterInvocationSecurityMetadataSource(MenuType.FrontEnd);
    }

    @Bean
    public FrontUserDetailsAuthenticationProvider frontUserDetailsAuthenticationProvider() {
        return new FrontUserDetailsAuthenticationProvider(userDetailsService(), passwordEncoder());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(frontUserDetailsAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        http.addFilterBefore(characterEncodingFilter, CsrfFilter.class);

        http
                .csrf().ignoringAntMatchers("/**")
                .and().cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());

        http.authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated();

        http.formLogin()
                .successHandler(successHandler())
                .failureHandler(failureHandler())
                .loginPage("/secure/login")
                .loginProcessingUrl("/secure/login-process")
                .usernameParameter("username").passwordParameter("password")
                .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutRequestMatcher(new AntPathRequestMatcher("/secure/logout"))
                .logoutSuccessUrl("/");
//                .and().addFilterBefore(filterSecurityInterceptor(), FilterSecurityInterceptor.class);

        http.logout()
                .logoutSuccessUrl("/");
    }
}
