package ru.mpei.requests.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.mpei.requests.domain.users.Role;
import ru.mpei.requests.domain.users.User;
import ru.mpei.requests.service.ServiceUtils;
import ru.mpei.requests.service.UserService;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class UserController { //Admin panel controller
    @Autowired
    private UserService userService; //For doing anything with users

    @Autowired
    private ServiceUtils serviceUtils;


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user")
    public String getAllUsers( //Getting the admin panel
            @RequestParam(required = false, defaultValue = "") String search,
            Model model
    ) {
        if (search == null || search.equals(""))
            model.addAttribute("users", userService.findAll()); //Showing all the users
        else
            model.addAttribute("users", userService.getAllUsersByQuery(search));
        model.addAttribute("userService", userService);
        model.addAttribute("roles", Role.values());
        model.addAttribute("deleteUsers", Collections.emptyList());
        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user/{user}") //Updating the roles for the user
    public String editUsers(@PathVariable User user, Model model) { //Showing the edit page
        model.addAttribute("users", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("userService", userService);
        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/user")
    public String saveEditedUser( //Handling the changes in particular user
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user, Model model) {
        userService.saveUser(user, username, form);
        model.addAttribute("roles", Role.values());
        model.addAttribute("deleteUsers", Collections.emptySet());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("userService", userService);
        return "redirect:/user";
    }

    @GetMapping("/user/profile") //Getting the profile page that can change the user's data
    public String getProfilePage(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("utils", serviceUtils);
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/user/profile")
    public String updateProfile( //Saving the updated data
            @AuthenticationPrincipal User user,
            @RequestParam String firstName,
            @RequestParam String secondName,
            @RequestParam String lastName,
            @RequestParam String telephone,
            @RequestParam(required = false, defaultValue = "1") String dob,
            @RequestParam String sex,
            @RequestParam String passport,
            @RequestParam(required = false, defaultValue = "1") String passportDate,
            @RequestParam String passportOrgan,
            @RequestParam String citizenship,
            @RequestParam String adress,
            @RequestParam String index,
            @RequestParam(required = false, defaultValue = "1") String education,
            @RequestParam String speciality,
            @RequestParam String groupName,
            @RequestParam("avatar") MultipartFile avatar) throws IOException {
        if (avatar != null && !avatar.getOriginalFilename().isEmpty()) {
            userService.setUserAvatar(user, avatar);
        }
        userService.updateProfile(user, firstName, secondName, lastName, telephone, dob, sex,
                passport, passportDate, passportOrgan, citizenship, adress,
                index, education, speciality, groupName);
        return "redirect:/user/profile";
    }

    @GetMapping("/user/search")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String findUsersToDelete( //Finding the users that may be deleted
            @RequestParam String search,
            @AuthenticationPrincipal User currentUser,
            Model model
    ) {
        List<User> users;
        if (!search.isEmpty()) {
            users = userService.getAllUsersByQuery(search);
            users.remove(currentUser);
        } else {
            users = Collections.emptyList();
        }
        model.addAttribute("deleteUsers", users);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", Role.values());
        model.addAttribute("userService", userService);
        return "userList";
    }

    @PostMapping("/user/delete") //Delete selected users from admin page
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteUsers(
            @RequestParam Map<String, String> form,
            Model model
    ) {
        if (form.keySet().size() > 1) {//Not 0 because of the _csrf
            List<String> names = new ArrayList<>(form.keySet());
            userService.deleteUsersByNames(names);
        }
        model.addAttribute("deleteUsers", Collections.emptySet());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", Role.values());
        model.addAttribute("userService", userService);
        return "userList";
    }

    @GetMapping("/user/contacts") //Get all the admins and moders
    public String getSearchContacts(
            @RequestParam(required = false, defaultValue = "") String search,
            Model model
    ) {
        List<User> users;
        if (search != null && !search.isEmpty()) {
            users = userService.getModersAndAdminsByQuery(search);
        } else {
            users = userService.getAllModersAndAdmins();
        }
        model.addAttribute("users", users);
        model.addAttribute("userService", userService);
        model.addAttribute("roles", Role.values());
        return "contacts";
    }
}