package ru.mpei.requests.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.mpei.requests.domain.DTO.CaptchaResponseDTO;
import ru.mpei.requests.domain.User;
import ru.mpei.requests.service.UserService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController { //Used to register new users
    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    private UserService userService; //To use the business logic

    @Autowired
    private RestTemplate restTemplate; //For contacting other sites

    @Value("${recaptcha.secret}")
    private String secret; //Secret captcha code

    @GetMapping("/registration")
    public String getRegisterPage() {
        return "registration";
    } //Just showing the page

    @PostMapping("/registration")
    public String register( //Adding a new user to the system and checking if the form is filled with no errors
            @RequestParam("password2") String passwordConfirm,
            @RequestParam("g-recaptcha-response") String captchaResponse,
            @RequestParam("avatar") MultipartFile file,
            @Valid User user,
            BindingResult bindingResult, //Errors
            Model model) throws IOException { //Creating a new account
        String captchaURL = String.format(CAPTCHA_URL, secret, captchaResponse); //URL for the captcha feedback send
        CaptchaResponseDTO response = restTemplate.postForObject(captchaURL, Collections.emptyList(), CaptchaResponseDTO.class);
        if (!response.isSuccess()) {
            model.addAttribute("captchaError", "Подтвердите, что вы не робот");
        }
        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);
        if (isConfirmEmpty) {
            model.addAttribute("password2Error", "Подтверждение пароля не может быть пустым");
        }
        if (user.getPassword() != null || !user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "Пароли не совпадают");
        }

        if(isConfirmEmpty || bindingResult.hasErrors() || !response.isSuccess()) { //If we have errors
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            return "registration";
        }
        if (userService.isPossibleToCreateAUser(user.getUsername())) { //If the user can be created
            userService.createUser(user);
            userService.setUserAvatar(user, file);
            model.addAttribute("message", ""); //We don't need to show anything
            return "redirect:/login"; //Going to login page
        } else { //If we cannot create a user
            model.addAttribute("usernameErr", "Этот e-mail уже занят"); //We tell it to the user
            return "registration"; //And return the registration page again
        }
    }
}