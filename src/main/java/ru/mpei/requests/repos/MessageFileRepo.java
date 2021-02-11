package ru.mpei.requests.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mpei.requests.domain.chats.MessageFile;

public interface MessageFileRepo extends JpaRepository<MessageFile, Long> {
}
