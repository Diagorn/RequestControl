package ru.mpei.requests.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mpei.requests.domain.users.Human;
import ru.mpei.requests.domain.users.Organisation;

import java.util.List;
import java.util.Optional;

public interface OrganisationRepo extends JpaRepository<Organisation, Long> {
    Optional<Organisation> findById(Long Id);
}
