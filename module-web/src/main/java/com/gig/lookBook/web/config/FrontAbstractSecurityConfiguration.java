package com.gig.lookBook.web.config;

import com.gig.lookBook.core.security.UserDetailsAuthenticationProvider;
import com.gig.lookBook.core.security.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Spring Security BaseConfiguration
 *
 * @author Jake
 * @date 20/04/04
 */
@Slf4j
public abstract class FrontAbstractSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public UserDetailsAuthenticationProvider userDetailsAuthenticationProvider() {
        return new UserDetailsAuthenticationProvider(userDetailsService(), passwordEncoder());
    }

    @Bean
    public FrontAuthenticationSuccessHandlerImpl successHandler() {
        return new FrontAuthenticationSuccessHandlerImpl();
    }

    @Bean
    public FrontAuthenticationFailureHandlerImpl failureHandler() {
        return new FrontAuthenticationFailureHandlerImpl();
    }

    /**
     * 비밀번호 암호화
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        String idForEncode = "SHA-512";
        encoders.put(idForEncode, new MessageDigestPasswordEncoder(idForEncode));
        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }


    /**
     * URL 대상정보
     *
     * @return
     */
    @Bean
    public abstract FilterInvocationSecurityMetadataSource gfFilterInvocationSecurityMetadataSource();

    /**
     * 로그인 의사결정 방식
     * 하나만 통과해도 OK
     *
     * @return
     */
    @Bean
    public AffirmativeBased affirmativeBased() {
        List<AccessDecisionVoter<? extends Object>> accessDecisionVoters = new ArrayList<>();
        accessDecisionVoters.add(new RoleVoter());
        AffirmativeBased affirmativeBased = new AffirmativeBased(accessDecisionVoters);
        affirmativeBased.setAllowIfAllAbstainDecisions(false);
        return affirmativeBased;

    }


    /**
     * dynamic url설정을 위한 Interceptor
     *
     * @return
     * @throws Exception
     */
    @Bean
    public FilterSecurityInterceptor filterSecurityInterceptor() throws Exception {
        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
        filterSecurityInterceptor.setSecurityMetadataSource(gfFilterInvocationSecurityMetadataSource());
        filterSecurityInterceptor.setAuthenticationManager(authenticationManager());
        filterSecurityInterceptor.setAccessDecisionManager(affirmativeBased());
        try {
            filterSecurityInterceptor.afterPropertiesSet();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return filterSecurityInterceptor;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/js/**", "/css/**", "/fonts/**", "/images/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
        auth.authenticationProvider(userDetailsAuthenticationProvider());
    }

}
