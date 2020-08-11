package ru.mpei.requests.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mpei.requests.domain.requests.Request;
import ru.mpei.requests.domain.requests.RequestState;
import ru.mpei.requests.domain.users.User;
import ru.mpei.requests.repos.RequestRepo;
import ru.mpei.requests.repos.UserRepo;

import java.util.List;
import java.util.Optional;

@Service
public class RequestService {
    @Autowired
    RequestRepo requestRepo; //For doing anything with requests

    @Autowired
    ChatService chatService; //For doing anything with chats

    @Autowired
    UserRepo userRepo;

    @Autowired
    UserService userService;

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
//        switch (status) {
//            case "no_executer":
//                requests = requestRepo.findAllByStatus(RequestState.NO_EXECUTER);
//                break;
//            case "in_process":
//                requests = requestRepo.findAllByStatus(RequestState.IN_PROCESS);
//                break;
//            case "frozen":
//                requests = requestRepo.findAllByStatus(RequestState.FROZEN);
//                break;
//            case "complete":
//                requests = requestRepo.findAllByStatus(RequestState.COMPLETE);
//                break;
//            case "deleted":
//                requests = requestRepo.findAllByStatus(RequestState.DELETED);
//                break;
//            default:
//                requests = requestRepo.findAll();
//                break;
//        }
//        if (!user.isAdmin() && !user.isModer() && !requests.isEmpty()) {
//            requests.removeIf(r -> r.getClient() != user && r.getExecuter() != user);
//        }
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
        Request request = new Request();
        requestRepo.save(request);
        request.setChat(chatService.createChatForClient(client, request));
        request.setClient(client);
        request.setTheme(theme);
        request.setStatus(RequestState.NO_EXECUTER);
        requestRepo.save(request);
    }

    public boolean canSetExecuter(Long requestID, Long userID) {
        Request request = requestRepo.findById(requestID).orElse(null);
        User user = userRepo.findById(userID).orElse(null);
        return (user != null && request != null);
    }

    public void setExecuter(Long requestID, Long userID) {
        Request request = requestRepo.findById(requestID).orElse(null);
        User user = userRepo.findById(userID).orElse(null);
        request.setExecuter(user);
        request.setStatus(RequestState.IN_PROCESS);
        requestRepo.save(request);
        userRepo.save(user);
    }

    public Request getRequestByID(Long id) {
        Optional<Request> optionalRequest = requestRepo.findById(id);
        return optionalRequest.orElse(null);
    }

    public void setStatus(Request request, RequestState status) {
        request.setStatus(status);
        requestRepo.save(request);
    }
}