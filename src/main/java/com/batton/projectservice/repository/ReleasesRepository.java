package com.batton.projectservice.repository;

import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.domain.Releases;
import com.batton.projectservice.enums.PublishState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.ListResourceBundle;
import java.util.Optional;

@Repository
public interface ReleasesRepository extends JpaRepository<Releases, Long> {
    Optional<Releases> findById(Long releaseId);
    Optional<List<Releases>> findByProjectIdOrderByCreatedAtAsc(Long projectId);

//    @Query("SELECT r FROM Releases r WHERE r.project.id = :projectId ORDER BY r.updatedAt DESC, r.id DESC")
//    List<Releases> findByProjectIdOrderByUpdatedAtDescIdDesc(Long projectId);

    Optional<Releases> findFirstByProjectIdOrderByUpdatedAtDesc(Long projectId);

    List<Releases> findByPublishStateOrderByUpdatedAtDesc(PublishState state);
    List<Releases> findByPublishStateAndProjectIdOrderByUpdatedAtDesc(PublishState state, Long projectId);
    List<Releases> findByReleaseContentContainingOrderByUpdatedAtDesc(String keyword);
    List<Releases> findByProjectIdAndReleaseContentContainingOrderByUpdatedAtDesc(Long projectId, String keyword);
}
