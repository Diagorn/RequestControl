package ru.mpei.requests.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mpei.requests.domain.chats.Chat;
import ru.mpei.requests.domain.requests.Request;

public interface ChatRepo extends JpaRepository<Chat, Long> {
    Chat findChatByRequest(Request request);
    Chat findByRequestIsNull();
}
