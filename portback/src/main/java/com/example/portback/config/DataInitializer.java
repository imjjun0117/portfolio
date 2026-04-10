package com.example.portback.config;

import com.example.portback.domain.ProblemSolving;
import com.example.portback.domain.Project;
import com.example.portback.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final ProjectRepository projectRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            if (projectRepository.count() == 0) {
                Project p1 = Project.builder()
                        .title("통합 검색 시스템")
                        .description("OpenAPI 기반 검색 기능 구현")
                        .skills(List.of("Spring Boot", "Vue 3", "OpenAPI", "PostgreSQL"))
                        .roles(List.of("데이터 수집 엔진 개발", "검색 API 구현", "프론트엔드 연동"))
                        .problemSolving(ProblemSolving.builder()
                                .problem("대용량 데이터 조회 시 응답 속도 저하 (평균 3초)")
                                .solution("Redis 캐싱 및 쿼리 튜닝, 비동기 처리 도입")
                                .result("응답 시간 0.2초로 단축 (약 90% 개선)")
                                .build())
                        .github("https://github.com/example/search-system")
                        .thumbnail("https://picsum.photos/seed/p1/600/400")
                        .build();

                Project p2 = Project.builder()
                        .title("실시간 협업 도구")
                        .description("WebSocket을 이용한 실시간 문서 편집 플랫폼")
                        .skills(List.of("Java", "WebSocket", "Redis", "TypeScript"))
                        .roles(List.of("소켓 통신 프로토콜 설계", "동시성 제어 로직 구현"))
                        .problemSolving(ProblemSolving.builder()
                                .problem("여러 사용자가 동시에 편집할 때 데이터 충돌 발생")
                                .solution("OT(Operational Transformation) 알고리즘 적용")
                                .result("10인 이상 동시 편집 시에도 데이터 무결성 보장")
                                .build())
                        .github("https://github.com/example/collab-tool")
                        .thumbnail("https://picsum.photos/seed/p2/600/400")
                        .build();

                projectRepository.saveAll(List.of(p1, p2));
            }
        };
    }
}
