package ru.mpei.requests.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mpei.requests.domain.users.Human;

import java.util.List;

public interface HumanRepo extends JpaRepository<Human, Long> {
    Human findByFirstName(String firstName);
    Human findBySecondName(String secondName);
    Human findByLastName(String lastName);
    Human findByFirstNameLike(String firstName);
    Human findBySecondNameLike(String secondName);
    Human findByLastNameLike(String lastName);
    List<Human> findAllByFirstNameContaining(String firstName);
    List<Human> findAllBySecondNameContaining(String secondName);
    List<Human> findAllByLastNameContaining(String lastName);
}
