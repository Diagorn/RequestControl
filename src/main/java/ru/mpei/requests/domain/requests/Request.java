package ru.mpei.requests.domain.requests;

import ru.mpei.requests.domain.chats.Chat;
import ru.mpei.requests.domain.users.User;

import javax.persistence.*;

public class Request {
    private Long id; //Identifyer

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_id")
    protected Chat chat; //Chat associated with the request

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    protected User client; //User that has prompted the request

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "executer_id")
    protected User executer; //Executer that is handling the request

    protected String theme;

    @Enumerated(EnumType.STRING)
    protected RequestState status;

    public Request() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public User getExecuter() {
        return executer;
    }

    public void setExecuter(User executer) {
        this.executer = executer;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public RequestState getStatus() {
        return status;
    }

    public void setStatus(RequestState status) {
        this.status = status;
    }
}
