package com.example.portback.repository;

import com.example.portback.domain.VisitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitLogRepository extends JpaRepository<VisitLog, Long> {

    List<VisitLog> findTop100ByOrderByVisitedAtDesc();

    long countByVisitedAtAfter(LocalDateTime since);

    @Query("SELECT COUNT(DISTINCT v.ip) FROM VisitLog v WHERE v.visitedAt > :since")
    long countDistinctIpAfter(LocalDateTime since);

    @Query("SELECT v.path, COUNT(v) FROM VisitLog v GROUP BY v.path ORDER BY COUNT(v) DESC")
    List<Object[]> countByPath();
}
