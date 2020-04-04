package com.gig.lookBook.web.config;

import com.gig.lookBook.core.dto.AccountDto;
import com.gig.lookBook.core.dto.ApiResultDto;
import com.gig.lookBook.core.security.LoginUser;
import com.gig.lookBook.core.service.AccountService;
import com.gig.lookBook.core.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;

/**
 * 로그인 성공 핸들러
 *
 * @author Giwoon Koo
 * @date 2019-04-09
 */
@Component
@Slf4j
public class FrontAuthenticationSuccessHandlerImpl extends SimpleUrlAuthenticationSuccessHandler {

    final int SESSION_TIMEOUT = 60 * 60; //1시간

//    @Autowired
//    LogService logService;

    @Autowired
    AccountService accountService;

    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        request.getSession().setMaxInactiveInterval(SESSION_TIMEOUT);

        User user = (User) authentication.getPrincipal();
        String clientIp = CommonUtils.getClientIP(request);
        accountService.loginSuccess(clientIp, user.getUsername());
//        logService.saveLoginLog(user.getUsername(), clientIp, YNType.Y);

        String roleName = null;
        Iterator<GrantedAuthority> itr = user.getAuthorities().iterator();

        while (itr.hasNext()) {
            roleName = itr.next().getAuthority();
        }

//        if(roleName != null && "ROLE_USER".equals(roleName)) {
//            try {
//
//            } catch (UserNotFoundException | ParseException e) {
//                e.printStackTrace();
//            }
//        }

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest != null) {
            savedRequest.getRedirectUrl();
            requestCache.removeRequest(request, response);
            clearAuthenticationAttributes(request);
        }

        String accept = request.getHeader("accept");

        if (accept == null || accept.matches(".*application/json.*") == false) {
            if ("ROLE_PARTNER".equals(roleName)) getRedirectStrategy().sendRedirect(request, response, "/partner/cep/index");
            else getRedirectStrategy().sendRedirect(request, response, savedRequest == null ? "/" :savedRequest.getRedirectUrl());
            
            return;
        }

        // application/json(ajax) 요청일 경우 아래의 처리!
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        MediaType jsonMimeType = MediaType.APPLICATION_JSON;

        LoginUser loginUser = (LoginUser) user;
        AccountDto accountDto = new AccountDto(loginUser.getLoginAccount());

        ApiResultDto<Object> result = new ApiResultDto<>(ApiResultDto.RESULT_CODE_OK);
        result.setData(accountDto);

        if (jsonConverter.canWrite(result.getClass(), jsonMimeType)) {
            jsonConverter.write(result, jsonMimeType, new ServletServerHttpResponse(response));
        }
    }
}
