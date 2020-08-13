package ru.mpei.requests.domain.requests;

import ru.mpei.requests.domain.chats.Chat;
import ru.mpei.requests.domain.users.Human;
import ru.mpei.requests.domain.users.Organisation;
import ru.mpei.requests.domain.users.User;

import javax.persistence.*;
import java.util.List;

@Entity
public class OrganisationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "organisationRequest", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Human> employees;

    @ManyToOne
    private Organisation organisation;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User executer;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User client;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_id")
    private Chat chat; //Chat associated with the request

    private String theme;

    @Enumerated(EnumType.STRING)
    private RequestState state;

    public OrganisationRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Human> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Human> employees) {
        this.employees = employees;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
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

    public RequestState getState() {
        return state;
    }

    public void setState(RequestState state) {
        this.state = state;
    }
}
