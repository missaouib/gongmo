package com.gig.lookBook.api.controller.secure;

import com.gig.lookBook.core.dto.account.AccountReqDto;
import com.gig.lookBook.core.exception.NotFoundException;
import com.gig.lookBook.core.model.Account;
import com.gig.lookBook.core.service.AccountService;
import com.gig.lookBook.core.validation.AccountReqValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/secure")
@RequiredArgsConstructor
public class SecureController {

    private final AccountReqValidator accountReqValidator;
    private final AccountService accountService;

    @InitBinder("accountReqValidator")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(accountReqValidator);
    }

    @GetMapping("sign-up")
    public String index(Model model){
        model.addAttribute(new AccountReqDto());
        return "account/editor";
    }

    @PostMapping("sign-up")
    public String signUpSubmit(@Valid AccountReqDto accountReqDto, Errors errors) {
        if (errors.hasErrors()) {
            return "account/editor";
        }
        accountService.processNewAccountByFront(accountReqDto);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
