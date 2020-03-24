package com.gig.lookBook.core.service;

import com.gig.lookBook.core.model.Account;
import com.gig.lookBook.core.repository.AccountRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account findByUsername(String username) throws NotFoundException {
        Optional<Account> findUser = accountRepository.findByUsernameIgnoreCase(username);
//        if (findUser.isEmpty()) {
//            throw new UserNotFoundException(username, UserNotFoundException.USER_NOT_FOUND);
//        }
        return findUser.get();
    }

    /**
     * 로그인 비밀번호 오류 카운트 추가
     */
    @Transactional
    public void addPasswordFailCnt(String userName) {
        accountRepository.addPasswordFailCntCount(userName);
    }

    /**
     * 비밀번호 리셋
     *
     * @param username
     */
    @Transactional
    public void resetPasswordFailure(String username) {
        accountRepository.resetPasswordFailure(username);
    }

    /**
     * 로그인 성공
     *
     * @param clientIp
     * @param username
     */
    @Transactional
    public void loginSuccess(String clientIp, String username) {
        accountRepository.setLoginSuccess(LocalDateTime.now(), clientIp, username);
    }

}
