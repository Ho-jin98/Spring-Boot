# Spring Advanced 과제

## 📌 프로젝트 소개

이미 완성된 Spring Boot 프로젝트를 분석하여 에러를 수정하고, 동작하지 않는 메서드를 개선하며, 코드 품질을 향상시키는 과제입니다.
도전 과제로는 어드민 API 접근 로깅을 위한 Interceptor를 구현했습니다.

---

## ✅ 필수 기능 구현

### Lv 0. 프로젝트 세팅 - 에러 분석

**문제 상황**
프로젝트 실행 시 애플리케이션이 실행되지 않는 에러 발생

**해결 방법**
- `application.yml` 파일 추가
- `JwtUtil`에 `jwt.secret.key` 값 주입

---

### Lv 1. ArgumentResolver

**문제 상황**
`AuthUserArgumentResolver`의 로직이 동작하지 않는 상태

**해결 방법**
- `@Component`로 Bean 등록 후 `WebMvcConfig`에서 Spring MVC와 연결
```java
@Component
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {
    ...
}
```

```java
@Configuration
@RequiredArgsConstructor

public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthUserArgumentResolver authUserArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authUserArgumentResolver);
    }
    ...
}
```

---

### Lv 2. 코드 개선

#### 1. Early Return 적용

**문제 상황**
`AuthService`의 `signup()` 메서드에서 이메일 중복 여부와 관계없이 `passwordEncoder.encode()`가 항상 실행됨

**해결 방법**
이메일 중복 체크를 `passwordEncoder.encode()` 호출 전으로 이동하여 불필요한 연산 방지

```java
// Before
String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
if (userRepository.existsByEmail(signupRequest.getEmail())) {
    throw new InvalidRequestException("이미 존재하는 이메일입니다.");
}

// After
if (userRepository.existsByEmail(signupRequest.getEmail())) {
    throw new InvalidRequestException("이미 존재하는 이메일입니다.");
}
String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
```

---

#### 2. 불필요한 if-else 제거

**문제 상황**
`WeatherClient`의 `getTodayWeather()`에서 `if-else` 중첩 구조로 가독성이 떨어짐

**해결 방법**
`throw`는 메서드를 즉시 종료시키므로 `else` 블록이 불필요함 → Early Return 패턴 적용

```java
// Before
if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
    throw new ServerException("...");
} else {
    if (weatherArray == null || weatherArray.length == 0) {
        throw new ServerException("...");
    }
}

// After
if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
    throw new ServerException("...");
}
if (weatherArray == null || weatherArray.length == 0) {
    throw new ServerException("...");
}
```

---

#### 3. Validation - DTO에서 유효성 검사 처리

**문제 상황**
`UserService`의 `changePassword()`에서 비밀번호 유효성 검사 로직이 서비스 레이어에 존재

**해결 방법**
유효성 검사를 `UserChangePasswordRequest` DTO로 이동하여 관심사 분리

```java
@NotBlank
private String oldPassword;

@NotBlank
@Length(min = 8, message = "새 비밀번호는 8자 이상입니다.")
@Pattern(regexp = "^(?=.*\\d)(?=.*[A-Z]).+$"
        ,message = "숫자와 대문자를 포함해야 합니다.")
private String newPassword;
```

---

### Lv 3. N+1 문제 해결

**문제 상황**
`getTodos()` 메서드에서 Todo 목록 조회 시 각 Todo의 User를 개별 쿼리로 조회하는 N+1 문제 발생

**N+1 문제란?**
- 엔티티 1개를 조회했을 때 연관된 데이터를 가져오기 위해 추가 쿼리가 N개 더 실행되는 현상
- Todo가 100개면 User 조회 쿼리가 100번 추가 실행 → 총 101번 쿼리 실행

**해결 방법**
JPQL의 `fetch join` → `@EntityGraph`로 변경하여 동일한 동작을 어노테이션으로 선언

