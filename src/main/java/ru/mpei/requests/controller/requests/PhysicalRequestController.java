package ru.mpei.requests.controller.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mpei.requests.domain.requests.PhysicalRequest;
import ru.mpei.requests.domain.requests.RequestState;
import ru.mpei.requests.domain.users.User;
import ru.mpei.requests.service.RequestService;
import ru.mpei.requests.service.UserService;

import java.util.List;

@Controller
public class PhysicalRequestController {
    @Autowired
    private UserService userService;

    @Autowired
    private RequestService requestService;

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

    @PostMapping("/physical/request/{id}/complete")
    public String completePhysicalRequest( //Setting the complete status for the request
                                           @PathVariable("id") Long requestId
    ) {
        if (requestService.getPhysicalRequestByID(requestId) != null) {
            requestService.setStatus(requestService.getPhysicalRequestByID(requestId), RequestState.COMPLETE);
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

    @PostMapping("/physical/request/{id}/delete")
    public String deletePhysicalRequest( //Deleting the request
                                         @PathVariable("id") Long requestId
    ) {
        if (requestService.getPhysicalRequestByID(requestId) != null) {
            requestService.setStatus(requestService.getPhysicalRequestByID(requestId), RequestState.DELETED);
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
