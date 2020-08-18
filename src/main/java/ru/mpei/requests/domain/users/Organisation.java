package ru.mpei.requests.domain.users;

import ru.mpei.requests.domain.requests.OrganisationRequest;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Organisation implements Serializable {

    private static final long serialVersionUID = -9003779202226434808L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User user;

    @OneToOne
    private Human director;

    private String name;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Human> employees; //If it is not a person but an organisation

    @OneToMany
    private List<OrganisationRequest> organisationRequests;

    private String legalAdress; //Yuridicheskiy adres

    private String physicalAdress; //Fisicheskiy adres

    private String phoneNumber; //Phone number

    public Organisation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Human getDirector() {
        return director;
    }

    public void setDirector(Human director) {
        this.director = director;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Human> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Human> employees) {
        this.employees = employees;
    }

    public List<OrganisationRequest> getOrganisationRequests() {
        return organisationRequests;
    }

    public void setOrganisationRequests(List<OrganisationRequest> organisationRequests) {
        this.organisationRequests = organisationRequests;
    }

    public String getLegalAdress() {
        return legalAdress;
    }

    public void setLegalAdress(String legalAdress) {
        this.legalAdress = legalAdress;
    }

    public String getPhysicalAdress() {
        return physicalAdress;
    }

    public void setPhysicalAdress(String physicalAdress) {
        this.physicalAdress = physicalAdress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
