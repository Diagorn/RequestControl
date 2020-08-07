package ru.mpei.requests.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Chat { //Entity that contains the messages for requests or admin chat
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id; //Identifyer

    @ManyToMany
    @JoinTable (
            name = "chat_users",
            joinColumns = { @JoinColumn(name = "chat_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    Set<User> members; //Users involved in the chat

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "chat")
    Set<Message> messages; //Messages written in the chat

    @OneToOne
        @JoinColumn(name = "request_id")
    Request request; //Chat may be devoted to request

    public Chat() {
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}