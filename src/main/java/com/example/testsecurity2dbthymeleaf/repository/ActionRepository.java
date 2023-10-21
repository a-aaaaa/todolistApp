package com.example.testsecurity2dbthymeleaf.repository;

import com.example.testsecurity2dbthymeleaf.entity.Action;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionRepository extends JpaRepository<Action, Long> {
}
