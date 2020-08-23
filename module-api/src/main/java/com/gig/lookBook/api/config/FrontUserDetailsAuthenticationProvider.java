package com.gig.lookBook.api.config;

import com.gig.lookBook.core.security.UserDetailsServiceImpl;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class FrontUserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private UserDetailsServiceImpl userDetailsService;

    private PasswordEncoder passwordEncoder;

    public FrontUserDetailsAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = (UserDetailsServiceImpl)userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if(!"".equals(authentication.getCredentials())) {
            if (!userDetails.getPassword().equals(authentication.getCredentials())) {
                throw new BadCredentialsException("Invalid credentials.");
            }
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        UserDetails userDetails = null;
        userDetails = userDetailsService.loadUserByUsername(username);
        return userDetails;
    }
}
