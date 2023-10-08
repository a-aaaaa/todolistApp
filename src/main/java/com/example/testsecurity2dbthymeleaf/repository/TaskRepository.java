package com.example.testsecurity2dbthymeleaf.repository;

import com.example.testsecurity2dbthymeleaf.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
