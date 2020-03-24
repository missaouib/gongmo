package com.gig.lookBook.core.security;

import com.gig.lookBook.core.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 로그인 실패 핸들러
 *
 * @author Giwoon Koo
 * @date 2019-04-09
 */
@Component
@Slf4j
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

//    @Autowired
//    LogService logService;

    @Autowired
    AccountService accountService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");

        accountService.addPasswordFailCnt(username);
//        logService.saveLoginLog(username, CommonUtils.getClientIP(request), YNType.N);

        if (exception instanceof BadCredentialsException) {
            request.setAttribute("exceptionMessage", "계정 또는 비밀번호가 틀렸습니다.");
        } else if (exception instanceof DisabledException) {
            request.setAttribute("exceptionMessage", "잠긴 계정 입니다. 관리자에게 문의 하세요.");
        } else if (exception instanceof LockedException) {
            request.setAttribute("exceptionMessage", "패스워드가 5회 이상 틀렸습니다. 관리자에게 문의 하세요.");
        } else if (exception instanceof CredentialsExpiredException) {
            request.setAttribute("exceptionMessage", "비밀번호 유효기간이 만료되었습니다. 비밀번호를 변경해 주세요.");
        } else if (exception instanceof AccountExpiredException) {
            request.setAttribute("exceptionMessage", "휴면 계정입니다. 휴면 해제 후 사용아 가능합니다.");
        } else {
            request.setAttribute("exceptionMessage", exception.getMessage());
        }

        log.error("Authentication fail username: {}, message: {}", username, request.getAttribute("exceptionMessage"));
        request.getRequestDispatcher("/login?error=500").forward(request, response);
    }
}
