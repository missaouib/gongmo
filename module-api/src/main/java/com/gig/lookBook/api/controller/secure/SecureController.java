package com.gig.lookBook.api.controller.secure;

import com.gig.lookBook.core.dto.account.AccountDto;
import com.gig.lookBook.core.exception.UserNotFoundException;
import com.gig.lookBook.core.model.Account;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/secure")
@RequiredArgsConstructor
public class SecureController {

    @GetMapping("sign-up")
    public String index(Model model){
        model.addAttribute(new AccountDto());
        return "account/editor";
    }
}
