package ru.mpei.requests.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mpei.requests.domain.requests.PhysicalRequest;
import ru.mpei.requests.domain.requests.RequestState;
import ru.mpei.requests.domain.users.User;

import java.util.List;

public interface PhysicalRequestRepo extends JpaRepository<PhysicalRequest, Long> {
    List<PhysicalRequest> findAllByStatus(RequestState requestState);
    List<PhysicalRequest> findAllByExecuterAndClient(User executer, User client);
    List<PhysicalRequest> findAllByExecuter(User executer);
    List<PhysicalRequest> findAllByClient(User client);
}
