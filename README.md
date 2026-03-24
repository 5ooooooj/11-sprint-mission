# Discodeit - Chat Service Architecture Practice

간단한 채팅 서비스 도메인을 구현하면서  
**데이터 저장 방식과 애플리케이션 구조를 점진적으로 개선하는 프로젝트**

이 프로젝트는 다음 아키텍처 개선 과정을 통해 발전합니다.

```
In-Memory Storage (JCF)
        ↓
File Persistence (Serialization)
        ↓
Repository Pattern (Separation of Concerns)
```

---

# Project Goals

이 프로젝트를 통해 다음 개념을 학습합니다.

- 도메인 모델 설계
- Service Layer 설계
- Java Collections Framework 활용
- File I/O 기반 데이터 영속화
- 객체 직렬화 / 역직렬화
- Repository Pattern
- 관심사 분리 (Separation of Concerns)
- 의존성 주입 (Dependency Injection)

---

# Tech Stack

| Technology | Description |
|---|---|
| Java 17 | Programming Language |
| Gradle | Build Tool |
| Java Collections Framework | In-memory Data Storage |
| File I/O | Data Persistence |
| Serialization | Object Persistence |

---

# Architecture

프로젝트는 다음과 같은 레이어 구조를 갖습니다.

```
Application
     │
Service Layer
     │
Repository Layer
     │
Storage (JCF / File)

+ DTO Layer (Request / Response)
```

### Layer Responsibilities

| Layer | Responsibility |
|---|---|
| Application | 프로그램 실행 |
| Service | 비즈니스 로직 처리 |
| Repository | 데이터 저장 및 조회 |
| Storage | 실제 데이터 저장 방식 |

---

# Domain Model

프로젝트는 다음 세 가지 핵심 도메인으로 구성됩니다.

### User

```
User
 ├ id : UUID
 ├ name : String
 ├ createdAt : Long
 └ updatedAt : Long
```

---

### Channel

```
Channel
 ├ id : UUID
 ├ name : String
 ├ createdAt : Long
 └ updatedAt : Long
```

---

### Message

```
Message
 ├ id : UUID
 ├ userId : UUID
 ├ channelId : UUID
 ├ content : String
 ├ createdAt : Long
 └ updatedAt : Long
```

---

### ReadStatus
```
ReadStatus
 ├ id : UUID
 ├ userId : UUID
 ├ channelId : UUID
 └ lastReadAt : Instant
```
사용자가 특정 채널에서 마지막으로 읽은 메시지 시점을 관리합니다.

---

### UserStatus
```
UserStatus
 ├ id : UUID
 ├ userId : UUID
 └ lastSeenAt : Instant
```
사용자의 접속 상태를 관리합니다.
현재시간 - lastSeenAt ≤ 5분 → Online

---

### BinaryContent
```
BinaryContent
├ id : UUID
├ fileName : String
├ contentType : String
├ bytes : byte[]
└ createdAt : Instant
```
이미지 및 파일과 같은 첨부 데이터를 저장합니다.
- Message 첨부파일
- User 프로필 이미지

---

Message 생성 시 다음 검증이 수행됩니다.

```
1. User 존재 여부 확인
2. Channel 존재 여부 확인
```

---

# Storage Evolution

이 프로젝트는 **데이터 저장 방식을 단계적으로 개선하는 과정**을 포함합니다.

---

## 1. In-Memory Storage (JCF)

초기 구현에서는 데이터를 **Java Collections Framework**를 사용하여  
메모리에 저장했습니다.

```java
Map<UUID, User> data = new HashMap<>();
```

### 특징

- 빠른 데이터 접근
- 프로그램 종료 시 데이터 삭제

### 문제점

```
데이터 영속성 없음
```

프로그램이 종료되면 모든 데이터가 사라집니다.

---

## 2. File Persistence

데이터를 유지하기 위해 **File I/O + 객체 직렬화**를 도입했습니다.

```
Java Object
   ↓
Serialization
   ↓
File (.ser)
```

사용 기술

```
ObjectOutputStream
ObjectInputStream
```

---

## 3. Service Layer Enhancement 

Spring 기반으로 애플리케이션을 개선하고,
DTO와 새로운 도메인을 도입하여 비즈니스 로직을 고도화했습니다.

### DTO 적용

기존 엔티티 기반 파라미터 전달 방식에서 DTO 기반 구조로 개선했습니다.

```
Controller / Application
↓
DTO
↓
Service
```

### 장점

- 파라미터 그룹화
- 불필요한 데이터 노출 방지
- 유지보수성 향상

### Service 고도화

각 서비스는 다음과 같이 개선되었습니다.

| Service | 주요 기능 |
|---|---|
| UserService | 프로필 이미지, 온라인 상태 포함 |
| ChannelService | PUBLIC / PRIVATE 채널 분리 |
| MessageService | 첨부파일 처리 |
| ReadStatusService | 읽음 상태 관리 |
| UserStatusService | 접속 상태 관리 |
| BinaryContentService | 파일 저장 |

### 의존성 구조 개선

```
Before
Service → Service (강한 결합)
```

```
After
Service → Repository (느슨한 결합)
```

순환 참조를 방지하고 구조를 단순화 했습니다.

---

# Serialization

직렬화는 **Java 객체를 byte 배열로 변환하여 저장하는 과정**입니다.

```
Java Object
   ↓
byte[]
   ↓
File
```

역직렬화

```
File
 ↓
byte[]
 ↓
Java Object
```

---

### Serializable

모든 Entity는 다음 인터페이스를 구현합니다.

```java
implements Serializable
```

이는 **Marker Interface**로  
객체가 직렬화 가능하다는 사실을 JVM에 알립니다.

---

### serialVersionUID

