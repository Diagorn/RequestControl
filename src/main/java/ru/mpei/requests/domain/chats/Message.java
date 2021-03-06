package ru.mpei.requests.domain.chats;

import org.hibernate.validator.constraints.Length;
import ru.mpei.requests.domain.users.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

@Entity //Message in the chat
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id; //Identifyer

    @NotBlank(message = "Пожалуйста, введите текст сообщения")
    @Length(max = 2048, message = "Лимит знаков превышен")
    private String text; //Text of the message

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author; //Account of the author

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @Column(name = "time")
    private GregorianCalendar timeOfSending;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "message")
    private Set<MessageFile> messageFiles;

    public Message() {
        messageFiles = new HashSet<>();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getAuthorName() {
        return author != null ? author.getUsername() : "";
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public GregorianCalendar getTimeOfSending() {
        return timeOfSending;
    }

    public void setTimeOfSending(GregorianCalendar timeOfSending) {
        this.timeOfSending = timeOfSending;
    }

    public Set<MessageFile> getMessageFiles() {
        return messageFiles;
    }

    public void setMessageFiles(Set<MessageFile> messageFiles) {
        this.messageFiles = messageFiles;
    }
}
