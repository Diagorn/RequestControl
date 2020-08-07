package ru.mpei.requests.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mpei.requests.domain.Request;
import ru.mpei.requests.domain.User;
import ru.mpei.requests.service.RequestService;

import java.util.List;

//Used only for the beginning of the development
@Controller
public class HelloController {
    @Autowired
    private RequestService requestService;

    @GetMapping("/")
    public String sayHelloToUser() {
        return "redirect:/request";
    }
}