```java
private static final long serialVersionUID = 1L;
```

클래스 변경 시 발생할 수 있는  
직렬화 호환성 문제를 방지하기 위해 선언합니다.

---

# Repository Pattern

초기 구조에서는 Service가 **저장 로직을 직접 처리**했습니다.

```
Service
 └ HashMap 조작
```

이 구조는 다음 문제를 발생시킵니다.

```
비즈니스 로직 + 저장 로직 혼합
```

이를 해결하기 위해 **Repository 패턴을 도입했습니다.**

---

## 개선된 구조

```
Service → Repository → Storage
```

### 역할 분리

| Layer | Responsibility |
|---|---|
| Service | 비즈니스 로직 |
| Repository | 데이터 저장 |
| Storage | 실제 저장 방식 |

---

### Repository Interface

```
UserRepository
ChannelRepository
MessageRepository
ReadStatusRepository
UserStatusRepository
BinaryContentRepository
```

기본 CRUD

```
save()
findById()
findAll()
delete()
```

---

# Storage Comparison

| Storage Type | Data Location | Persistence | Responsibility |
|---|---|---|---|
| JCF | Memory | X | 빠른 데이터 처리 |
| File | File System | O | 데이터 영속성 |
| Repository | Layer Structure | O | 관심사 분리 |

---

# Repository Selection Strategy (Spring + YAML)

이 프로젝트는 Repository 구현체를 코드 수정 없이
**application.yaml 설정 값으로 선택할 수 있도록 설계되었습니다.**

## 설정 방식

```yaml
discodeit:
  repository:
    type: jcf  # jcf | file
    file-directory: data (.discodeit)
```

## 구조

Service
   ↓
Repository (interface)
   ↓
┌───────────────┬───────────────┐
│ JCFRepository │ FileRepository │
└───────────────┴───────────────┘

### 특징

- 코드 수정 없이 저장 방식 변경 가능
- 환경별 설정 분리 가능 (dev / prod)
- 확장에 유리한 구조

### 파일 저장 경로 설정

```
discodeit:
  repository:
    file-directory: data (.discodeit)
```

ex) data/user.ser, data/messages.ser

### 설계 의도

이 구조는 다음을 달성합니다.
- DIP (Dependency Inversion Principle)
- 구현체 교체 가능 구조
- 테스트 / 운영 환경 분리 가능

"코드는 그대로, 설정만 바꿔서 동작 변경"

---

# Project Structure

```
com.sprint.mission.discodeit

config
├ AppConfig
└ RepositoryProperties

dto
├ BinaryContentCreateRequest
├ BinaryContentResponse
├ ChannelResponse
├ ChannelUpdateRequest
├ LoginRequest
├ MessageCreateRequest
├ MessageResponse
├ MessageUpdateRequest
├ PrivateChannelCreateRequest
├ PublicChannelCreateRequest
├ ReadStatusCreateRequest
├ ReadStatusResponse
├ ReadStatusUpdateRequest
├ UserCreateRequest
├ UserDto
├ UserStatusCreateRequest
├ UserStatusResponse
├ UserStatusUpdateRequest
└ UserUpdateRequest

entity
├ BaseEntity
├ BinaryContent
├ Channel
├ ChannelType
├ Message
├ ReadStatus
├ User
└ UserStatus

repository
 ├ (interface)
 │   ├ UserRepository
 │   ├ ChannelRepository
 │   ├ MessageRepository
 │   ├ ReadStatusRepository
 │   ├ UserStatusRepository
 │   └ BinaryContentRepository
 │
 ├ jcf
 │   ├ JCFUserRepository
 │   ├ JCFChannelRepository
 │   ├ JCFMessageRepository
 │   ├ JCFReadStatusRepository
 │   ├ JCFUserStatusRepository
 │   └ JCFBinaryContentRepository
 │
 └ file
     ├ FileUserRepository
     ├ FileChannelRepository
     ├ FileMessageRepository
     ├ FileReadStatusRepository
     ├ FileUserStatusRepository
     └ FileBinaryContentRepository

service
 ├ (interface)
 │   ├ UserService
 │   ├ ChannelService
 │   ├ MessageService
 │   ├ ReadStatusService
 │   ├ UserStatusService
 │   ├ BinaryContentService
 │   └ AuthService
 │
 └ basic
     ├ BasicUserService
     ├ BasicChannelService
     ├ BasicMessageService
     ├ BasicReadStatusService
     ├ BasicUserStatusService
     ├ BasicBinaryContentService
     └ BasicAuthService

DiscodeitApplication
```

---

# Key Learnings

이 프로젝트를 통해 다음 개념을 이해했습니다.

### 1. 데이터 저장 방식

```
Memory → File Persistence
```

데이터 영속성 개념 이해

---

### 2. 객체 직렬화

Java 객체를 파일로 저장하는 방법

---

### 3. Repository Pattern

저장 로직을 분리하여  
코드 유지보수성을 향상

---

### 4. 관심사 분리

```
Service → Business Logic
Repository → Persistence
```

---

### 5. DTO  패턴

엔티티와 외부 계층을 분리하여
데이터 전달 구조를 개선

---

### 6. 서비스 설계 확장

단순 CRUD를 넘어
- 도메인 간 관계 관리
- 첨부파일 처리
- 읽음 상태 관리

등 실제 서비스에 가까운 구조 경험

---

### 7. Spring 기반 구조 이해

- Bean 등록
- Dependency Injection
- IoC Container

기존 수동 객체 생성 방식에서 
Spring 기반 구조로 전환

---

# Future Improvements

- Database 기반 저장소 (JPA)
- Spring Boot 기반 REST API
- 실제 채팅 서버 구현

---

# References

- Effective Java
- Java Serialization Documentation