```java
// Before
@Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

// After
@EntityGraph(attributePaths = {"user"})
    @Query("SELECT t FROM Todo t ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);
```

**@EntityGraph 사용 시 주의점**
- `N:1 (@ManyToOne)`: JOIN 시 행 뻥튀기 없음 → 페이징 안전
- `1:N (@OneToMany)`: JOIN 시 행 뻥튀기 발생 → Hibernate가 메모리 페이징으로 전환
- `getTodos()`에서 실제 사용하는 데이터는 `user`뿐이므로 `attributePaths = {"user"}`만 지정

---

### Lv 4. 테스트 코드 연습

#### 1. PasswordEncoderTest 수정
`matches()` 메서드가 인코딩된 비밀번호와 원본 비밀번호를 올바르게 비교하도록 테스트 코드 수정

#### 2-1. ManagerServiceTest 메서드명 및 테스트 수정
- **문제**: `NullPointerException`이 아닌 `InvalidRequestException`을 던지는데 메서드명이 NPE로 되어 있음
- **해결**: 메서드명을 `manager_목록_조회_시_Todo가_없다면_InvalidRequestException_에러를_던진다()`로 수정, 예외 타입도 `InvalidRequestException`으로 변경

#### 2-2. CommentServiceTest 수정
- **문제**: `ServerException`으로 테스트하고 있었으나 서비스 로직은 `InvalidRequestException`을 던짐
- **해결**: `InvalidRequestException`으로 수정

#### 2-3. ManagerService 서비스 로직 수정
- **문제**: `todo.getUser()`가 `null`인 경우 `.getId()` 호출 시 `NullPointerException` 발생
- **해결**: `todo.getUser() == null` null 체크를 추가하여 `InvalidRequestException`이 발생하도록 수정

```java
// Before
if (!ObjectUtils.nullSafeEquals(user.getId(), todo.getUser().getId())) {
throw new InvalidRequestException("일정을 생성한 유저만 담당자를 지정할 수 있습니다.");
}
// After
if (todo.getUser() == null || !ObjectUtils.nullSafeEquals(user.getId(), todo.getUser().getId())) {
throw new InvalidRequestException("일정을 생성한 유저만 담당자를 지정할 수 있습니다.");
}
```

---

## 🚀 도전 기능 구현

### Lv 5. API 로깅 - Interceptor 구현

**구현 목적**
어드민 사용자만 접근 가능한 API(`/admin/**`)에 대한 접근 로그 기록

**구현 흐름**
```
클라이언트 요청
    ↓
JwtFilter → request에 userId, email, userRole 저장
    ↓
AdminApiLoggingInterceptor (preHandle)
    → request에서 userRole 꺼내기
    → ADMIN이 아니면 403 Forbidden 반환
    → ADMIN이면 요청 시각 + URL 로깅 후 통과
    ↓
Controller 실행
```

**구현 코드**

```java
@Slf4j
@Component
public class AdminApiLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        // request에서 JwtFilter에 저장해둔 userRole 꺼내기
        String userRole = (String) request.getAttribute("userRole");

        // Admin 권한 확인
        if (!"ADMIN".equals(userRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin 권한이 없습니다.");
            return false;
        }

        // 로깅 (요청 시각 + URL)
        log.info("ADMIN API 요청 - 시각: {}, URL: {}",
                LocalDateTime.now(),
                request.getRequestURI());

        return true; // 통과
    }
}
```

**WebMvcConfig 등록**
```java
@Configuration
@RequiredArgsConstructor

public class WebMvcConfig implements WebMvcConfigurer {

    private final AdminApiLoggingInterceptor adminApiLoggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(adminApiLoggingInterceptor)
                .addPathPatterns("/admin/**"); // CommentAdminController & UserAdminController
    }

}
```

**적용 대상**
- `DELETE /admin/comments/{commentId}` - `CommentAdminController.deleteComment()`
- `PATCH /admin/users/{userId}` - `UserAdminController.changeUserRole()`

---


