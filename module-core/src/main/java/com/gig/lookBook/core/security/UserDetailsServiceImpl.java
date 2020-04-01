package com.gig.lookBook.core.security;

import com.gig.lookBook.core.model.Account;
import com.gig.lookBook.core.model.Role;
import com.gig.lookBook.core.model.types.YNType;
import com.gig.lookBook.core.repository.RoleRepository;
import com.gig.lookBook.core.service.AccountService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Jake
 * @date: 20/04/01
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    AccountService accountService;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account;
        try {
            account = accountService.findByUsername(username);
        } catch (NotFoundException e) {
            String msg = "User Not found: " + username;
            log.error(msg);
            throw new UsernameNotFoundException(msg);
        }

        if (account.getPasswordFailCnt() >= 5 && account.getPasswordFailTime() != null && account.getPasswordFailTime().plusMinutes(30).isBefore(LocalDateTime.now())) {
            accountService.resetPasswordFailure(username);
            account.setPasswordFailCnt(0);
        }

        //user.setPassword("{SHA-512}" + user.getPassword());

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role r : account.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(r.getRoleName()));
        }

        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        // 접속가능 여부
        if (account.getActiveYn() == YNType.N) {
            log.error("account disabled");
            enabled = false;
        }

        // 패스워드 틀린 횟수
        if (account.getPasswordFailCnt() >= 5) {
            accountNonLocked = false;
        }

        Set<Role> setRole = account.getRoles();

        for(Role role : setRole) {
            if(("ROLE_USER".equals(role.getRoleName()))) {
                if("ROLE_USER".equals(role.getRoleName())) account.setPassword("");
                accountNonExpired = false;
            }
        }

        /*
        // 계정 만료 여부
        if (account.getDormancyYn() == YNType.Y) {
            accountNonExpired = false;
        }

        // 비밀번호 만료 여부
        if (account.getPasswordModifyAt() != null && account.getPasswordModifyAt().plusMonths(3).isBefore(LocalDateTime.now())) {
            credentialsNonExpired = false;
        }
        */

        return new LoginUser(account.getUsername(), account.getPassword(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities, account);
    }

}
