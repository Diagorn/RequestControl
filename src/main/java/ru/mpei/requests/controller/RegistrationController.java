package ru.mpei.requests.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.mpei.requests.domain.users.Organisation;
import ru.mpei.requests.service.HumanService;
import ru.mpei.requests.service.OrganisationService;
import ru.mpei.requests.service.UserService;

import java.io.IOException;
import java.text.ParseException;

@Controller
public class RegistrationController { //Used to register new users
    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    private OrganisationService organisationService;

    @Autowired
    private UserService userService; //To use the business logic

    @Autowired
    private RestTemplate restTemplate; //For contacting other sites

    @Autowired
    private HumanService humanService;

    @Value("${recaptcha.secret}")
    private String secret; //Secret captcha code

    @GetMapping("/registration/organisation")
    public String getOrganisationRegistrationPage() {
        return "organisation";
    }

    @PostMapping("/registration/organisation")
    public String registerOrganisation(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("name") String name,
            @RequestParam("physicalAdress") String physicalAdress,
            @RequestParam("legalAdress") String legalAdress,
            @RequestParam("phone") String phone,
            @RequestParam("firstName") String firstName,
            @RequestParam("secondName") String secondName,
            @RequestParam("lastName") String lastName,
            @RequestParam("position") String position,
            @RequestParam("telephone") String directorPhone,
            @RequestParam("avatar") MultipartFile avatar
    ) throws IOException {
        Organisation organisation = organisationService.saveOrganisation(username, password, name, physicalAdress, legalAdress, phone, avatar);
        humanService.saveDirectorOfOrganisation(organisation, firstName, secondName, lastName, position, directorPhone);
        return "redirect:/login";
    }

    @GetMapping("/registration/physical")
    public String getRegisterPhysicalPage() {
        return "physical";
    }

    @PostMapping("/registration/physical")
    public String registerPhysical(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String lastName,
            @RequestParam String firstName,
            @RequestParam String secondName,
            @RequestParam String telephone,
            @RequestParam String passport,
            @RequestParam String adress,
            @RequestParam String education,
            @RequestParam String dob,
            @RequestParam MultipartFile avatar,
            @RequestParam String passportDate,
            @RequestParam String passportOrgan,
            @RequestParam String index,
            @RequestParam String sex,
            @RequestParam String citizenship,
            @RequestParam String speciality,
            @RequestParam String groupName,
            @RequestParam(required = false, defaultValue = "false") boolean agreed
            ) throws IOException, ParseException {
        if (!agreed)
            return "redirect:/registration/physical";
        humanService.saveHumanFromForm(username, password, lastName, firstName, secondName, telephone, passport, adress, education,
                dob, avatar, passportDate, passportOrgan, index, citizenship, speciality, groupName, sex);
        return "redirect:/login";
    }
}