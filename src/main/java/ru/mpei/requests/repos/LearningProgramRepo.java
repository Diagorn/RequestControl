package ru.mpei.requests.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mpei.requests.domain.requests.LearningProgram;

import java.util.List;

public interface LearningProgramRepo extends JpaRepository<LearningProgram, Long> {
    List<LearningProgram> findAll();
}
