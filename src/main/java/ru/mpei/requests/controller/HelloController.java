package ru.mpei.requests.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.mpei.requests.service.RequestService;

//Used only for the beginning of the development
@Controller
public class HelloController {

    @GetMapping("/")
    public String sayHelloToUser() {
        return "redirect:/request";
    }
}
