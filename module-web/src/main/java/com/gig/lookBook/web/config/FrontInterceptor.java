package com.gig.lookBook.web.config;

import com.gig.lookBook.core.dto.AccountDto;
import com.gig.lookBook.core.model.Account;
import com.gig.lookBook.core.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class FrontInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private SecurityService securityService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
        if (mav != null) {
            Account loginAccount = securityService.getLoginUser();
            if (loginAccount != null) {
                AccountDto accountDto = new AccountDto(loginAccount);
                mav.addObject("accountDto", accountDto);
            }
        }

        super.postHandle(request, response, handler, mav);
    }
}
