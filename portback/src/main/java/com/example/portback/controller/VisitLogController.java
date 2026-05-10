package com.example.portback.controller;

import com.example.portback.repository.VisitLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/visits")
@RequiredArgsConstructor
public class VisitLogController {

    private final VisitLogRepository visitLogRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getVisitStats() {
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        LocalDateTime monthAgo = LocalDateTime.now().minusDays(30);

        List<Object[]> pathCounts = visitLogRepository.countByPath();

        return ResponseEntity.ok(Map.of(
            "todayTotal", visitLogRepository.countByVisitedAtAfter(today),
            "todayUnique", visitLogRepository.countDistinctIpAfter(today),
            "weekTotal", visitLogRepository.countByVisitedAtAfter(weekAgo),
            "monthTotal", visitLogRepository.countByVisitedAtAfter(monthAgo),
            "recentLogs", visitLogRepository.findTop100ByOrderByVisitedAtDesc(),
            "pathStats", pathCounts.stream()
                .map(r -> Map.of("path", r[0], "count", r[1]))
                .toList()
        ));
    }
}
