package ru.mpei.requests.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mpei.requests.domain.Role;
import ru.mpei.requests.domain.User;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByFirstName(String firstName);
    User findBySecondName(String secondName);
    User findByLastName(String lastName);
    User findByUsernameLike(String username);
    User findByFirstNameLike(String firstName);
    User findBySecondNameLike(String secondName);
    User findByLastNameLike(String lastName);
    void deleteByUsername(String username);

    List<User> findAllByFirstNameContaining(String firstName);
    List<User> findAllBySecondNameContaining(String secondName);
    List<User> findAllByLastNameContaining(String lastName);
    List<User> findAllByUsernameContaining(String username);
    List<User> findAllByRoles(Role role);
}