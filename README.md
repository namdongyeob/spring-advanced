# 🚀 Spring Advanced 과제

Spring Boot 기반 Todo 관리 API 서버 (Sparta NBC 4기 Spring 심화 코드 개선 과제)


## 🛠️ 기술 스택
- Java 17
- Spring Boot 3.3.3
- Spring Data JPA
- MySQL 8.0
- JWT (jjwt 0.11.5)
- BCrypt 0.10.2
- Lombok
- JUnit 5, Mockito

## ⚙️ 환경 설정

### 1. 데이터베이스 생성
```sql
CREATE DATABASE experts;
```

### 2. application.properties 설정
`src/main/resources/application.properties.example` 파일을 참고하여 `application.properties` 파일을 생성하세요.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/{DB명}
spring.datasource.username={유저명}
spring.datasource.password={비밀번호}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
jwt.secret.key={시크릿키}
```

## 📋 구현 기능

### ✅ 필수 기능

#### Lv 0. 프로젝트 실행 에러 해결
- `application.properties`에 DB, JWT 설정 추가
- `.gitignore` 예외 처리로 설정 파일 깃에 포함

#### Lv 1. ArgumentResolver 등록
- `WebConfig` 생성하여 `AuthUserArgumentResolver`를 Spring MVC에 등록
- `@Auth AuthUser` 어노테이션 정상 동작 처리

#### Lv 2. 코드 개선
- **2-1. Early Return**: `AuthService.signup()`에서 이메일 중복 검사를 `passwordEncoder.encode()` 앞으로 이동
- **2-2. 불필요한 if-else 제거**: `WeatherClient.getTodayWeather()` 가독성 개선
- **2-3. Validation**: `UserChangePasswordRequest`에 `@Pattern` 어노테이션 적용으로 검증 로직을 DTO로 이동

#### Lv 3. N+1 문제 해결
- `TodoRepository`의 JPQL fetch join을 `@EntityGraph` 방식으로 변경

#### Lv 4. 테스트 코드 수정
- `PasswordEncoderTest`: `matches()` 인자 순서 수정
- `ManagerServiceTest`: 메서드명 및 예외 메시지 수정
- `CommentServiceTest`: 예외 타입 `ServerException` → `InvalidRequestException` 수정
- `ManagerService`: `todo.getUser()` null 체크 추가

### 🔥 도전 기능

#### Lv 5. 어드민 API 로깅
- **Interceptor**: `AdminInterceptor`로 어드민 권한 검증 + 접근 시각/URL 로깅
- **AOP**: `AdminAspect`로 요청/응답 데이터를 JSON으로 상세 로깅

## 🌳 브랜치 전략

각 레벨별로 feature 브랜치 생성 → 작업 완료 후 PR로 main에 병합

- `main` — 최종 제출 브랜치
- `feature/lv0-error-fix` — Lv0 작업
- `feature/lv1-argument-resolver` — Lv1 작업
- `feature/lv2-early-return` — Lv2 작업
- `feature/lv3-entity-graph` — Lv3 작업
- `feature/lv4-test-fix` — Lv4 작업
- `feature/lv5-logging` — Lv5 작업
- `feature/readme-til` — README, TIL 작업

## 📝 트러블슈팅

자세한 트러블슈팅 내용은 [TIL 블로그](https://www.notion.so/35cb1fddfba981a7be34f0ebc8518571)

### 주요 이슈
1. **`@Configurable` vs `@Configuration` 혼동** — ArgumentResolver 등록 안 됨
2. **환경변수 매핑 규칙 미숙지** — Spring Boot의 relaxed binding 학습
3. **JPA Transient 에러** — `User.fromAuthUser()`가 영속 상태가 아닌 객체 생성
4. **`@EntityGraph` 메서드명 파싱 충돌** — `findByIdWithUser`에서 `WithUser`를 필드로 인식

## 🧪 테스트
```bash
./gradlew test
```