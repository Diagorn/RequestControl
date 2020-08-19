package ru.mpei.requests.domain.chats;

import ru.mpei.requests.domain.requests.OrganisationRequest;
import ru.mpei.requests.domain.requests.PhysicalRequest;
import ru.mpei.requests.domain.users.User;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Chat { //Entity that contains the messages for requests or admin chat
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id; //Identifyer

    @ManyToMany
    @JoinTable (
            name = "chat_users",
            joinColumns = { @JoinColumn(name = "chat_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    Set<User> members; //Users involved in the chat

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "chat")
    Set<Message> messages; //Messages written in the chat

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation_request_id")
    OrganisationRequest organisationRequest; //Chat may be devoted to organisation request

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "physical_request_id")
    PhysicalRequest physicalRequest; //Chat may be devoted to physical request

    public Chat() {
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public OrganisationRequest getOrganisationRequest() {
        return organisationRequest;
    }

    public void setOrganisationRequest(OrganisationRequest organisationRequest) {
        this.organisationRequest = organisationRequest;
    }

    public PhysicalRequest getPhysicalRequest() {
        return physicalRequest;
    }

    public void setPhysicalRequest(PhysicalRequest physicalRequest) {
        this.physicalRequest = physicalRequest;
    }
}