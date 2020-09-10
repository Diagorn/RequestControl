package ru.mpei.requests.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mpei.requests.domain.requests.OrganisationRequest;

import java.util.Optional;

public interface OrganisationRequestRepo extends JpaRepository<OrganisationRequest, Long> {
    Optional<OrganisationRequest> findById(Long id);
}