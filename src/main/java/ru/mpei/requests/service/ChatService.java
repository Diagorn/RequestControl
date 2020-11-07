package ru.mpei.requests.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mpei.requests.domain.chats.Chat;
import ru.mpei.requests.domain.requests.OrganisationRequest;
import ru.mpei.requests.domain.requests.PhysicalRequest;
import ru.mpei.requests.domain.requests.Request;
import ru.mpei.requests.domain.users.User;
import ru.mpei.requests.repos.ChatRepo;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class ChatService {
    @Autowired
    private ChatRepo chatRepo; //For doing anything with chats

    public Chat createChatForClient(User client) {
        Chat chat = new Chat();
        Set<User> members = new HashSet<User>();
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
}