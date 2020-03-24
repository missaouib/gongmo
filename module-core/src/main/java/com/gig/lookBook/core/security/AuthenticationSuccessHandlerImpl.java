package com.gig.lookBook.core.security;

import com.gig.lookBook.core.service.AccountService;
import com.gig.lookBook.core.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 로그인 성공 핸들러
 *
 * @author Giwoon Koo
 * @date 2019-04-09
 */
@Component
@Slf4j
public class AuthenticationSuccessHandlerImpl extends SavedRequestAwareAuthenticationSuccessHandler {
    final int SESSION_TIMEOUT = 60 * 60; //1시간

//    @Autowired
//    LogService logService;

    @Autowired
    AccountService accountService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        super.onAuthenticationSuccess(request, response, authentication);
        request.getSession().setMaxInactiveInterval(SESSION_TIMEOUT);

        User user = (User) authentication.getPrincipal();
        String clientIp = CommonUtils.getClientIP(request);
        accountService.loginSuccess(clientIp, user.getUsername());
//        logService.saveLoginLog(user.getUsername(), clientIp, YNType.Y);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        String target = request.getParameter("target");

        if(target != null && !"".equals(target))
            return target;
        else
            return super.determineTargetUrl(request, response);
    }
}
