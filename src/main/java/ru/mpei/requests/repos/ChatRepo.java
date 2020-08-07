package ru.mpei.requests.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mpei.requests.domain.Chat;
import ru.mpei.requests.domain.Request;

public interface ChatRepo extends JpaRepository<Chat, Long> {
    Chat findChatByRequest(Request request);
    Chat findByRequestIsNull();
}
