# 📅 일정 관리 앱 (Schedule App)

내 일정을 관리할 수 있는 프로젝트입니다.

## 📑 API 명세서

### 1. 일정 관리
| 기능 | 메서드 | URL | 설명 |
| :--- | :--- | :--- | :--- |
| 일정 등록 | `POST` | `/schedules` | 새로운 일정을 등록합니다. |
| 전체 조회 | `GET` | `/schedules` | 모든 일정을 조회합니다. |
| 단건 조회 | `GET` | `/schedules/{id}` | 특정 ID의 일정을 상세 조회합니다. |
| 일정 수정 | `PUT` | `/schedules/{id}` | 일정 제목과 작성자를 수정합니다. |
| 일정 삭제 | `DELETE` | `/schedules/{id}` | 특정 사용자의 일정을 삭제합니다. |

---

### 📥 API 

#### **일정 등록**
- **URL**: `POST/schedules`
- **Request Body**
```json
{
    "title": "여행",
    "contents": "해외여행 2026.02.10 ~ 2026.02.20",
    "writer": "둘리",
    "password": "asd123456789"
}
```
- **Response Body**
- **Status code: (201 Created)**
```json
{
    "id": 2,
    "title": "여행",
    "contents": "해외여행 2026.02.10 ~ 2026.02.20",
    "writer": "둘리",
    "createdAt": "2026-02-04T17:41:06.1563508",
    "modifiedAt": "2026-02-04T17:41:06.1563508"
}
```

#### **단 건 조회**
- **URL**: `GET/schedules/{id}`
- **Path Variable**: `scheduleId`
- **Response Body**
- **Status code: (200 OK)**
```json
{
    "id": 2,
    "title": "여행",
    "contents": "해외여행 2026.02.10 ~ 2026.02.20",
    "writer": "둘리",
    "createdAt": "2026-02-04T17:41:06.156351",
    "modifiedAt": "2026-02-04T17:41:06.156351"
}
```

#### **전체 조회**
- **URL**: `GET/schedules`
- **@RequestParam**:`(required = false) String writer`
- **Response Body**
- **Status code: (200 OK)**
```json
[
   {
        "id": 2,
        "title": "여행",
        "contents": "해외여행 2026.02.10 ~ 2026.02.20",
        "writer": "둘리",
        "createdAt": "2026-02-04T17:41:06.156351",
        "modifiedAt": "2026-02-04T17:41:06.156351"
    },
    {
        "id": 1,
        "title": "여행",
        "contents": "제주도 여행 4박 5일",
        "writer": "고길동",
        "createdAt": "2026-02-04T17:33:14.720814",
        "modifiedAt": "2026-02-04T17:33:14.720814"
    }
]
```

#### **일정 수정**
- **URL**: `PUT/schedules/{id}`
- **Path Variable**: `scheduleId`
- **Request Body**
```json
{
    "title": "미식 여행",
    "contents": "해외여행 2026.02.10 ~ 2026.02.20",
    "writer": "푸드파이터",
    "password": "asd123456789"
}
```
- **Response Body**
- **Status code: (200 OK)**
```json
{
    "id": 2,
    "title": "미식 여행",
    "writer": "푸드파이터",
    "createdAt": "2026-02-04T17:41:06.156351",
    "modifiedAt": "2026-02-04T17:41:06.156351",
    "contents": "해외여행 2026.02.10 ~ 2026.02.20"
}
```

#### **일정 삭제**
- **URL**: `DELETE/schedules/{id}`
- **Path Variable**: `scheduleId`
- **Response Body**
- **Status code: (204 No Content)**

---

## 🏗️ 데이터베이스 시각화 (ERD) - **Entity Relationship Diagram**

- 데이터베이스 구조를 한 눈에 파악할 수 있다.
- 설계를 진행하며 논리적인 오류를 미리 발견하고 수정할 수 있다.
- 데이터 구조를 문서화하여 다른 사람과의 소통을 원활하게 한다. (협업 관점)
- 데이터 구조의 근본적인 결함을 제거하여 기술적 부채를 사전에 방지한다.

<p align="center">
  <img width="157" height="197" alt="Image" src="https://github.com/user-attachments/assets/4b59fe75-eb61-4e87-af94-250f6b6112b1" />
  <br>
  <em> Schedule Entity </em>
</p>

---

## 🚀 주요 기능

**🎯 일정 생성**

- 일정에 대한 제목을 지어서 보기 편하게 만들어보세요.
- 무엇을 할 건지 자세하게 기록하여 남겨보세요.
- 일정 생성시 작성일과 수정일을 자동으로 반영이 됩니다.

**⭐ 일정 조회**

- **전체 일정 조회**
    - 작성자의 모든 일정을 조회해볼 수 있습니다.
    - 다른 사람들의 일정까지 모두 보고싶다면 작성자를 빼고 검색해주세요!

- **선택 일정 조회**
    - 보고 싶은 일정을 하나 콕집어서 볼 수 있습니다~

**🔧 일정 수정**
- 일정 제목이나 작성자 이름에 오타가 났을 때 유용하게 활용해보세요.
- 수정 완료시 자동으로 수정일이 갱신됩니다.

**📱 일정 삭제**
- 삭제하고 싶은 일정만 골라서 삭제해보세요.

---

## 📁 프로젝트 구조

```text
schedule
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.example.schedule
│   │   │       ├── controller (API 요청 처리)
│   │   │       ├── dto        (데이터 전달 객체)
│   │   │       ├── entity     (JPA 영속 컨텍스트 관리 객체)
│   │   │       ├── repository (DB 접근 인터페이스)
│   │   │       └── service    (비즈니스 로직)
│   │   └── resources
│   │       ├── static
│   │       ├── templates
│   │       └── application.properties (프로젝트 설정 정보 파일)
├── build.gradle
└── README.md
```
---

## ⚙️ 설정 정보

****application.propertie****
```text

spring.application.name=schedule
spring.datasource.url=jdbc:mysql://localhost:3306/schedule
spring.datasource.username=
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

```

****build.gradle****
```text

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-webmvc'
    compileOnly 'org.projectlombok:lombok'
    runtimeOnly 'com.mysql:mysql-connector-j'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-data-jpa-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-webmvc-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
```

---
## 🛠️ 기술 스택
- Java 17
- Spring Boot
- Spring JPA
- MySQL

---
<p align="center">
  <img width="400" height="500" alt="Image" src="https://github.com/user-attachments/assets/3b41a83b-ae49-4c68-9b17-b0c44c626355" />
  <br>
  <em> 🙏 끝까지 봐주셔서 감사합니다! </em>
</p>





