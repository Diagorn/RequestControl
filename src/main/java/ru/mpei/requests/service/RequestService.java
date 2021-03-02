package ru.mpei.requests.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mpei.requests.domain.chats.Chat;
import ru.mpei.requests.domain.requests.*;
import ru.mpei.requests.domain.users.Human;
import ru.mpei.requests.domain.users.User;
import ru.mpei.requests.repos.*;

import java.util.*;

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
    private ChatRepo chatRepo;

    @Autowired
    private LearningProgramRepo learningProgramRepo;

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

    public boolean canSetExecuter(Long requestID, Long userID, boolean isPhysicalRequest) {
        Request request;
        if (isPhysicalRequest)
            request = getPhysicalRequestByID(requestID);
        else
            request = getOrganisationRequestByID(requestID);
        User user = userRepo.findById(userID).orElse(null);
        return (user != null && request != null);
    }

    public void setExecuter(Long requestID, Long userID, boolean isPhysicalRequest) {
        User user = userRepo.findById(userID).orElse(null);
        Request request;
        if (isPhysicalRequest)
            request = getPhysicalRequestByID(requestID);
        else
            request = getOrganisationRequestByID(requestID);
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

    public TreeSet<Request> sort(String sort, List<Request> requests) {
        TreeSet<Request> sortedSet;
        switch (sort) {
            case "last_message":
                sortedSet = new TreeSet<Request>(new Comparator<Request>() {
                    @Override
                    public int compare(Request request, Request t1) {
                        if (request.getChat().getMessages().isEmpty() || t1.getChat().getMessages().isEmpty())
                            return 0;
                        return ServiceUtils.getLastMessage(request).toString().compareTo(ServiceUtils.getLastMessage(t1).toString());
                    }
                });
                sortedSet.addAll(requests);
                return sortedSet;
            case "id":
                sortedSet = new TreeSet<Request>(new Comparator<Request>() {
                    @Override
                    public int compare(Request request, Request t1) {
                        if (request.isPhysical() && t1.isPhysical()) {
                            PhysicalRequest r1 = (PhysicalRequest)request;
                            PhysicalRequest r2 = (PhysicalRequest)t1;
                            return r1.getId().toString().compareTo(r2.getId().toString());
                        }
                        if (!request.isPhysical() && !t1.isPhysical()) {
                            OrganisationRequest r1 = (OrganisationRequest) request;
                            OrganisationRequest r2 = (OrganisationRequest) t1;
                            return r1.getId().toString().compareTo(r2.getId().toString());
                        }
                        if (!request.isPhysical() && t1.isPhysical()) {
                            OrganisationRequest r1 = (OrganisationRequest) request;
                            PhysicalRequest r2 = (PhysicalRequest)t1;
                            return r1.getId().toString().compareTo(r2.getId().toString());
                        }
                        if (request.isPhysical() && !t1.isPhysical()) {
                            PhysicalRequest r1 = (PhysicalRequest)request;
                            OrganisationRequest r2 = (OrganisationRequest) t1;
                            return r1.getId().toString().compareTo(r2.getId().toString());
                        }
                        return "a".compareTo("b");
                    }
                });
                sortedSet.addAll(requests);
                return sortedSet;
            case "executer":
                sortedSet = new TreeSet<Request>(new Comparator<Request>() {
                    @Override
                    public int compare(Request request, Request t1) {
                        if (request.getExecuter() == null || t1.getExecuter() == null)
                            return request.getTheme().compareTo(t1.getTheme());
                        return request.getExecuter().toString().compareTo(t1.getExecuter().toString());
                    }
                });
                sortedSet.addAll(requests);
                return sortedSet;
            case "client":
                sortedSet = new TreeSet<Request>(new Comparator<Request>() {
                    @Override
                    public int compare(Request request, Request t1) {
                        if (request.getClient() == null || t1.getClient() == null)
                            return request.getTheme().compareTo(t1.getTheme());
                        if (request.getClient().getId().equals(t1.getClient().getId()))
                            return request.getTheme().compareTo(t1.getTheme());
                        return request.getClient().toString().compareTo(t1.getClient().toString());
                    }
                });
                sortedSet.addAll(requests);
                return sortedSet;
            case "theme":
                sortedSet = new TreeSet<Request>(new Comparator<Request>() {
                    @Override
                    public int compare(Request request, Request t1) {
                        return request.getTheme().compareTo(t1.getTheme());
                    }
                });
                sortedSet.addAll(requests);
                return sortedSet;
            default:
                sortedSet = new TreeSet<Request>(new Comparator<Request>() {
                    @Override
                    public int compare(Request request, Request t1) {
                        return request.getChat().getId().compareTo(t1.getChat().getId());
                    }
                });
                sortedSet.addAll(requests);
                return sortedSet;
        }
    }

    public List<LearningProgram> getAllPrograms() {
        return learningProgramRepo.findAll();
    }
}