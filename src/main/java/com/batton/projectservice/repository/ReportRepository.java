package com.batton.projectservice.repository;

import com.batton.projectservice.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findByIssueId(Long issueId);

    Optional<Report> findById(Long reportId);
}
