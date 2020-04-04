package com.gig.lookBook.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Jake
 * @date: 24/3/20
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @RequestMapping
    public ModelAndView index() { return new ModelAndView("index"); }
}
