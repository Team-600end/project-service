package com.batton.projectservice.repository;

import com.batton.projectservice.domain.RegisteredIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegisteredIssueRepository extends JpaRepository<RegisteredIssue, Long> {
    Optional<List<RegisteredIssue>> findByReleasesId(Long releaseId);

    void deleteAllByReleasesId(Long releaseId);
}
