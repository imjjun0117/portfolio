package com.example.portback.config;

import com.example.portback.domain.*;
import com.example.portback.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProjectRepository projectRepository;
    private final ExperienceRepository experienceRepository;
    private final SkillGroupRepository skillGroupRepository;
    private final SiteConfigRepository siteConfigRepository;

    @Override
    public void run(String... args) throws Exception {
        seedProjects();
        seedExperiences();
        seedSkillGroups();
        seedSiteConfig();
    }

    private void seedProjects() {
        if (projectRepository.count() > 0) return;

        Project project1 = Project.builder()
                .title("스마트 팩토리 통합 관제 시스템")
                .description("IoT 센서 데이터를 실시간으로 수집하여 공정 효율을 분석하고 가동 중단을 예측하는 대시보드 시스템입니다.")
                .period("2023.08 ~ 2023.12 (5개월)")
                .longDescription("전국 단위 제조업 공장의 스마트화를 위한 통합 관제 플랫폼입니다. 수만 개의 센서로부터 초당 발생하는 시계열 데이터를 실시간 처리하여 공정 분석 알고리즘에 대입합니다. 가동 중단 시 막대한 비용이 발생하는 공정의 가동률을 예측하여 예방 정비를 지원하는 핵심 시스템으로 개발되었습니다.")
                .skills(Arrays.asList("Java", "Spring Boot", "InfluxDB", "React", "WebSocket"))
                .roles(Arrays.asList("백엔드 API 설계 및 구현", "실시간 데이터 스트리밍 처리", "공정 분석 알고리즘 최적화"))
                .github("https://github.com/example/smart-factory")
                .thumbnail("https://images.unsplash.com/photo-1581091226825-a6a2a5aee158?auto=format&fit=crop&w=800&q=80")
                .images(Arrays.asList(
                        "https://images.unsplash.com/photo-1581091226825-a6a2a5aee158?auto=format&fit=crop&w=1200",
                        "https://images.unsplash.com/photo-1558494949-ef010cbdcc48?auto=format&fit=crop&w=1200",
                        "https://images.unsplash.com/photo-1518770660439-4636190af475?auto=format&fit=crop&w=1200"
                ))
                .problemSolving(ProblemSolving.builder()
                        .problem("수만 개의 센서에서 초당 발생하는 데이터를 처리할 때 DB 병목 현상 발생")
                        .solution("InfluxDB(시계열 DB)와 Redis 캐싱 레이어 도입으로 저장 및 조회 속도 개선")
                        .result("전체 시스템 응답 속도 40% 향상 및 대역폭 효율화 성공")
                        .build())
                .build();

        Project project2 = Project.builder()
                .title("차세대 인사/급여 자동화 플랫폼")
                .description("복잡한 인사 관리 및 급여 계산 로직을 자동화하고 연말정산 프로세스를 지원하는 기업용 SaaS입니다.")
                .period("2024.01 ~ 2024.04 (4개월)")
                .longDescription("대규모 기업의 인사 행정 및 급여 정산 업무를 디지털로 전환하는 프로젝트입니다. 수동으로 처리되던 복잡한 급여 산정 로직을 자동화하고, 개정된 세법을 즉시 반영할 수 있는 구조로 리팩토링하였습니다. 특히 연말정산 처리 효율을 극대화하기 위해 배치 엔진을 최적화한 실무용 SaaS 플랫폼입니다.")
                .skills(Arrays.asList("Java", "Spring Boot", "PostgreSQL", "Vue.js", "Docker"))
                .roles(Arrays.asList("인사 관리 모듈 리팩토링", "급여 정산 비즈니스 로직 최적화", "도커 기반 배포 파이프라인 구축"))
                .github(null)
                .thumbnail("https://images.unsplash.com/photo-1460925895917-afdab827c52f?auto=format&fit=crop&w=800&q=80")
                .images(Arrays.asList(
                        "https://images.unsplash.com/photo-1460925895917-afdab827c52f?auto=format&fit=crop&w=1200",
                        "https://images.unsplash.com/photo-1454165833267-028ec280650d?auto=format&fit=crop&w=1200",
                        "https://images.unsplash.com/photo-1554224155-8d04cb21cd6c?auto=format&fit=crop&w=1200"
                ))
                .problemSolving(ProblemSolving.builder()
                        .problem("기존 레거시 시스템의 급여 계산 로직이 복잡하여 정산 시간이 10분 이상 소요됨")
                        .solution("함수형 프로그래밍과 병렬 스트림 처리를 도입하여 정산 로직 최적화")
                        .result("정산 시간 10분에서 30초 내외로 단축 (약 95% 개선)")
                        .build())
                .build();

        Project project3 = Project.builder()
                .title("AI 기반 이커머스 매출 분석 대시보드")
                .description("판매 데이터를 기반으로 미래 매출을 예측하고 카테고리별 추천 상품을 제안하는 분석 솔루션입니다.")
                .period("2023.05 ~ 2023.07 (3개월)")
                .longDescription("이커머스 운영자가 데이터를 기반으로 의사결정을 내릴 수 있도록 돕는 인공지능 분석 도구입니다. 과거 매출 패턴을 시계열 분석 모델로 학습시켜 다음 달 매출액을 예측하고, 고객 구매 패턴을 분석하여 마케팅 집중 카테고리를 추천합니다. 모든 분석 결과는 고해상도 차트로 시각화되어 제공됩니다.")
                .skills(Arrays.asList("Python", "FastAPI", "TensorFlow", "Vue.js", "MySQL"))
                .roles(Arrays.asList("데이터 전처리 파이프라인 구축", "예측 모델 결과 시각화 모듈 개발", "REST API 통합"))
                .github("https://github.com/example/sales-ai")
                .thumbnail("https://images.unsplash.com/photo-1551288049-bbbda536339a?auto=format&fit=crop&w=800&q=80")
                .images(Arrays.asList(
                        "https://images.unsplash.com/photo-1551288049-bbbda536339a?auto=format&fit=crop&w=1200",
                        "https://images.unsplash.com/photo-1460925895917-afdab827c52f?auto=format&fit=crop&w=1200",
                        "https://images.unsplash.com/photo-1504868584819-f8eec4b6d730?auto=format&fit=crop&w=1200"
                ))
                .problemSolving(ProblemSolving.builder()
                        .problem("머신러닝 예측 모델의 추론 시간이 길어 실시간 시각화가 어려움")
                        .solution("모델 경량화 및 비동기 작업 큐(Celery)를 활용한 작업 분산")
                        .result("사용자 체감 대기 시간 최소화 및 안정적인 차트 렌더링 구현")
                        .build())
                .build();

        projectRepository.saveAll(Arrays.asList(project1, project2, project3));
    }

    private void seedExperiences() {
        if (experienceRepository.count() > 0) return;

        Experience exp1 = Experience.builder()
                .company("(주)디지털서커스")
                .period("2024.05 ~ 현재")
                .duration("약 2년")
                .role("개발팀 대리")
                .type("SI 개발")
                .description("Java/Spring Boot 기반 공공 및 기업용 SI 시스템 개발. 요구사항 분석부터 설계, 개발, 납품까지 전 과정 참여.")
                .tags(Arrays.asList("Java", "Spring Boot", "Oracle", "MyBatis"))
                .current(true)
                .displayOrder(1)
                .build();

        Experience exp2 = Experience.builder()
                .company("주식회사 큐브에이")
                .period("2022.08 ~ 2023.09")
                .duration("1년 2개월")
                .role("개발팀 사원")
                .type("웹 개발")
                .description("Spring Framework, MyBatis 기반 웹 서비스 신규 개발 및 리뉴얼. Java Model1, JDBC 기반 레거시 시스템 운영 유지보수.")
                .tags(Arrays.asList("Spring Framework", "MyBatis", "Java", "JDBC"))
                .current(false)
                .displayOrder(2)
                .build();

        experienceRepository.saveAll(Arrays.asList(exp1, exp2));
    }

    private void seedSkillGroups() {
        if (skillGroupRepository.count() > 0) return;

        SkillGroup backend = SkillGroup.builder()
                .name("Backend")
                .icon("⚙️")
                .color("#3b82f6")
                .skills(Arrays.asList("Java", "Spring Boot", "Spring Framework", "JPA / MyBatis", "REST API"))
                .displayOrder(1)
                .build();

        SkillGroup frontend = SkillGroup.builder()
                .name("Frontend")
                .icon("🎨")
                .color("#8b5cf6")
                .skills(Arrays.asList("Vue.js", "JavaScript", "HTML5 / CSS3", "Vite", "Axios"))
                .displayOrder(2)
                .build();

        SkillGroup database = SkillGroup.builder()
                .name("Database")
                .icon("🗄️")
                .color("#10b981")
                .skills(Arrays.asList("PostgreSQL", "Oracle", "MySQL", "JDBC", "Redis"))
                .displayOrder(3)
                .build();

        SkillGroup devops = SkillGroup.builder()
                .name("DevOps & Tools")
                .icon("🛠️")
                .color("#f59e0b")
                .skills(Arrays.asList("Docker", "Git / GitHub", "Maven", "Linux", "AWS"))
                .displayOrder(4)
                .build();

        skillGroupRepository.saveAll(Arrays.asList(backend, frontend, database, devops));
    }

    private void seedSiteConfig() {
        if (siteConfigRepository.count() > 0) return;

        List<SiteConfig> configs = Arrays.asList(
                new SiteConfig("hero.badge", "풀스택 개발자"),
                new SiteConfig("hero.name", "황성준"),
                new SiteConfig("hero.description", "웹 에이전시부터 SI까지, 다양한 도메인의 실무 프로젝트를 경험한\n3년차 풀스택 개발자입니다."),
                new SiteConfig("hero.stat1.value", "3+"),
                new SiteConfig("hero.stat1.label", "년 경력"),
                new SiteConfig("hero.stat2.value", "2"),
                new SiteConfig("hero.stat2.label", "개 기업"),
                new SiteConfig("hero.stat3.value", "Full"),
                new SiteConfig("hero.stat3.label", "Stack"),
                new SiteConfig("hero.githubUrl", "https://github.com/imjjun0117"),
                new SiteConfig("hero.email", "hsungjun0117@gmail.com"),
                new SiteConfig("contact.email", "hsungjun0117@gmail.com"),
                new SiteConfig("contact.github", "https://github.com/imjjun0117")
        );

        siteConfigRepository.saveAll(configs);
    }
}
