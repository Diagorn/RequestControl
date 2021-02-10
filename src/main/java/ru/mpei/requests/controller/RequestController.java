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
import ru.mpei.requests.domain.requests.OrganisationRequest;
import ru.mpei.requests.domain.requests.PhysicalRequest;
import ru.mpei.requests.domain.requests.Request;
import ru.mpei.requests.domain.requests.RequestState;
import ru.mpei.requests.domain.users.Human;
import ru.mpei.requests.domain.users.User;
import ru.mpei.requests.service.*;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

@Controller
public class RequestController { //Handling the page containing requests

    @Autowired
    private RequestService requestService; //For doing anything with requests

    @Autowired
    private UserService userService; //For doing anything with users

    @Autowired
    private HumanService humanService;

    @Autowired
    private ru.mpei.requests.service.ServiceUtils serviceUtils;

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
            @AuthenticationPrincipal User user,
            Model model) {
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
        List<Request> requests = requestService.getRequestsByStatus("", user);; //
        model.addAttribute("requests", requests);
        model.addAttribute("utils", serviceUtils);
        return "requests_list";
    }

    @GetMapping("/request-create/organisation/{id}")
    public String getOrganisationRequestCreateAddPeoplePage(@PathVariable Long id,
                                                            @AuthenticationPrincipal User user,
                                                            Model model) {
        OrganisationRequest request = requestService.getOrganisationRequestByID(id);
        if (!user.getId().equals(request.getOrganisation().getUser().getId()))
            return "redirect:/request";
        model.addAttribute("request", request);
        model.addAttribute("employees", request.getEmployees());
        model.addAttribute("utils", serviceUtils);
        return "organisation_request_create";
    }

    @GetMapping("/request-create/organisation/{id}/add")
    public String getPeopleAddPage(
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            Model model
    ) {
        OrganisationRequest request = requestService.getOrganisationRequestByID(id);
        if (!user.getId().equals(request.getOrganisation().getUser().getId()))
            return "redirect:/request";
        List<Human> employees = requestService.getAllEmployeesForRequest(request);
        model.addAttribute("request", request);
        model.addAttribute("employees", request.getEmployees());
        return "add_employees";
    }

    @PostMapping("/request-create/organisation/{id}/add")
    public String addEmployeeToRequest(
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            @RequestParam String lastName,
            @RequestParam String firstName,
            @RequestParam String secondName,
            @RequestParam String telephone,
            @RequestParam String email,
            @RequestParam String passport,
            @RequestParam String adress,
            @RequestParam String education,
            @RequestParam String dob
    ) throws ParseException {
        OrganisationRequest request = requestService.getOrganisationRequestByID(id);
        if (!user.getId().equals(request.getOrganisation().getUser().getId()))
            return "redirect:/request";
        Human human = humanService.createEmployee(lastName, firstName, secondName, telephone, email, passport, adress, education, dob);
        requestService.addEmployeeToOrganisationRequest(human, request);
        return "redirect:/request-create/organisation/{id}/add";
    }

    @PostMapping("/request-create/organisation/{id}/delete")
    public String deleteEmployee(
            @PathVariable Long id,
            @RequestParam Long employeeID,
            @AuthenticationPrincipal User user
    ) {
        OrganisationRequest request = requestService.getOrganisationRequestByID(id);
        requestService.deleteEmployeeByID(request, employeeID);
        return "redirect:/request-create/organisation/{id}";
    }

    @PostMapping("/request-create/organisation/{id}/save")
    public String saveOrganisationRequest(
            @PathVariable Long id
    ) {
        OrganisationRequest request = requestService.getOrganisationRequestByID(id);
        requestService.setStatus(request, RequestState.NO_EXECUTER);
        return "redirect:/request";
    }

    @GetMapping("/physical/request/{id}/set-executer") //Showing all the executers that can be set
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MODER')")
    public String getPhysicalRequestExecuterSetPage(
            @RequestParam(required = false, defaultValue = "") String search,
            @PathVariable Long id,
            Model model
    ) {
        List<User> users;
        if (search != null && !search.isEmpty()) {
            users = userService.getExecutersByQuery(search);
        } else {
            users = userService.getAllExecuters();
        }
        model.addAttribute("users", users);
        model.addAttribute("userService", userService);
        if (requestService.getPhysicalRequestByID(id) != null) {
            PhysicalRequest request = requestService.getPhysicalRequestByID(id);
            model.addAttribute("request", request);
        }
        return "set-executer";
    }

    @GetMapping("/organisation/request/{id}/set-executer") //Showing all the executers that can be set
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MODER')")
    public String getOrganisationRequestExecuterSetPage(
            @RequestParam(required = false, defaultValue = "") String search,
            @PathVariable Long id,
            Model model
    ) {
        List<User> users;
        if (search != null && !search.isEmpty()) {
            users = userService.getExecutersByQuery(search);
        } else {
            users = userService.getAllExecuters();
        }
        model.addAttribute("users", users);
        model.addAttribute("userService", userService);
        if (requestService.getOrganisationRequestByID(id) != null)
            model.addAttribute("request", requestService.getOrganisationRequestByID(id));
        return "set-executer";
    }

    @PostMapping("/organisation/request/{id}/set-executer") //Setting executer for the request
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MODER')")
    public String setOrganisationRequestExecuter(
            @PathVariable("id") Long requestId,
            @RequestParam Long userId
    ) {
        if (requestService.canSetExecuter(requestId, userId, false))
            requestService.setExecuter(requestId, userId, false);
        return "redirect:/request";
    }

    @PostMapping("/physical/request/{id}/set-executer") //Setting executer for the request
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MODER')")
    public String setExecuter(
            @PathVariable("id") Long requestId,
            @RequestParam Long userId
    ) {
        if (requestService.canSetExecuter(requestId, userId, true))
            requestService.setExecuter(requestId, userId, true);
        return "redirect:/request";
    }

    @PostMapping("/organisation/request/{id}/complete")
    public String completeOrganisationRequest( //Setting the complete status for the request
            @PathVariable("id") Long requestId
    ) {
        if (requestService.getOrganisationRequestByID(requestId) != null) {
            requestService.setStatus(requestService.getOrganisationRequestByID(requestId), RequestState.COMPLETE);
        }
        return "redirect:/request";
    }

    @PostMapping("/physical/request/{id}/complete")
    public String completePhysicalRequest( //Setting the complete status for the request
                                   @PathVariable("id") Long requestId
    ) {
        if (requestService.getPhysicalRequestByID(requestId) != null) {
            requestService.setStatus(requestService.getPhysicalRequestByID(requestId), RequestState.COMPLETE);
        }
        return "redirect:/request";
    }

    @PostMapping("/organisation/request/{id}/freeze")
    public String freezeOrganisationRequest( //Setting the freeze status for request
            @PathVariable("id") Long requestId
    ) {
        if (requestService.getOrganisationRequestByID(requestId) != null) {
            requestService.setStatus(requestService.getOrganisationRequestByID(requestId), RequestState.FROZEN);
        }
        return "redirect:/request";
    }

    @PostMapping("/physical/request/{id}/freeze")
    public String freezePhysicalRequest( //Setting the freeze status for request
                                             @PathVariable("id") Long requestId
    ) {
        if (requestService.getPhysicalRequestByID(requestId) != null) {
            requestService.setStatus(requestService.getPhysicalRequestByID(requestId), RequestState.FROZEN);
        }
        return "redirect:/request";
    }

    @PostMapping("/organisation/request/{id}/delete")
    public String deleteOrganisationRequest( //Deleting the request
            @PathVariable("id") Long requestId
    ) {
        if (requestService.getOrganisationRequestByID(requestId) != null) {
            requestService.setStatus(requestService.getOrganisationRequestByID(requestId), RequestState.DELETED);
        }
        return "redirect:/request";
    }

    @PostMapping("/physical/request/{id}/delete")
    public String deletePhysicalRequest( //Deleting the request
                                 @PathVariable("id") Long requestId
    ) {
        if (requestService.getPhysicalRequestByID(requestId) != null) {
            requestService.setStatus(requestService.getPhysicalRequestByID(requestId), RequestState.DELETED);
        }
        return "redirect:/request";
    }

    @PostMapping("/organisation/request/{id}/unfreeze")
    public String unfreezeOrganisationRequest( //Refreshing the request and setting status "in process"
            @PathVariable("id") Long requestId
    ) {
        if (requestService.getOrganisationRequestByID(requestId) != null) {
            requestService.setStatus(requestService.getOrganisationRequestByID(requestId), RequestState.IN_PROCESS);
        }
        return "redirect:/request";
    }

    @PostMapping("/physical/request/{id}/unfreeze")
    public String unfreezePhysicalRequest( //Refreshing the request and setting status "in process"
                                   @PathVariable("id") Long requestId
    ) {
        if (requestService.getPhysicalRequestByID(requestId) != null) {
            requestService.setStatus(requestService.getPhysicalRequestByID(requestId), RequestState.IN_PROCESS);
        }
        return "redirect:/request";
    }
}