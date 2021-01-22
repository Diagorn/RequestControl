package ru.mpei.requests.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mpei.requests.domain.requests.OrganisationRequest;
import ru.mpei.requests.domain.users.Human;

import java.util.List;

public interface HumanRepo extends JpaRepository<Human, Long> {
    Human findByFirstName(String firstName);
    Human findBySecondName(String secondName);
    Human findByLastName(String lastName);
    Human findByFirstNameContaining(String firstName);
    Human findBySecondNameContaining(String secondName);
    Human findByLastNameContaining(String lastName);
    List<Human> findAllByFirstNameContaining(String firstName);
    List<Human> findAllBySecondNameContaining(String secondName);
    List<Human> findAllByLastNameContaining(String lastName);
    List<Human> findAllByOrganisationRequest(OrganisationRequest organisationRequest);
}