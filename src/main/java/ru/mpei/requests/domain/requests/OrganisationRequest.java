package ru.mpei.requests.domain.requests;

import ru.mpei.requests.domain.users.Human;
import ru.mpei.requests.domain.users.Organisation;

import javax.persistence.*;
import java.util.List;

@Entity
public class OrganisationRequest extends Request {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "organisation_requests_humans",
            inverseJoinColumns = @JoinColumn(name = "employee_id"),
            joinColumns = @JoinColumn(name = "request_id")
    )
    private List<Human> employees;

    @ManyToOne
    private Organisation organisation;

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

    @Override
    public RequestState getStatus() {
        return status;
    }

    @Override
    public void setStatus(RequestState status) {
        this.status = status;
    }
}
