# CLAUDE.md - StudyShare 프로젝트 가이드

## 🎯 Claude의 역할

Claude는 이 프로젝트에서 **조력자이자 비서** 역할을 수행합니다.

### ✅ Claude가 해야 할 일

- 이슈 생성 및 관리
- PR 생성 및 코드 리뷰
- 설계 검토 및 피드백
- 막혔을 때 힌트 제공
- 작업 계획 수립 지원

### ❌ Claude가 하지 않을 일

- **코드 직접 작성 금지**
- 복사-붙여넣기용 코드 제공 금지
- 전체 클래스/메서드 구현 금지

### 💡 예외 상황

아래의 경우에만 코드 예시 제공 가능:
- 문법/사용법 설명을 위한 **3줄 이하 스니펫**
- 오류 수정을 위한 **해당 라인만** 제시
- 설정 파일 (application.yml 등) 기본 템플릿

---

## 🔄 워크플로우

```
┌─────────────────────────────────────────────────────────────┐
│  1. /daily-plan    →  오늘 할 작업 확인                      │
│  2. /create-issue  →  이슈 생성 (체크박스 포함)               │
│  3. 브랜치 생성     →  feature/#이슈번호-설명                 │
│  4. 코딩                                                    │
│  5. git commit     →  /check-task (체크박스 업데이트)        │
│  6. (4-5 반복)                                              │
│  7. /create-pr     →  코드 리뷰 + PR 생성                    │
│  8. 피드백 반영 후 머지                                       │
└─────────────────────────────────────────────────────────────┘
```

## 📌 커맨드 사용법

| 명령어 | 시점 | 설명 |
|--------|------|------|
| `/daily-plan` | 작업 시작 전 | 오늘 할 일 확인, 전체 진행상황 파악 |
| `/create-issue` | 새 기능 시작 | 이슈 생성, 세부 작업 체크박스 포함 |
| `/check-task` | git commit 후 | 완료한 작업 체크박스 업데이트 |
| `/create-pr` | 기능 완료 후 | 코드 리뷰 받고 PR 생성 |
| `/ask` | 막혔을 때 | 힌트 요청 (코드 제공 X) |

---

## 📋 프로젝트 개요

### 프로젝트명
StudyShare - 스터디 일정 공유 플랫폼

### 핵심 기능
- 사용자 관리 (회원가입, 로그인, 프로필)
- 스터디 그룹 (생성, 초대, 멤버 관리)
- 일정 관리 (하루 계획, 주간 일정, 장기 목표)
- 알림 시스템 (RabbitMQ 비동기 처리)

### 기술 스택
- Java 17
- Spring Boot 3.x
- Spring Security + JWT
- Spring Data JPA
- MySQL 8.x
- RabbitMQ
- Gradle

---

## 🏗️ 프로젝트 구조

```
src/main/java/com/studyshare/
├── domain/
│   ├── user/
│   │   ├── entity/
│   │   ├── repository/
│   │   ├── service/
│   │   ├── controller/
│   │   └── dto/
│   ├── group/
│   ├── plan/
│   └── notification/
├── global/
│   ├── config/
│   ├── exception/
│   ├── security/
│   └── common/
└── infra/
    └── rabbitmq/
```

---

## 📊 ERD 요약

### 테이블 목록
- `users` - 사용자
- `study_groups` - 스터디 그룹
- `group_members` - 그룹 멤버 (중간 테이블)
- `daily_plans` - 하루 계획
- `weekly_schedules` - 주간 일정
- `long_term_goals` - 장기 목표
- `notifications` - 알림

### 주요 관계
- users (1) : (N) daily_plans, weekly_schedules, long_term_goals
- users (1) : (N) study_groups (leader)
- users (1) : (N) group_members (N) : (1) study_groups
- study_groups (1) : (N) daily_plans, weekly_schedules, long_term_goals

---

## 📝 개발 컨벤션

### 네이밍
- 클래스: PascalCase (`UserService`)
- 메서드/변수: camelCase (`findByEmail`)
- 상수: UPPER_SNAKE_CASE (`MAX_GROUP_SIZE`)
- 패키지: lowercase (`domain.user`)

### 브랜치 전략
```
main
 └── feature/#이슈번호-간단설명
      예: feature/#3-user-signup
```

### 커밋 메시지
```
feat: 새로운 기능 추가
fix: 버그 수정
refactor: 코드 리팩토링
docs: 문서 수정
test: 테스트 코드
chore: 빌드, 설정 변경
```

### API 설계
- RESTful 원칙 준수
- 버저닝: `/api/v1/...`
- 응답 형식 통일 (ApiResponse 래퍼)

---

## 🚀 개발 마일스톤

### 1단계: 프로젝트 세팅
- [ ] Spring Boot 프로젝트 생성
- [ ] 디렉토리 구조 설정
- [ ] 기본 설정 파일 작성
- [ ] Entity 클래스 작성

### 2단계: 사용자 관리
- [ ] User 엔티티 및 Repository
- [ ] 회원가입 API
- [ ] 로그인 API (JWT)
- [ ] 프로필 조회/수정 API

### 3단계: 스터디 그룹
- [ ] StudyGroup 엔티티 및 Repository
- [ ] 그룹 CRUD API
- [ ] 초대 링크 생성/가입 API
- [ ] 멤버 관리 API

### 4단계: 일정 관리
- [ ] Plan 엔티티들 및 Repository
- [ ] 일정 CRUD API
- [ ] 그룹 피드 조회 API

### 5단계: 알림 시스템
- [ ] RabbitMQ 연동
- [ ] 알림 발행/소비 구현
- [ ] 알림 조회 API

---

## 📝 코드 리뷰 체크리스트

PR 생성 시 Claude가 확인하는 항목:

- [ ] 네이밍 컨벤션 준수 여부
- [ ] 불필요한 코드/주석 존재 여부
- [ ] 예외 처리 적절성
- [ ] N+1 쿼리 가능성
- [ ] 보안 취약점 (SQL Injection, XSS 등)
- [ ] 트랜잭션 범위 적절성
- [ ] DTO/Entity 분리 여부
- [ ] 테스트 코드 존재 여부

---

## 📚 참고 자료

- [Spring Boot 공식 문서](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Security 레퍼런스](https://docs.spring.io/spring-security/reference/)
- [JPA 가이드](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)