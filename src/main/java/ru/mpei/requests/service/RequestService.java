package ru.mpei.requests.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mpei.requests.domain.chats.Chat;
import ru.mpei.requests.domain.requests.OrganisationRequest;
import ru.mpei.requests.domain.requests.PhysicalRequest;
import ru.mpei.requests.domain.requests.Request;
import ru.mpei.requests.domain.requests.RequestState;
import ru.mpei.requests.domain.users.Human;
import ru.mpei.requests.domain.users.Organisation;
import ru.mpei.requests.domain.users.User;
import ru.mpei.requests.repos.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class RequestService {

    @Autowired
    private PhysicalRequestRepo physicalRequestRepo;

    @Autowired
    private OrganisationRequestRepo organisationRequestRepo;

    @Autowired
    private ChatService chatService; //For doing anything with chats

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private HumanRepo humanRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private ChatRepo chatRepo;

//    public List<Request> getRequestsForUser(User user) { //Get request page with only those requests which
//        List<Request> requests; //Contain this particular user
//        if (user.isAdmin() || user.isModer()) {
//            requests = requestRepo.findAll();
//        } else if (user.isExecuter() && user.isClient()) {
//            requests = requestRepo.findAllByExecuterAndClient(user, user);
//        } else if (user.isExecuter()) {
//            requests = requestRepo.findAllByExecuter(user);
//        } else {
//            requests = requestRepo.findAllByClient(user);
//        }
//        requests.removeIf(r -> r.getStatus() == RequestState.DELETED);
//        return requests;
//    }

    public List<Request> getRequestsByStatus(String status, User user) {
        List<Request> requests;
        List<OrganisationRequest> organisationRequests;
        List<PhysicalRequest> physicalRequests;
        switch (status) {
            case "no_executer":
                organisationRequests = organisationRequestRepo.findAllByStatus(RequestState.NO_EXECUTER);
                physicalRequests = physicalRequestRepo.findAllByStatus(RequestState.NO_EXECUTER);
                requests = ServiceUtils.getCollidedRequestList(organisationRequests, physicalRequests);
                break;
            case "in_process":
                organisationRequests = organisationRequestRepo.findAllByStatus(RequestState.IN_PROCESS);
                physicalRequests = physicalRequestRepo.findAllByStatus(RequestState.IN_PROCESS);
                requests = ServiceUtils.getCollidedRequestList(organisationRequests, physicalRequests);
                break;
            case "frozen":
                organisationRequests = organisationRequestRepo.findAllByStatus(RequestState.FROZEN);
                physicalRequests = physicalRequestRepo.findAllByStatus(RequestState.FROZEN);
                requests = ServiceUtils.getCollidedRequestList(organisationRequests, physicalRequests);
                break;
            case "complete":
                organisationRequests = organisationRequestRepo.findAllByStatus(RequestState.COMPLETE);
                physicalRequests = physicalRequestRepo.findAllByStatus(RequestState.COMPLETE);
                requests = ServiceUtils.getCollidedRequestList(organisationRequests, physicalRequests);
                break;
            case "deleted":
                organisationRequests = organisationRequestRepo.findAllByStatus(RequestState.DELETED);
                physicalRequests = physicalRequestRepo.findAllByStatus(RequestState.DELETED);
                requests = ServiceUtils.getCollidedRequestList(organisationRequests, physicalRequests);
                break;
            case "prototype":
                organisationRequests = organisationRequestRepo.findAllByStatus(RequestState.PROTOTYPE);
                requests = ServiceUtils.getCollidedRequestList(organisationRequests, new ArrayList<PhysicalRequest>());
                break;
            default:
                organisationRequests = organisationRequestRepo.findAll();
                physicalRequests = physicalRequestRepo.findAll();
                requests = ServiceUtils.getCollidedRequestList(organisationRequests, physicalRequests);
                break;
        }
        if (!user.isAdmin() && !user.isModer() && !requests.isEmpty()) {
            requests.removeIf(r -> r.getClient() != user && r.getExecuter() != user);
        }
        if (user.isAdmin() || user.isModer()) {
            organisationRequests = organisationRequestRepo.findAll();
            physicalRequests = physicalRequestRepo.findAll();
            requests = ServiceUtils.getCollidedRequestList(organisationRequests, physicalRequests);
        } else if (user.isExecuter() && user.isClient()) {
            organisationRequests = organisationRequestRepo.findAllByExecuterAndClient(user, user);
            physicalRequests = physicalRequestRepo.findAllByExecuterAndClient(user, user);
            requests = ServiceUtils.getCollidedRequestList(organisationRequests, physicalRequests);
        } else if (user.isExecuter()) {
            organisationRequests = organisationRequestRepo.findAllByExecuter(user);
            physicalRequests = physicalRequestRepo.findAllByExecuter(user);
            requests = ServiceUtils.getCollidedRequestList(organisationRequests, physicalRequests);
        } else {
            organisationRequests = organisationRequestRepo.findAllByClient(user);
            physicalRequests = physicalRequestRepo.findAllByClient(user);
            requests = ServiceUtils.getCollidedRequestList(organisationRequests, physicalRequests);
        }
        if (!status.equals(""))
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

//    public boolean canSetExecuter(Long requestID, Long userID) {
//        Request request = requestRepo.findById(requestID).orElse(null);
//        User user = userRepo.findById(userID).orElse(null);
//        return (user != null && request != null);
//    }

    public void setExecuter(Request request, Long userID) {
        User user = userRepo.findById(userID).orElse(null);
        request.setExecuter(user);
        request.setStatus(RequestState.IN_PROCESS);

        if (user != null) {
            if (request.getClass().equals(OrganisationRequest.class)) {
                user.getExecuterOrganisationRequests().add((OrganisationRequest) request);
                organisationRequestRepo.save((OrganisationRequest) request);
            } else {
                user.getExecuterPhysicalRequests().add((PhysicalRequest) request);
                physicalRequestRepo.save((PhysicalRequest) request);
            }
            userRepo.save(user);
        }
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
        if (request.getClass().equals(OrganisationRequest.class))
            organisationRequestRepo.save((OrganisationRequest) request);
        else
            physicalRequestRepo.save((PhysicalRequest) request);
    }

    public long createPhysicalRequest(User client, String theme) {
        PhysicalRequest request = new PhysicalRequest();
        request.setClient(client);
        request.setStatus(RequestState.NO_EXECUTER);
        request.setPhysical(true);
        Chat chat = chatService.createChatForClient(client);
        chatRepo.save(chat);
        request.setChat(chat);
        request.setTheme(theme);
        physicalRequestRepo.save(request);
        chatService.setPhysicalRequest(chat, request);
        return request.getId();
    }

    public Long createOrganisationRequest(User client, String theme) {
        OrganisationRequest request = new OrganisationRequest();
        request.setEmployees(Collections.emptyList());
        request.setClient(client);
        request.setStatus(RequestState.PROTOTYPE);
        request.setPhysical(false);
        request.setOrganisation(client.getOrganisation());
        request.setTheme(theme);
        Chat chat = chatService.createChatForClient(client);
        chatRepo.save(chat);
        request.setChat(chat);
        organisationRequestRepo.save(request);
        chatService.setOrganisationRequest(chat, request);
        return request.getId();
    }

    public List<Human> getAllEmployeesForRequest(OrganisationRequest request) {
        return humanRepo.findAllByOrganisationRequest(request);
    }

    public void addEmployeeToOrganisationRequest(Human human, OrganisationRequest request) {
        List<Human> employees = request.getEmployees();
        employees.add(human);
        organisationRequestRepo.save(request);
    }

    public void deleteEmployeeByID(OrganisationRequest request, Long ID) {
        Human employee = humanRepo.findById(ID).get();
        request.getEmployees().remove(employee);
        organisationRequestRepo.save(request);
    }
}