package ru.mpei.requests.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mpei.requests.repos.ChatRepo;

@Service
public class ChatService {
    @Autowired
    private ChatRepo chatRepo; //For doing anything with chats

//    public Chat createChatForClient(User client, Request request) { //Creating the chat after the client creating the request
//        Chat chat = new Chat();
//        chat.setMembers(Collections.singleton(client)); //We have only one member until executer is not set
//        //chat.setRequest(request);
//        chatRepo.save(chat);
//        return chat;
//    }
}