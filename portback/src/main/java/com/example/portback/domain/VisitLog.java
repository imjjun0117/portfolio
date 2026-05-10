package com.example.portback.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ip;
    private String path;

    @Column(length = 512)
    private String userAgent;

    private String referer;

    @Builder.Default
    private LocalDateTime visitedAt = LocalDateTime.now();
}
