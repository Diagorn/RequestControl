package ru.mpei.requests.domain.requests;

import ru.mpei.requests.domain.chats.Chat;
import ru.mpei.requests.domain.users.User;

import javax.persistence.*;

@Entity
public class PhysicalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User executer;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User client;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_id")
    private Chat chat; //Chat associated with the request

    private String theme;

    @Enumerated(EnumType.STRING)
    protected RequestState status;

    public PhysicalRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getExecuter() {
        return executer;
    }

    public void setExecuter(User executer) {
        this.executer = executer;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
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
