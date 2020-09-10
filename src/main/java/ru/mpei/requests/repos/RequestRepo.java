package ru.mpei.requests.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mpei.requests.domain.requests.Request;

public interface RequestRepo extends JpaRepository<Request, Long> {

}
