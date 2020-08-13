package ru.mpei.requests.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.mpei.requests.domain.DTO.CaptchaResponseDTO;
import ru.mpei.requests.domain.users.User;
import ru.mpei.requests.service.OrganisationService;
import ru.mpei.requests.service.UserService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController { //Used to register new users
    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    private OrganisationService organisationService;

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
            RedirectAttributes redirectAttributes,
            @RequestParam("password2") String passwordConfirm,
            @RequestParam("g-recaptcha-response") String captchaResponse,
            @RequestParam("isOrganisation") boolean isOrganisation,
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
            userService.createUser(user, isOrganisation);
            userService.setUserAvatar(user, file);
            model.addAttribute("message", ""); //We don't need to show anything
            model.addAttribute("user", user);
            if (isOrganisation) {
                return "redirect:/registration/organisation";
            }
            return "redirect:/registration/physical"; //Going to login page
        } else { //If we cannot create a user
            model.addAttribute("usernameErr", "Этот e-mail уже занят"); //We tell it to the user
            return "registration"; //And return the registration page again
        }
    }

    @GetMapping("/registration/organisation/{id}")
    public String getOrganisationRegistrationPage(
            @PathVariable Long id,
            Model model) {
        model.addAttribute("user", id);
        return "organisation";
    }

    @PostMapping("/registration/organisation")
    public String continueRegistrationOrganisation(
            @RequestParam("user") Long id,
            @RequestParam("name") String name,
            @RequestParam("position") String position,
            @RequestParam("physicalAdress") String physicalAdress,
            @RequestParam("legalAdress") String legalAdress,
            @RequestParam("telephone") String phone
    ) {
        organisationService.saveOrganisation(id, name, position, physicalAdress, legalAdress, phone);
        return "organisation_director";
    }

    @GetMapping("/registration/organisation/{id}/director")
    public String getDirectorSetPage(@PathVariable Long id) {
        return "organisation_director";
    }

    @PostMapping("/registration/organisation/{id}/director")
    public String saveDirectorForOrganisation(
            @PathVariable Long id,
            @RequestParam String lastName,
            @RequestParam String firstName,
            @RequestParam String secondName,
            @RequestParam String phone
    ) {
        organisationService.saveDirector(id, lastName, firstName, secondName, phone);
        return "redirect:/login";
    }

    @GetMapping("/registration/physical")
    public String registerPhysical() {
        return "physical";
    }
}