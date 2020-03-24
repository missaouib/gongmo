package com.gig.lookBook.core.service;

import com.gig.lookBook.core.model.Account;
import com.gig.lookBook.core.model.types.YNType;
import com.gig.lookBook.core.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityService {

    @Autowired
    AccountRepository accountRepository;

    /**
     * 로그인 사용자 갖고오기
     *
     * @return
     * @throws
     */
    public Account getLoginUser() {
        if(SecurityContextHolder.getContext().getAuthentication() == null) return null;

        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        Optional<Account> findUser = accountRepository.findByUsernameIgnoreCaseAndActiveYn(username, YNType.Y);
        if (findUser.isEmpty()) {
            //throw new UserNotFoundException(username, UserNotFoundException.USER_NOT_FOUND);
            return null;
        }

        return findUser.get();
    }


}
