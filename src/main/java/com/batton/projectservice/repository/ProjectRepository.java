package com.batton.projectservice.repository;

import com.batton.projectservice.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    boolean existsByProjectKey(String projectKey);

    List<Project> findByProjectTitleContaining(String keyword);
}
