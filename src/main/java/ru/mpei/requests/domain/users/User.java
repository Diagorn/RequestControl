package ru.mpei.requests.domain.users;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.mpei.requests.domain.requests.OrganisationRequest;
import ru.mpei.requests.domain.requests.PhysicalRequest;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity //Entity for all the users
@Table(name = "usr") //Table name is used to remove conflicts with Postgres
public class User implements UserDetails, Serializable {
    private static final long serialVersionUID = 6855845368479688868L; //А Х У Е Т Ь

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id; //Identifyer

    @NotBlank(message = "Пожалуйста, введите ваш e-mail")
    @Email(message = "Пожалуйста, введите правильный e-mail")
    private String username; //Username which is also an email
    @NotBlank(message = "Пароль не может быть пустым")
    private String password; //Password

    private String fileName; //Name of the avatar file

    private String activationCode; //For activating the email

    private boolean active; //Active state

    private boolean isPhysical; //True if user is not an organisation

    @OneToOne
    private Human person; //If it is a person

    @OneToOne
    private Organisation organisation; //If it is an organisation

    @ElementCollection(fetch = FetchType.EAGER, targetClass = Role.class)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles; //Roles for the particular user

    @OneToMany(mappedBy = "executer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<OrganisationRequest> executerOrganisationRequests;

    @OneToMany(mappedBy = "executer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<PhysicalRequest> executerPhysicalRequests;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<OrganisationRequest> clientOrganisationRequests;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<PhysicalRequest> clientPhysicalRequests;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User() {
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUsernameWithInitials() {
        if (person.getSecondName() != null && !person.getSecondName().equals(""))
            return person.getLastName() + " " + person.getFirstName().substring(0, 1) + ". " +
                    person.getSecondName().substring(0, 1) + ".";
        else {
            return person.getLastName() + " " + person.getFirstName().substring(0, 1) + ".";
        }
    }

    public boolean isAdmin() {
        return roles.contains(Role.ADMIN);
    }

    public boolean isModer() {return  roles.contains(Role.MODER);}

    public boolean isExecuter() {return  roles.contains(Role.EXECUTER);}

    public boolean isClient() {return roles.contains(Role.CLIENT);}

    @PreRemove
    private void removeUserFromRequests() {
        for(PhysicalRequest r : executerPhysicalRequests) {
            r.setExecuter(null);
        }
        for(OrganisationRequest r : executerOrganisationRequests) {
            r.setExecuter(null);
        }
        for(OrganisationRequest r : clientOrganisationRequests) {
            r.setClient(null);
        }
        for(PhysicalRequest r : clientPhysicalRequests) {
            r.setClient(null);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Id.equals(user.Id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id);
    }

    public boolean isPhysical() {
        return isPhysical;
    }

    public void setPhysical(boolean physical) {
        isPhysical = physical;
    }

    public Human getPerson() {
        return person;
    }

    public void setPerson(Human person) {
        this.person = person;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public Set<OrganisationRequest> getExecuterOrganisationRequests() {
        return executerOrganisationRequests;
    }

    public void setExecuterOrganisationRequests(Set<OrganisationRequest> executerOrganisationRequests) {
        this.executerOrganisationRequests = executerOrganisationRequests;
    }

    public Set<PhysicalRequest> getExecuterPhysicalRequests() {
        return executerPhysicalRequests;
    }

    public void setExecuterPhysicalRequests(Set<PhysicalRequest> executerPhysicalRequests) {
        this.executerPhysicalRequests = executerPhysicalRequests;
    }

    public Set<OrganisationRequest> getClientOrganisationRequests() {
        return clientOrganisationRequests;
    }

    public void setClientOrganisationRequests(Set<OrganisationRequest> clientOrganisationRequests) {
        this.clientOrganisationRequests = clientOrganisationRequests;
    }

    public Set<PhysicalRequest> getClientPhysicalRequests() {
        return clientPhysicalRequests;
    }

    public void setClientPhysicalRequests(Set<PhysicalRequest> clientPhysicalRequests) {
        this.clientPhysicalRequests = clientPhysicalRequests;
    }
}