package ru.mpei.requests.controller.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mpei.requests.domain.requests.Request;
import ru.mpei.requests.domain.requests.RequestState;
import ru.mpei.requests.domain.users.User;
import ru.mpei.requests.service.RequestService;
import ru.mpei.requests.service.UserService;

import java.util.List;

@Controller
public class RequestController { //Handling the page containing requests
    @Autowired
    private RequestService requestService; //For doing anything with requests

    @Autowired
    private UserService userService; //For doing anything with user

    @Autowired
    private ru.mpei.requests.service.ServiceUtils serviceUtils;

    @GetMapping("/")
    public String getRequestPage() {
        return "redirect:/request";
    }

    @GetMapping("/request")
    public String getRequestsForUser( //Get-mapping to show just the requests for the authentificated user
            @AuthenticationPrincipal User user,
            @RequestParam(required = false, defaultValue = "") String status,
            Model model
    ) {
        List<Request> requests = requestService.getRequestsByStatus(status, user);; //requestService.getRequestsByStatus(status, user);
        model.addAttribute("requests", requests);
        model.addAttribute("statuses", RequestState.values());
        model.addAttribute("utils", serviceUtils);
        return "requests_list";
    }

    @GetMapping("/request-create") //Just getting the request creating page
    public String getRequestCreatePage(
            @AuthenticationPrincipal User currentUser,
            Model model
    ) {
        if (!currentUser.isModer() && !currentUser.isAdmin()) {
            model.addAttribute("isClientRequest", true);
            model.addAttribute("client", currentUser);
        }
        else {
            model.addAttribute("isClientRequest", false);
        }
        return "request_create";
    }

    @PostMapping("/request-create") //Finding the user on request creating page
    public String findClient(
            @RequestParam String name,
            Model model) {
        User client;
        client = userService.getClientByQuery(name);
        if (client == null) {
            model.addAttribute("message", "Пользователь не найден");
        } else {
            model.addAttribute("client", client);
        }
        model.addAttribute("query", name);
        model.addAttribute("isClientRequest", false);
        return "request_create";
    }

    @PostMapping("/request") //Creating the request
    public String createRequest (
            @RequestParam String username,
            @RequestParam String theme,
            @AuthenticationPrincipal User user) {
        User client;
        if (username.equals("") || username.isEmpty())
            client = user;
        else
            client = userService.findUserByQuery(username);
        if (client.isPhysical()) {
            Long id = requestService.createPhysicalRequest(client, theme);
        } else {
            Long id = requestService.createOrganisationRequest(client, theme);
            return "redirect:/request-create/organisation/" + id.toString();
        }
        return "redirect:/request";
    }
}