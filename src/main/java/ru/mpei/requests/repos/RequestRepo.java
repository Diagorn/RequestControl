package ru.mpei.requests.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mpei.requests.domain.requests.Request;
import ru.mpei.requests.domain.users.User;

import java.util.List;

public interface RequestRepo extends JpaRepository<Request, Long> {
    Request findRequestById(Long id);
    List<Request> findAllByClient(User client);
    List<Request> findAllByExecuter(User executer);
    List<Request> findAllByExecuterAndClient(User executer, User client);
}