package ru.mpei.requests.domain.requests;

import ru.mpei.requests.domain.users.Human;
import ru.mpei.requests.domain.users.Organisation;
import ru.mpei.requests.domain.users.User;

import javax.persistence.*;
import java.util.List;

@Entity
public class OrganisationRequest extends Request {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "request", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Human> employees;

    @ManyToOne
    private Organisation organisation;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User executer;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User client;

    public OrganisationRequest() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
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

    @Override
    public User getExecuter() {
        return executer;
    }

    @Override
    public void setExecuter(User executer) {
        this.executer = executer;
    }

    @Override
    public User getClient() {
        return client;
    }

    @Override
    public void setClient(User client) {
        this.client = client;
    }
}
