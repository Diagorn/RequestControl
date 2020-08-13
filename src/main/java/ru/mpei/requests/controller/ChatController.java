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
import ru.mpei.requests.domain.chats.Message;
import ru.mpei.requests.domain.users.User;
import ru.mpei.requests.repos.ChatRepo;
import ru.mpei.requests.repos.MessageRepo;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class ChatController {
    @Autowired
    private MessageRepo messageRepo; //Attaching repo to work with messages

    @Autowired
    private ChatRepo chatRepo; //For doing anything with chats

//    @Autowired
//    private RequestRepo requestRepo;

    @GetMapping("/request/{id}") //Showing the chat page for the particular request
    public String getRequestChat (
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            Model model
    ) {
//        Request request = requestRepo.findRequestById(id);
        //Chat chat = chatRepo.findChatByRequest(request);
        //List<Message> messages = messageRepo.findAllByChat(chat);
//        model.addAttribute("status", request.getStatus().name());
        model.addAttribute("user", user);
//        model.addAttribute("request", request);
        //model.addAttribute("messages", messages);
        model.addAttribute("isAdminChat", false);
        return "chat";
    }

    @PostMapping("/request/{id}") //Posting a message
    public String addMessage(
            @AuthenticationPrincipal User user,
            @Valid Message message,
            @PathVariable Long id,
            BindingResult bindingResult, //Validation error container
            Model model) {
//        Request request = requestRepo.findRequestById(id);
        if(bindingResult.hasErrors()) { //If we have errors
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
        }
        else {
            message.setAuthor(user);
            //message.setChat(chatRepo.findChatByRequest(request));
            messageRepo.save(message);
            model.addAttribute("message", null);
        }
        //List<Message> messages = messageRepo.findAllByChat(chatRepo.findChatByRequest(request));
//        model.addAttribute("request", request);
        model.addAttribute("user", user);
//        model.addAttribute("status", request.getStatus().name());
        //model.addAttribute("messages", messages);
        model.addAttribute("isAdminChat", false);
        return "chat";
    }

    @GetMapping("/chat")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MODER')")
    public String getAdminModerChat (Model model) { //Get-mapping for admins and moders chat
        //Chat chat = chatRepo.findByRequestIsNull();
        //List<Message> messages = messageRepo.findAllByChat(chat);
        //model.addAttribute("messages", messages);
        model.addAttribute("isAdminChat", true);
        return "chat";
    }

    @PostMapping("/chat")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MODER')") //Sending a message to chat
    public String sendMessageToAdminChat(@AuthenticationPrincipal User user,
                                         @Valid Message message,
                                         BindingResult bindingResult, //Validation error container
                                         Model model) {
        if(bindingResult.hasErrors()) { //If we have errors
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
        }
        else {
            message.setAuthor(user);
            //message.setChat(chatRepo.findByRequestIsNull());
            messageRepo.save(message);
            model.addAttribute("message", null);
        }
        //List<Message> messages = messageRepo.findAllByChat(chatRepo.findByRequestIsNull()); //Showing all the messages
        //model.addAttribute("messages", messages);
        model.addAttribute("isAdminChat", true);
        return "chat";
    }
}