package ru.mpei.requests.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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

    @NotBlank(message = "Пожалуйста, введите ваше имя")
    private String firstName; //Name
    private String secondName; //Patronymic
    @NotBlank(message = "Пожалуйста, введите вашу фамилию")
    private String lastName; //Surname

    private String fileName; //Name of the avatar file

    private String activationCode; //For activating the email

    private boolean active; //Active state

    @ElementCollection(fetch = FetchType.EAGER, targetClass = Role.class)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles; //Roles for the particular user

    @OneToMany(mappedBy = "executer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Request> executerRequests;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Request> clientRequests;

    public User(String username, String password, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
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
        if(secondName != null && !secondName.equals(""))
            return lastName + " " + firstName.substring(0, 1) + ". " + secondName.substring(0, 1) + ".";
        else
            return lastName + " " + firstName.substring(0, 1) + ".";
    }

    public boolean isAdmin() {
        return roles.contains(Role.ADMIN);
    }

    public boolean isModer() {return  roles.contains(Role.MODER);}

    public boolean isExecuter() {return  roles.contains(Role.EXECUTER);}

    public boolean isClient() {return roles.contains(Role.CLIENT);}

    public Set<Request> getExecuterRequests() {
        return executerRequests;
    }

    public void setExecuterRequests(Set<Request> executerRequests) {
        this.executerRequests = executerRequests;
    }

    public Set<Request> getClientRequests() {
        return clientRequests;
    }

    public void setClientRequests(Set<Request> clientRequests) {
        this.clientRequests = clientRequests;
    }

    @PreRemove
    private void removeUserFromRequests() {
        for(Request r : executerRequests) {
            r.setExecuter(null);
        }
        for(Request r : clientRequests) {
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
}