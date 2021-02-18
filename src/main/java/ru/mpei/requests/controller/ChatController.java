package ru.mpei.requests.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.mpei.requests.domain.chats.Chat;
import ru.mpei.requests.domain.chats.Message;
import ru.mpei.requests.domain.requests.OrganisationRequest;
import ru.mpei.requests.domain.requests.PhysicalRequest;
import ru.mpei.requests.domain.users.User;
import ru.mpei.requests.repos.ChatRepo;
import ru.mpei.requests.repos.MessageRepo;
import ru.mpei.requests.service.ChatService;
import ru.mpei.requests.service.RequestService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class ChatController {
    @Autowired
    private MessageRepo messageRepo; //Attaching repo to work with messages

    @Autowired
    private ChatRepo chatRepo; //For doing anything with chats

    @Autowired
    private ChatService chatService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private ru.mpei.requests.service.ServiceUtils serviceUtils;

    @GetMapping("/request/physical/{id}") //Showing the chat page for the particular request
    public String getPhysicalRequestChat (
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            Model model
    ) {
        PhysicalRequest request = requestService.getPhysicalRequestByID(id);
        Chat chat = chatRepo.findChatByPhysicalRequest(request);
        List<Message> messages = messageRepo.findAllByChat(chat);
        model.addAttribute("status", request.getStatus().name());
        model.addAttribute("user", user);
        model.addAttribute("request", request);
        model.addAttribute("messages", messages);
        model.addAttribute("isAdminChat", false);
        model.addAttribute("utils", serviceUtils);
        return "chat";
    }

    @GetMapping("/request/organisation/{id}") //Showing the chat page for the particular request
    public String getOrganisationRequestChat (
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            Model model
    ) {
        OrganisationRequest request = requestService.getOrganisationRequestByID(id);
        Chat chat = chatRepo.findChatByOrganisationRequest(request);
        List<Message> messages = messageRepo.findAllByChat(chat);
        model.addAttribute("status", request.getStatus().name());
        model.addAttribute("user", user);
        model.addAttribute("request", request);
        model.addAttribute("messages", messages);
        model.addAttribute("isAdminChat", false);
        model.addAttribute("utils", serviceUtils);
        return "chat";
    }

    @PostMapping("/request/physical/{id}") //Posting a message
    public String addMessageToPhysicalRequest(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @PathVariable Long id,
            @RequestParam MultipartFile[] files,
            Model model) throws IOException {
        PhysicalRequest request = requestService.getPhysicalRequestByID(id);
        Long messageId = chatService.fillMessage(text, user, request, null, true, false, files);
        model.addAttribute("message", null);
        chatService.addFilesToMessage(messageId, files);
        List<Message> messages = messageRepo.findAllByChat(chatRepo.findChatByPhysicalRequest(request));
        return "redirect:/request/physical/" + id;
    }

    @PostMapping("/request/organisation/{id}") //Posting a message
    public String addMessageToOrganisationRequest(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @PathVariable Long id,
            @RequestParam MultipartFile[] files,
            Model model) throws IOException {
        OrganisationRequest request = requestService.getOrganisationRequestByID(id);
        Long messageId = chatService.fillMessage(text, user, request,
                null, true, false, files);
        chatService.addFilesToMessage(messageId, files);
        model.addAttribute("message", null);
        List<Message> messages = messageRepo.findAllByChat(chatRepo.findChatByOrganisationRequest(request));
        model.addAttribute("request", request);
        model.addAttribute("user", user);
        model.addAttribute("status", request.getStatus().name());
        model.addAttribute("messages", messages);
        model.addAttribute("isAdminChat", false);
        model.addAttribute("utils", serviceUtils);
        return "chat";
    }

    @GetMapping("/chat")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MODER')")
    public String getAdminModerChat (Model model) { //Get-mapping for admins and moders chat
        Chat chat = chatRepo.findByOrganisationRequestIsNullAndPhysicalRequestIsNull();
        List<Message> messages = messageRepo.findAllByChat(chat);
        model.addAttribute("messages", messages);
        model.addAttribute("isAdminChat", true);
        model.addAttribute("utils", serviceUtils);
        return "chat";
    }

    @PostMapping("/chat")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MODER')") //Sending a message to chat
    public String sendMessageToAdminChat(@AuthenticationPrincipal User user,
                                         @RequestParam String text,
                                         @RequestParam MultipartFile[] files,
                                         Model model) throws IOException {
            Long messageId = chatService.fillMessage(text, user, null,
                    null, false, true, files);
            chatService.addFilesToMessage(messageId, files);
            model.addAttribute("message", null);
        List<Message> messages = messageRepo.findAllByChat(chatRepo.findByOrganisationRequestIsNullAndPhysicalRequestIsNull()); //Showing all the messages
        model.addAttribute("messages", messages);
        model.addAttribute("isAdminChat", true);
        model.addAttribute("utils", serviceUtils);
        return "chat";
    }
}