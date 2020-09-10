package ru.mpei.requests.domain.requests;

import ru.mpei.requests.domain.chats.Chat;
import ru.mpei.requests.domain.users.User;

import javax.persistence.*;

@MappedSuperclass
public class Request {
    @Id
    private Long id;

    public Request() {
    }

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    protected User executer;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    protected User client;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_id")
    protected Chat chat; //Chat associated with the request

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    protected RequestState status;

    protected String theme;

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

    public RequestState getStatus() {
        return status;
    }

    public void setStatus(RequestState status) {
        this.status = status;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
