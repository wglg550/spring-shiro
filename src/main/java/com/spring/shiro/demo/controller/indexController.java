package com.spring.shiro.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/index")
public class indexController {

    @RequestMapping("/first")
    public void first() {
        log.debug("welcome index");
    }

    @RequestMapping("/goIndexHtml")
    public String goIndexHtml() {
        log.debug("goIndexHtml");
        return "/html/index.html";
    }
}
