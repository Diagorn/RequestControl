package ru.mpei.requests.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mpei.requests.domain.requests.PhysicalRequest;

public interface PhysicalRequestRepo extends JpaRepository<PhysicalRequest, Long> {
}
