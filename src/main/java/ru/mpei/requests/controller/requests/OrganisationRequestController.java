package ru.mpei.requests.controller.requests;

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
import ru.mpei.requests.domain.requests.RequestState;
import ru.mpei.requests.domain.users.Human;
import ru.mpei.requests.domain.users.User;
import ru.mpei.requests.service.HumanService;
import ru.mpei.requests.service.RequestService;
import ru.mpei.requests.service.ServiceUtils;
import ru.mpei.requests.service.UserService;

import java.text.ParseException;
import java.util.List;

@Controller
public class OrganisationRequestController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private ServiceUtils serviceUtils;

    @Autowired
    private HumanService humanService;

    @Autowired
    private UserService userService;

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

    @PostMapping("/organisation/request/{id}/complete")
    public String completeOrganisationRequest( //Setting the complete status for the request
                                               @PathVariable("id") Long requestId
    ) {
        if (requestService.getOrganisationRequestByID(requestId) != null) {
            requestService.setStatus(requestService.getOrganisationRequestByID(requestId), RequestState.COMPLETE);
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

    @PostMapping("/organisation/request/{id}/delete")
    public String deleteOrganisationRequest( //Deleting the request
                                             @PathVariable("id") Long requestId
    ) {
        if (requestService.getOrganisationRequestByID(requestId) != null) {
            requestService.setStatus(requestService.getOrganisationRequestByID(requestId), RequestState.DELETED);
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
}
