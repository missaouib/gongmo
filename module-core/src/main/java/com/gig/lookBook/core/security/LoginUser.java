package com.gig.lookBook.core.security;

import com.gig.lookBook.core.model.Account;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 로그인 유저 정보
 *
 * @author prographer
 * @date 2019-04-09
 */
public class LoginUser extends org.springframework.security.core.userdetails.User {
    private Account loginAccount;

    public LoginUser(String username, String password, Collection<? extends GrantedAuthority> authorities, Account loginAccount) {
        super(username, password, authorities);
        this.loginAccount = loginAccount;
    }

    public LoginUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
                     boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, Account loginAccount) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.loginAccount = loginAccount;
    }

    public Account getLoginAccount() {
        return loginAccount;
    }
}
