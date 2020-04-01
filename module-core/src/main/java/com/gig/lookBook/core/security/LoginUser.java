package com.gig.lookBook.core.security;

import com.gig.lookBook.core.model.Account;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author Jake
 * @date: 20/04/01
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
