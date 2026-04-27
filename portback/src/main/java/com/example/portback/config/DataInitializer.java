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

        Project portfolio = Project.builder()
                .title("개인 포트폴리오 웹사이트")
                .description("Spring Boot + Vue.js + Docker 풀스택으로 직접 설계·개발한 포트폴리오 사이트입니다.")
                .period("2026.04 ~ 현재")
                .longDescription("단순한 정적 페이지 대신, 실제 운영 가능한 풀스택 구조로 직접 설계·개발한 포트폴리오입니다.\n\n" +
                        "Spring Boot 백엔드와 Vue.js 프론트엔드를 Docker Compose로 오케스트레이션하고, Nginx를 리버스 프록시로 두어 SPA 라우팅과 API 프록시를 처리합니다. " +
                        "관리자 페이지에서 JWT 인증을 통해 프로젝트·경력·기술스택·사이트 설정을 모두 관리할 수 있으며, 이미지 파일은 Docker 볼륨에 저장됩니다.\n\n" +
                        "콘텐츠를 코드 수정 없이 대시보드에서 직접 편집할 수 있도록 SiteConfig 엔티티를 key-value 구조로 설계하여 배포 후에도 유연하게 운영할 수 있습니다.")
                .skills(Arrays.asList("Java", "Spring Boot", "Vue.js", "PostgreSQL", "Docker", "Nginx", "JWT"))
                .roles(Arrays.asList(
                        "Spring Boot REST API 설계 및 구현",
                        "Vue.js SPA 프론트엔드 개발",
                        "JWT 기반 관리자 인증 구현",
                        "Docker Compose 멀티 서비스 인프라 구성",
                        "관리자 대시보드 (CRUD + 파일 업로드) 개발"
                ))
                .github("https://github.com/imjjun0117/portfolio")
                .thumbnail("")
                .images(Arrays.asList())
                .vibeCoding(true)
                .problemSolving(ProblemSolving.builder()
                        .problem("콘텐츠 수정 때마다 코드 배포가 필요한 정적 포트폴리오 구조")
                        .solution("SiteConfig key-value 엔티티로 DB 기반 콘텐츠 관리 + 관리자 대시보드 구현")
                        .result("배포 없이 브라우저에서 모든 콘텐츠를 실시간으로 수정·반영 가능")
                        .build())
                .build();

        projectRepository.save(portfolio);
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
