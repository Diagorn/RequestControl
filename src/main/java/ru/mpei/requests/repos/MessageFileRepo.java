package ru.mpei.requests.repos;

import org.springframework.data.repository.CrudRepository;
import ru.mpei.requests.domain.chats.MessageFile;

public interface MessageFileRepo extends CrudRepository<MessageFile, Long> {
}
