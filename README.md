# 📅 일정 관리 플랫폼 (Task Management App)

사용자가 일정을 등록하고, 해당 일정에 대해 자유롭게 댓글로 소통할 수 있는 서비스입니다.

---

## 🚀 프로그램 소개
**이 프로젝트는 사용자가 일정을 생성하여 댓글을 통해 소통 하고, 체계적인 일정 관리를 목표로 합니다.**

---

## 📑 API 명세 (Key Endpoints)

### 1. 유저 관련 (User)
| 기능 | 메서드 | 엔드포인트 | 상세 보기 |
| :--- | :---: | :--- | :---: |
| 회원가입 | `POST` | `/users/signup` | [클릭](#1-1-회원가입) |
| 로그인 | `POST` | `/users/login` | [클릭](#1-2-로그인) |
| 로그아웃 | `POST` | `/users/logout` | [클릭](#1-3-로그아웃) |
| 유저 단건 조회 | `GET` | `/users/{userId}` | [클릭](#1-4-유저-단건-조회) |
| 유저 전체 목록 | `GET` | `/users` | - |
| 유저 정보 수정 | `PUT` | `/users/{userId}` | - |
| 회원 탈퇴 | `DELETE` | `/users/{userId}` | - |


### 2. 일정 관련 (Schedule)
| 기능 | 메서드 | 엔드포인트 | 상세 보기 |
| :--- | :---: | :--- | :---: |
| 일정 생성 | `POST` | `/users/{userId}schedules` | [클릭](#2-1-일정-생성) |
| 일정 상세 조회 | `GET` | `/users/{userId}/schedules/{scheduleId}` | [클릭](#2-2-일정-상세-조회) |
| 일정 전체 목록 | `GET` | `/users/{userId}/schedules` | - |
| 일정 수정 | `PUT` | `/users/{userId}/schedules/{scheduleId}` | - |
| 일정 삭제 | `DELETE` | `/users/{userId}/schedules/{scheduleId}` | - |

### 3. 댓글 관련 (Comment)
| 기능 | 메서드 | 엔드포인트 | 상세 보기 |
| :--- | :---: | :--- | :---: |
| 댓글 작성 | `POST` | `/users/{userId}/schedules/{scheduleId}/comments` | [클릭](#3-1-댓글-작성) |
| 댓글 상세 조회 | `GET` | `/users/{userId}/schedules/{scheduleId}/comments/{commentId}` | - |
| 댓글 전체 목록 | `GET` | `/users/{userId}/schedules/{scheduleId}/comments` | - |
| 댓글 수정 | `PUT` | `/users/{userId}/schedules/{scheduleId}/comments/{commentId}` | - |
| 댓글 삭제 | `DELETE` | `/users/{userId}/schedules/{scheduleId}/comments/{commentId}` | - |
## 📖 API Documentation
상세한 API 사용법과 파라미터 정보는 아래 포스트맨 문서에서 확인하실 수 있습니다.

[![Run in Postman](https://run.pstmn.io/button.svg)](https://documenter.getpostman.com/view/51186116/2sBXcBmh8b)

> **Tip:** 위 버튼을 클릭하면 브라우저에서 명세서를 확인하거나, 본인의 포스트맨으로 컬렉션을 가져올 수 있습니다.

---

## 📊 ERD (Entity Relationship Diagram)


**데이터베이스의 연관 관계는 다음과 같이 구성되어 있습니다.**

User : Schedule = 1 : N (@ManyToOne)

User : Comment = 1 : N (@ManyToOne)

Schedule : Comment = 1 : N (양방향 연관관계)

**일정 삭제 시 관련 댓글이 함께 관리되도록 영속성 전이(Cascade) 적용.**


<p align="center">
  <img width="360" height="424" alt="Image" src="https://github.com/user-attachments/assets/b4aadd4c-f89c-47e8-983d-02563255cb94" />
  <br>
  <em> ERD </em>
</p>


---

## 🌟 주요 기능


**1. 사용자 관리 (User)**

회원가입 및 고유 식별자를 통한 유저 식별.

자신이 작성한 일정과 댓글에 대한 소유권 검증.

**2. 일정 관리 (Schedule)**

일정 생성, 수정, 조회 및 삭제 (CRUD).


**3. 댓글 시스템 (Comment)**

일정별 피드백 및 소통을 위한 댓글 기능.



---


## 📁 프로젝트 구조

```text
schedule
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.example.schedule
│   │   │       ├── user 
│   │   │       ├── schedule        
│   │   │       ├── comment     
│   │   │       ├── exception 
│   │   │       ├── common    
|   |   |       ├── config
|   |   |       └── utill
|   |   |
│   │   └── resources
│   │       ├── static
│   │       ├── templates
│   │       └── application.properties
├── build.gradle
└── README.md
```
## ⚙️ 세팅 정보

***application.properties***

```text
spring.application.name=UserSchedule
spring.datasource.url=jdbc:mysql://localhost:3306/UserSchedule
spring.datasource.username=${Ho}
spring.datasource.password=${Ho_pw}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=create
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```
***build.gradle***

```text
dependencies {
    implementation 'at.favre.lib:bcrypt:0.10.2'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
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
## 🛠 기술 스택

| Category | Technology | Description |
| :--- | :--- | :--- |
| **Language** | Java 17 | JDK 17 LTS 버전 사용 |
| **Framework** | Spring Boot 3.x | 효율적인 REST API 서버 구축 |
| **ORM** | Spring Data JPA | Entity 연관 관계 및 데이터 영속성 관리 |
| **Database** | MySQL | 서비스 데이터 저장 및 관계형 스키마 설계 |
| **Build Tool** | Gradle | 프로젝트 빌드 및 의존성 관리 |

---

<p align="center">
  <img width=""500 height="500" alt="Image" src="https://github.com/user-attachments/assets/2ccf6c48-0868-4838-be99-be8b4b914d8a" />
  <br>
  <em> 🙏 바쁘신 와중에 세세히 봐주셔서 감사합니다! </em>
</p>













