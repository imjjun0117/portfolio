# 📘 포트폴리오 사이트 설계서 (Docker 포함)

---

# 1. 📌 프로젝트 개요

## ■ 프로젝트명

Portfolio Web Site

## ■ 목적

* 인사담당자가 **3~10초 내 개발자 역량 파악**
* 프로젝트 기반 **문제 해결 능력 강조**
* 실무 중심 포트폴리오 제공

---

# 2. 🧱 시스템 아키텍처

## ■ 전체 구조

```
[Client]
Vue (SPA)
   ↓
[Server]
Spring Boot (REST API)
   ↓
[Data]
JSON or DB (선택)
```

---

## ■ Docker 구성

```
[Docker]
 ├── portfolio-app (Spring Boot)
 ├── portfolio-front (Vue)  ← 선택
 └── nginx (선택)
```

👉 초기에는

* Spring Boot 하나로 정적 + API 같이 운영 가능

---

# 3. 🖥 화면 설계 (One Page)

```
1. Hero Section
2. Skills Section
3. Projects Section (카드형)
4. Project Detail (모달)
5. Contact Section
```

---

# 4. 📂 기능 정의

| 기능         | 설명     |
| ---------- | ------ |
| 프로젝트 목록 조회 | 카드 형태  |
| 프로젝트 상세 보기 | 모달     |
| GitHub 이동  | 외부 링크  |
| 반응형 UI     | 모바일 대응 |

---

# 5. 🎯 UI/UX 설계

## ■ 핵심 전략

* 클릭 최소화
* 스크롤 기반
* 정보 압축

## ■ UI 패턴

* 카드형 리스트
* 모달 상세 보기

---

# 6. 📦 컴포넌트 구조

```
/frontend
 ├── components
 │   ├── Hero.vue
 │   ├── Skills.vue
 │   ├── ProjectCard.vue
 │   ├── ProjectModal.vue
 │   └── Contact.vue
```

---

# 7. 📊 데이터 설계

```json
{
  "projects": [
    {
      "id": 1,
      "title": "통합 검색 시스템",
      "description": "OpenAPI 기반 검색 기능 구현",
      "skills": ["Spring", "Vue", "API"],
      "role": [
        "OpenAPI 연동 구현",
        "검색 쿼리 최적화"
      ],
      "problemSolving": {
        "problem": "API 응답 지연",
        "solution": "비동기 처리 적용",
        "result": "응답 속도 개선"
      },
      "github": "https://github.com/..."
    }
  ]
}
```

---

# 8. 🛠 API 설계

## ■ 프로젝트 목록

```
GET /api/projects
```

## ■ 프로젝트 상세

```
GET /api/projects/{id}
```

---

# 9. 🗂 백엔드 구조 (Spring)

```
/src/main/java
 ├── controller
 │   └── ProjectController.java
 ├── service
 │   └── ProjectService.java
 ├── domain
 │   └── Project.java
 └── repository
```

---

# 10. 🎴 카드 UI 설계

```
[썸네일]
프로젝트명
한줄 설명
[Spring] [Vue] [API]
```

## ■ 기준

* 카드 크기 통일
* 태그 3~4개 제한

---

# 11. 📱 반응형 설계

| 디바이스   | 구조 |
| ------ | -- |
| PC     | 3열 |
| Tablet | 2열 |
| Mobile | 1열 |

---

# 12. 🐳 Docker 설계

## ■ 1. Dockerfile (Spring Boot)

```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
```

---

## ■ 2. docker-compose.yml

```yaml
version: "3.8"

services:
  portfolio:
    build: .
    container_name: portfolio-app
    ports:
      - "8080:8080"
    restart: always
```

---

## ■ 3. 실행 방법

```bash
# build
./gradlew build

# docker 실행
docker-compose up --build -d
```

---

# 13. 🚀 배포 전략

## ■ 단일 컨테이너

* Spring Boot + 정적 파일 포함

## ■ 확장 구조

* Front: Vue → Nginx
* Backend: Spring Boot

---

# 14. 📈 확장 계획

## ■ 1차

* 정적 포트폴리오

## ■ 2차

* 관리자 페이지 (CRUD)

## ■ 3차

* 로그 분석 / 방문자 통계

---

# 15. ⚠️ 주의사항

* 과도한 애니메이션 금지
* 텍스트 과다 금지
* 기술 나열 금지

---

# 16. ✅ 핵심 성공 요소

* 문제 해결 중심
* 빠른 정보 전달
* 직관적인 카드 UI

---

# 🔥 결론

이 프로젝트는 단순 소개 페이지가 아니라
**“채용을 위한 설득형 웹 서비스”로 설계한다.**
