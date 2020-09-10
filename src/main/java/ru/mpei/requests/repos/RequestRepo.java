package ru.mpei.requests.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mpei.requests.domain.requests.Request;
import ru.mpei.requests.domain.requests.RequestState;
import ru.mpei.requests.domain.users.User;

import java.util.List;

public interface RequestRepo extends JpaRepository<Request, Long> {
    List<Request> findAllByExecuterAndClient(User executer, User client);
    List<Request> findAllByExecuter(User executer);
    List<Request> findAllByClient(User client);
    List<Request> findAllByStatus(RequestState status);
}
