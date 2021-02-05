package ru.mpei.requests.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mpei.requests.domain.chats.Chat;
import ru.mpei.requests.domain.chats.Message;
import ru.mpei.requests.domain.requests.OrganisationRequest;
import ru.mpei.requests.domain.requests.PhysicalRequest;
import ru.mpei.requests.domain.requests.Request;
import ru.mpei.requests.domain.users.User;
import ru.mpei.requests.repos.ChatRepo;
import ru.mpei.requests.repos.MessageRepo;

import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

@Service
public class ChatService {
    @Autowired
    private ChatRepo chatRepo; //For doing anything with chats

    @Autowired
    private MessageRepo messageRepo;

    public Chat createChatForClient(User client) {
        Chat chat = new Chat();
        Set<User> members = new HashSet<>();
        members.add(client);
        chat.setMembers(members);
        return chat;
    }

    public void setPhysicalRequest(Chat chat, PhysicalRequest request) {
        chat.setPhysicalRequest(request);
        chatRepo.save(chat);
    }

    public void setOrganisationRequest(Chat chat, OrganisationRequest request) {
        chat.setOrganisationRequest(request);
        chatRepo.save(chat);
    }

    public Chat getChatById(Long id) {
        return chatRepo.findById(id).get();
    }

    public void fillMessage(Message message, User author, Request request,
                            Chat chat, boolean isPhysical, boolean isAdminChat) {
        message.setAuthor(author);
        if (!isAdminChat) {
            if (isPhysical)
                message.setChat(chatRepo.findChatByPhysicalRequest((PhysicalRequest) request));
            else
                message.setChat(chatRepo.findChatByOrganisationRequest((OrganisationRequest) request));
        } else {
            message.setChat(getChatById(0L));
        }
        message.setTimeOfSending(new GregorianCalendar());
        messageRepo.save(message);
    }
}