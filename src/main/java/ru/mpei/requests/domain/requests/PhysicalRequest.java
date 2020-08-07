package ru.mpei.requests.domain.requests;

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
}
