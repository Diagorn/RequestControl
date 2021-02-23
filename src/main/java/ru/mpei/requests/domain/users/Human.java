package ru.mpei.requests.domain.users;

import ru.mpei.requests.domain.requests.OrganisationRequest;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Calendar;

@Entity
public class Human implements Serializable {
    private static final long serialVersionUID = 2991582847736919397L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Пожалуйста, введите ваше имя")
    private String firstName; //Name

    private String secondName; //Patronymic

    @NotBlank(message = "Пожалуйста, введите вашу фамилию")
    private String lastName; //Surname

    private Calendar DOB; //Date of birth

    private String passport; //Passport series and number

    private String passportGivingOrgan;

    private Calendar passportDate;

    private String registrationAdress; //Adress of registration

    private String index;

    private String education; //Education

    private String phoneNumber;

    private String email;

    private String position; //Doljnost'

    private boolean sex;

    private String citizenship;

    private String groupName;

    private String speciality;

    @OneToOne(mappedBy = "director")
    private Organisation organisation;

    @OneToOne(fetch = FetchType.EAGER)
    private User user;

    @ManyToOne
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

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public OrganisationRequest getOrganisationRequest() {
        return organisationRequest;
    }

    public void setOrganisationRequest(OrganisationRequest organisationRequest) {
        this.organisationRequest = organisationRequest;
    }

    public String getPassportGivingOrgan() {
        return passportGivingOrgan;
    }

    public void setPassportGivingOrgan(String passportGivingOrgan) {
        this.passportGivingOrgan = passportGivingOrgan;
    }

    public Calendar getPassportDate() {
        return passportDate;
    }

    public void setPassportDate(Calendar passportDate) {
        this.passportDate = passportDate;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public boolean getSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public String getGroup() {
        return groupName;
    }

    public void setGroup(String group) {
        this.groupName = group;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
}
