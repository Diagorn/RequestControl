package ru.mpei.requests.domain;

import javax.persistence.*;

@Entity
public class Request {
    @Id
    @GeneratedValue
    private Long id; //Identifyer

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_id")
    private Chat chat; //Chat associated with the request

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private User client; //User that has prompted the request

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "executer_id")
    private User executer; //Executer that is handling the request

    private String theme;

    @Enumerated(EnumType.STRING)
    private RequestState status;

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
