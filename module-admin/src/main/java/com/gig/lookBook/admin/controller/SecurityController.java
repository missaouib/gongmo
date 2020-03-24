package com.gig.lookBook.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class SecurityController {

    @RequestMapping("login")
    public String login() { return "login"; }
}