### Lv 6. 내가 정의한 문제와 해결 과정

#### 1. 문제 인식 및 정의

`JwtFilter`와 `AdminApiLoggingInterceptor`에서 ADMIN 권한을 체크하는 로직이 **중복**으로 존재하는 것을 발견했습니다.

```java
// JwtFilter - ADMIN 권한 체크
if (url.startsWith("/admin") && !UserRole.ADMIN.equals(userRole)) {
log.warn("권한 부족: userId={}, role={}, URI={}", claims.getSubject(), userRole, url);
sendErrorResponse(httpResponse, HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");
return;
}

// AdminApiLoggingInterceptor - 또 ADMIN 권한 체크 (중복!)

if (!"ADMIN".equals(userRole)) {
response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin 권한이 없습니다.");
return false;
}
```

#### 2. 해결 방안

**2-1. 의사결정 과정**

| 방안 | 설명 | 장점 | 단점 |
|---|---|---|---|
| A. 현재 구조 유지 | Filter가 인가, Interceptor가 로깅 | 역할이 명확히 분리됨 | 권한 체크 중복 존재 |
| B. Interceptor로 권한 체크 이동 | Filter에서 권한 체크 제거 | 중복 제거 | Filter/Interceptor 역할 혼재 |

일반적으로 **인증/인가는 Filter의 역할**이라고 생각을 했습니다. Spring Security도 Filter 기반으로 인증/인가를 처리하며, Interceptor는 비즈니스 로직의 전처리를 담당하는 것이 자연스럽다고 생각합니다.

따라서 **방안 A를 선택**하여 Filter는 인가, Interceptor는 로깅이라는 단일 책임을 갖도록 유지하고, Interceptor의 중복된 권한 체크 로직을 제거하는 방향으로 결정했습니다.

**2-2. 해결 과정**

`AdminApiLoggingInterceptor`에서 중복된 권한 체크 로직을 제거하고 로깅만 담당하도록 수정했습니다.

```java
// Before - 권한 체크 + 로깅 (역할 혼재)
@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws IOException {
           // request에서 JwtFilter에 저장해둔 userRole 꺼내기
        String userRole = (String) request.getAttribute("userRole");

           // Admin 권한 확인
        if (!"ADMIN".equals(userRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin 권한이 없습니다.");
            return false;
        } 

    // 로깅 (요청 시각 + URL)
    log.info("ADMIN API 요청 - 시각: {}, URL: {}",
            LocalDateTime.now(),
            request.getRequestURI());

    return true; // 통과
}

// After - 로깅만 담당 (단일 책임)
@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws IOException {
    // 로깅 (요청 시각 + URL)
    log.info("ADMIN API 요청 - 시각: {}, URL: {}",
            LocalDateTime.now(),
            request.getRequestURI());

    return true; // 통과
}
```

#### 3. 해결 완료

**3-1. 회고**


- **Filter**: 보안 레이어 → 인증/인가 담당
- **Interceptor**: 비즈니스 레이어 → 전처리/후처리 담당

처음에는 단순히 중복 코드를 제거하는 것이 목표였지만, 이 과정에서 Filter와 Interceptor의 역할 차이를 이해하게 되었습니다.
단순히 코드를 줄이는 것보다 **각 클래스가 단일 책임을 갖도록 설계하는 것**이 더 중요하다는 것을 배웠습니다.

**3-2. 전후 데이터 비교**

| | Before | After |
|---|---|---|
| JwtFilter | 인증 + 인가 | 인증 + 인가 ✅ |
| AdminApiLoggingInterceptor | 인가 + 로깅 (역할 혼재) | 로깅만 담당 ✅ |
| 권한 체크 위치 | Filter + Interceptor (중복) | Filter만 (단일) ✅ |

---


<br>

<div align="center">

## 💖 끝까지 봐주셔서 감사합니다!

<img src="./images/thank_you.png" alt="Thank you for watching until the end" width="600">

<br>
---