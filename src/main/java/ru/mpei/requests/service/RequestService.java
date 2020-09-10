package ru.mpei.requests.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mpei.requests.domain.requests.OrganisationRequest;
import ru.mpei.requests.domain.requests.PhysicalRequest;
import ru.mpei.requests.domain.requests.Request;
import ru.mpei.requests.domain.requests.RequestState;
import ru.mpei.requests.domain.users.Organisation;
import ru.mpei.requests.domain.users.User;
import ru.mpei.requests.repos.OrganisationRequestRepo;
import ru.mpei.requests.repos.PhysicalRequestRepo;
import ru.mpei.requests.repos.RequestRepo;
import ru.mpei.requests.repos.UserRepo;

import java.util.List;
import java.util.Optional;

@Service
public class RequestService {

    @Autowired
    private RequestRepo requestRepo;

    @Autowired
    private PhysicalRequestRepo physicalRequestRepo;

    @Autowired
    private OrganisationRequestRepo organisationRequestRepo;

    @Autowired
    private ChatService chatService; //For doing anything with chats

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    public List<Request> getRequestsForUser(User user) { //Get request page with only those requests which
        List<Request> requests; //Contain this particular user
        if (user.isAdmin() || user.isModer()) {
            requests = requestRepo.findAll();
        } else if (user.isExecuter() && user.isClient()) {
            requests = requestRepo.findAllByExecuterAndClient(user, user);
        } else if (user.isExecuter()) {
            requests = requestRepo.findAllByExecuter(user);
        } else {
            requests = requestRepo.findAllByClient(user);
        }
        requests.removeIf(r -> r.getStatus() == RequestState.DELETED);
        return requests;
    }

    public List<Request> getRequestsByStatus(String status, User user) {
        List<Request> requests;
        switch (status) {
            case "no_executer":
                requests = requestRepo.findAllByStatus(RequestState.NO_EXECUTER);
                break;
            case "in_process":
                requests = requestRepo.findAllByStatus(RequestState.IN_PROCESS);
                break;
            case "frozen":
                requests = requestRepo.findAllByStatus(RequestState.FROZEN);
                break;
            case "complete":
                requests = requestRepo.findAllByStatus(RequestState.COMPLETE);
                break;
            case "deleted":
                requests = requestRepo.findAllByStatus(RequestState.DELETED);
                break;
            default:
                requests = requestRepo.findAll();
                break;
        }
        if (!user.isAdmin() && !user.isModer() && !requests.isEmpty()) {
            requests.removeIf(r -> r.getClient() != user && r.getExecuter() != user);
        }
        if (user.isAdmin() || user.isModer()) {
            requests = requestRepo.findAll();
        } else if (user.isExecuter() && user.isClient()) {
            requests = requestRepo.findAllByExecuterAndClient(user, user);
        } else if (user.isExecuter()) {
            requests = requestRepo.findAllByExecuter(user);
        } else {
            requests = requestRepo.findAllByClient(user);
        }
        if (status != null && !status.equals(""))
        for (RequestState s: RequestState.values()) {
            requests.removeIf(r -> !r.getStatus().name().toLowerCase().equals(status));
        }
        return requests;
    }

    public void createRequest(User client, String theme) {
//        Request request = new Request();
//        //requestRepo.save(request);
//        //request.setChat(chatService.createChatForClient(client, request));
//        request.setClient(client);
//        request.setTheme(theme);
//        request.setStatus(RequestState.NO_EXECUTER);
////        requestRepo.save(request);
    }

    public boolean canSetExecuter(Long requestID, Long userID) {
        Request request = requestRepo.findById(requestID).orElse(null);
        User user = userRepo.findById(userID).orElse(null);
        return (user != null && request != null);
    }

    public void setExecuter(Request request, Long userID) {
        User user = userRepo.findById(userID).orElse(null);
        request.setExecuter(user);
        request.setStatus(RequestState.IN_PROCESS);

        if (user != null) {
            if (request.getClass().equals(OrganisationRequest.class)) {
                user.getExecuterOrganisationRequests().add((OrganisationRequest) request);
            } else {
                user.getExecuterPhysicalRequests().add((PhysicalRequest) request);
            }
        }

        requestRepo.save(request);
    }

    public OrganisationRequest getOrganisationRequestByID(Long id) {
        Optional<OrganisationRequest> optionalRequest = organisationRequestRepo.findById(id);
        return optionalRequest.orElse(null);
    }

    public PhysicalRequest getPhysicalRequestByID(Long id) {
        Optional<PhysicalRequest> optionalRequest = physicalRequestRepo.findById(id);
        return optionalRequest.orElse(null);
    }

    public void setStatus(Request request, RequestState status) {
        request.setStatus(status);
        requestRepo.save(request);
    }
}