package ru.mpei.requests.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mpei.requests.domain.users.Role;
import ru.mpei.requests.domain.users.User;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

    void deleteByUsername(String username);


    List<User> findAllByUsernameContaining(String username);
    List<User> findAllByRoles(Role role);
}