package ru.mpei.requests.domain.users;

import ru.mpei.requests.domain.requests.OrganisationRequest;
import ru.mpei.requests.domain.requests.PhysicalRequest;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Calendar;

@Entity
public class Human {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Пожалуйста, введите ваше имя")
    private String firstName; //Name

    private String secondName; //Patronymic

    @NotBlank(message = "Пожалуйста, введите вашу фамилию")
    private String lastName; //Surname

    private Calendar DOB; //Date of birth

    private int passport; //Passport series and number

    private String registrationAdress; //Adress of registration

    private String education; //Education

    private String phoneNumber;

    private String email;

    @ManyToOne
    private User user;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PhysicalRequest physicalRequest;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private OrganisationRequest organisationRequest;

    public Human() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Calendar getDOB() {
        return DOB;
    }

    public void setDOB(Calendar DOB) {
        this.DOB = DOB;
    }

    public int getPassport() {
        return passport;
    }

    public void setPassport(int passport) {
        this.passport = passport;
    }

    public String getRegistrationAdress() {
        return registrationAdress;
    }

    public void setRegistrationAdress(String registrationAdress) {
        this.registrationAdress = registrationAdress;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PhysicalRequest getPhysicalRequest() {
        return physicalRequest;
    }

    public void setPhysicalRequest(PhysicalRequest physicalRequest) {
        this.physicalRequest = physicalRequest;
    }

    public OrganisationRequest getOrganisationRequest() {
        return organisationRequest;
    }

    public void setOrganisationRequest(OrganisationRequest organisationRequest) {
        this.organisationRequest = organisationRequest;
    }
}
