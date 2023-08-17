package com.batton.projectservice.repository;

import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.enums.GradeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BelongRepository extends JpaRepository<Belong, Long> {
    Optional<Belong> findByProjectIdAndMemberId(Long projectId, Long memberId);

    List<Belong> findBelongByProjectId(Long projectId);

    Optional<Belong> findById(Long belongId);

    List<Belong> findByMemberId(Long memberId);

    List<Belong> findByProjectId(Long projectId);

    @Query("SELECT b FROM Belong b WHERE b.project.id = :projectId AND b.grade = :type")
    List<Belong> findLeader(@Param("projectId") Long projectId, @Param("type") GradeType type);
}
