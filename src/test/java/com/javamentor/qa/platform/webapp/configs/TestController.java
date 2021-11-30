package com.javamentor.qa.platform.webapp.configs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
    private final TestingService service;

    public TestController(TestingService service) {
        this.service = service;
    }

    @RequestMapping("/")
    public @ResponseBody String testing() {
        return "Hello, World";
    }
}
