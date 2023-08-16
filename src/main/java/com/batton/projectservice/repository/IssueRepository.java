package com.batton.projectservice.repository;

import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.enums.IssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    // 이슈 상태에 따라 이슈 Seq 순서대로 정렬 조회
    List<Issue> findByIssueStatusOrderByIssueSeq(IssueStatus status);

    List<Issue> findByProjectIdOrderByIssueSeq(Long projectId);

    List<Issue> findByProjectId(Long projectId);

    // 사용자가 담당한 이슈 리스트를 마지막 수정 날짜 내림차순으로 반환
    List<Issue> findByBelongIdOrderByUpdatedAtDesc(Long belongId);

    // 해당 프로젝트의 이슈가 있는지 확인
    boolean existsByProjectId(Long projectId);

    // 해당 프로젝트의 이슈 중 가장 최신의 이슈 반환
    Issue findTopByProjectIdOrderByCreatedAtDesc(Long projectId);

    // 해당 프로젝트의 이슈 리스트를 마지막 수정 날짜 내림차순으로 변환
    List<Issue> findByProjectIdOrderByUpdatedAtDesc(Long projectId);

    List<Issue> findByIssueStatus(IssueStatus status);

    Optional<Issue> findById(Long id);

    // 특정 키워드 조회
    List<Issue> findByBelongIdAndIssueTitleContaining(Long id, String keyword);

    List<Issue> findByBelongIdAndIssueStatus(Long id, IssueStatus issueStatus);

    // 특정 키워드와 이슈 상태 조회
    List<Issue> findByBelongIdAndIssueStatusAndIssueTitleContaining(Long id, IssueStatus issueStatus, String keyword);

    //해당 프로젝트에서 이슈 상태에 따른 이슈 목록 조회
    List<Issue> findByProjectIdAndIssueStatusOrderByIssueSeq(Long projectId, IssueStatus status);

    // 해당 belongId가 할당된 이슈 목록 조회
    List<Issue> findByBelongId(Long belongId);
}
