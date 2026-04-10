# Portfolio Web Service (Full Stack)

현대적인 디자인과 문제 해결 중심의 컨텐츠를 담은 개발자용 포트폴리오 웹 서비스입니다.

## 🚀 프로젝트 개요

- **목적**: 개발자의 역량을 직관적으로 전달하고, 실제 문제 해결 경험을 강조하기 위함
- **구조**: 
  - **Frontend**: Vue 3 + Vite (SPA)
  - **Backend**: Spring Boot + JPA
  - **DB**: PostgreSQL
  - **Infra**: Docker Compose

## ✨ 주요 기능

- **One-Page Scroll**: 부드러운 스크롤 애니메이션과 함께 전체 정보 탐색
- **Dark/Light Mode**: 사용자 선호에 따른 테마 전환 지원
- **Responsive Web**: 모바일, 태블릿, PC 모든 기기 대응
- **Project Detail Modal**: 기술 스택 뿐만 아니라 상세 역할 및 문제 해결(Problem-Solution-Result) 과정 공개
- **REST API**: Spring Boot 백엔드와 연동된 동적 데이터 관리

## 🛠 기술 스택

### Frontend
- Vue 3 (Composition API)
- Vite
- Axios (API 통신)
- Vanilla CSS (Custom Design System)

### Backend
- Java 17
- Spring Boot 3.4.5
- Spring Data JPA
- PostgreSQL

### DevOps
- Docker & Docker Compose
- Maven (Backend Build)

## 🚦 시작하기 (Docker)

프로젝트 루트 디렉토리에서 아래 명령어를 실행하면 모든 환경이 자동으로 구축됩니다.

```bash
docker-compose up --build -d
```

- **Frontend**: http://localhost
- **Backend API**: http://localhost:8080/api/projects

## 📂 폴더 구조

```text
.
├── portback/          # Spring Boot Backend
├── portfront/         # Vue 3 Frontend
├── docker-compose.yml # Docker 설정
├── README.md          # 프로젝트 설명서
└── implement.md       # 설계 정보
```

## 📝 커스터마이징 가이드

1. **프로젝트 데이터**: `portback/src/main/java/com/example/portback/config/DataInitializer.java`에서 실제 프로젝트 정보를 수정하세요.
2. **개인 정보**: `portfront/src/components/Hero.vue` 및 `Contact.vue`의 플레이스홀더 텍스트를 수정하세요.

---
Created by Antigravity (AI Pair Programmer)
