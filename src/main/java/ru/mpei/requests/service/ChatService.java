package ru.mpei.requests.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.mpei.requests.domain.chats.Chat;
import ru.mpei.requests.domain.chats.Message;
import ru.mpei.requests.domain.chats.MessageFile;
import ru.mpei.requests.domain.requests.OrganisationRequest;
import ru.mpei.requests.domain.requests.PhysicalRequest;
import ru.mpei.requests.domain.requests.Request;
import ru.mpei.requests.domain.users.User;
import ru.mpei.requests.repos.ChatRepo;
import ru.mpei.requests.repos.MessageFileRepo;
import ru.mpei.requests.repos.MessageRepo;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class ChatService {
    @Autowired
    private ChatRepo chatRepo; //For doing anything with chats

    @Autowired
    private MessageFileRepo messageFileRepo;

    @Autowired
    private MessageRepo messageRepo;

    @Value("${upload.path}")
    private String uploadPath;

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

    public Message getMessageById(Long id) {
        return messageRepo.findById(id).get();
    }

    public Long fillMessage(String text, User author, Request request,
                            Chat chat, boolean isPhysical, boolean isAdminChat, MultipartFile[] files) throws IOException {
        Message message = new Message();
        message.setText(text);
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
        return message.getId();
    }

    public void addFilesToMessage(Long id, MultipartFile[] files) throws IOException {
        Message message = getMessageById(id);
        File uploadDir = new File(uploadPath + File.separator + "files" + File.separator);
        if(!uploadDir.exists()) { //If the file directory does not exist
            uploadDir.mkdir(); //We make it
        }
        for (MultipartFile file: files) {
            String resultFileName = "";

            if(file != null && !file.getOriginalFilename().isEmpty()) {
                String uuidFile = UUID.randomUUID().toString(); //Unique ID to prevent collisions
                resultFileName = uuidFile + "." + file.getOriginalFilename();
                file.transferTo(new File(uploadPath + File.separator + "files" + File.separator + resultFileName));
                MessageFile messageFile = new MessageFile();
                messageFile.setOriginalName(file.getOriginalFilename());
                messageFile.setNewFileName(resultFileName);
                messageFile.setMessage(message);
                messageFileRepo.save(messageFile);
            }
        }
    }
}