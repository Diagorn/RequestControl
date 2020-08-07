package ru.mpei.requests.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mpei.requests.domain.users.Human;

public interface HumanRepo extends JpaRepository<Human, Long> {
}
