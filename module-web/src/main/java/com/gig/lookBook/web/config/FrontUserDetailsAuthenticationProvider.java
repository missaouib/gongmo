package com.gig.lookBook.web.config;

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

//        if("".equals(authentication.getCredentials())) {
//            // 일반 개인회원, 사업자 회원
//            if(optAccount.isEmpty()) {
//                Map<String, Object> params = new HashMap<>();
//                params.put("MEMBER_ID", username);
//
//                try {
//                    JSONObject jsonObject = restfulApiService.defaultApiRequestForm(RestfulApiService.USER_INFORMATION_LOAD, params);
//                    MemberDto dto = new MemberDto().objectToMemberDto(jsonObject);
//                    Account account = accountService.saveForFront(dto);
//                    userDetails = userDetailsService.loadUserByCustomerNo(account.getCustomerNo());
//                } catch (AlreadyEntity | NotFoundException | ParseException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                userDetails = userDetailsService.loadUserByCustomerNo(username);
//            }
//        } else {
//            // 파트너 회원
//            userDetails = userDetailsService.loadUserByUsername(username);
//        }

        return userDetails;
    }
}
