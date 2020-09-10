package ru.mpei.requests.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mpei.requests.domain.requests.OrganisationRequest;

public interface OrganisationRequestRepo extends JpaRepository<OrganisationRequest, Long> {
}
