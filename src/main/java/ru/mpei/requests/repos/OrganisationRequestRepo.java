package ru.mpei.requests.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mpei.requests.domain.requests.OrganisationRequest;
import ru.mpei.requests.domain.requests.RequestState;
import ru.mpei.requests.domain.users.User;

import java.util.List;
import java.util.Optional;

public interface OrganisationRequestRepo extends JpaRepository<OrganisationRequest, Long> {
    Optional<OrganisationRequest> findById(Long id);

    List<OrganisationRequest> findAllByStatus(RequestState requestState);
    List<OrganisationRequest> findAllByExecuterAndClient(User executer, User client);
    List<OrganisationRequest> findAllByExecuter(User executer);
    List<OrganisationRequest> findAllByClient(User client);
}