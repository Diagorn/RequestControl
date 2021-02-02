package ru.mpei.requests.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mpei.requests.domain.chats.Chat;
import ru.mpei.requests.domain.requests.OrganisationRequest;
import ru.mpei.requests.domain.requests.PhysicalRequest;

public interface ChatRepo extends JpaRepository<Chat, Long> {
//    Chat findChatByRequest(Request request);
    Chat findByOrganisationRequestIsNullAndPhysicalRequestIsNull();

    Chat findChatByPhysicalRequest(PhysicalRequest request);

    Chat findChatByOrganisationRequest(OrganisationRequest request);
}
