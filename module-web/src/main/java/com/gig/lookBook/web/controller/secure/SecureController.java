package com.gig.lookBook.web.controller.secure;

import com.gig.lookBook.core.dto.account.AccountDto;
import com.gig.lookBook.core.exception.UserNotFoundException;
import com.gig.lookBook.core.model.Account;
import com.gig.lookBook.core.service.AccountService;
import com.gig.lookBook.core.validation.SignUpFormValidator;
import javassist.NotFoundException;
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

    private final SignUpFormValidator signUpFormValidator;

    private final AccountService accountService;

    @InitBinder("accountDto")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("sign-up")
    public String index(Model model){
        model.addAttribute(new AccountDto());
        return "account/editor";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid AccountDto accountDto, Errors errors) throws UserNotFoundException, NotFoundException {
        if (errors.hasErrors()) {
            return "account/editor";
        }
        Account newAccount = accountService.createUser(accountDto);
        return "redirect:/";
    }

}
