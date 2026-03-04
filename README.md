# Discodeit - Chat Service Architecture Practice

간단한 채팅 서비스 도메인을 구현하면서 **데이터 저장 방식과 애플리케이션 구조를 점진적으로 개선하는 프로젝트**

이 프로젝트는 다음 아키텍처 개선 과정을 통해 발전합니다.

'''
In-Memory Storage (JCF) -> File Persistence (Serialization) -> Repository Pattern (Separation of Concerns)
'''

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
| Java 17 | Programing Language |
| Gradle | Build Tool |
| Java Collection Framework | In-memory Data Storage |
| File I/O | Data Persistence |
| Serialization | Object Persistence |

---

# Architecture

프로젝트는 다음과 같은 레이어 구조를 갖습니다.

'''
Application
    |
Service Layer
    |
Repository Layer
    |
Storage (JCF / File)
'''

### Layer Responsibilities

| Layer | Responsibility |
|---|---|
| Application | 프로그램 실행 |
| Service | 비즈니스 로직 처리 |
| Respository | 데이터 저장 및 조회 |
| Storage | 실제 데이터 저장 방식 | 

---

# Domain Model

프로젝트는 다음 세 가지 핵심 도메인으로 구성됩니다.

### User
'''
User
|- id : UUID
|- name : String
|- createdAt : Long
|- updatedAt : Long
'''

---

### Channel

'''
Channel
|- id : UUID
|- name : String
|- createdAt : Long
|- updatedAt : Long
'''

---

### Message

'''
Message
|- id : UUID
|- userId : UUID
|- channelId : UUID
|- content : String
|- createdAt : Long
|- updatedAt : Long
'''

Message 생성 시 다음 검증이 수행됩니다.

'''
1. User 존재 여부 확인
2. Channel 존재 여부 확인
'''

---

# Storage Evolution

이 프로젝트는 **데이터 저장 방식을 단계적으로 개선하는 과정**을 포함합니다.

---

## (초기) In-Memory Storage (JCF)

초기 구현에서는 데이터를 **Java Collections Framework**를 사용하여 메모리에 저장 했습니다.

'''java
Map<UUID, User> data = new HashMap<>();
'''

### 특징 

- 빠른 데이터 접근
- 프로그램 종료 시 데이터 삭제

### 문제점

''' 
데이터 영속성 없음
'''

프로그램이 종료되면 모든 데이터가 사라집니다.

---

## (변경) File Persistence

데이터를 유지하기 위해 **File I/O + 객체 직렬화**를 도입했습니다.

'''
Java Object -> Serialization -> File (.ser)
'''

사용 기술

'''
ObjectInputStream
ObjectOutputStream
'''

---

# Serialization

직렬화는 **Java 객체를 byte 배열로 변환하여 저장하는 과정** 입니다.

'''
Java Object -> byte[] -> File
'''

역직렬화

'''
File -> byte[] -> Java Object
'''

---

### Serializable

모든 Entity는 다음 인터페이스를 구현합니다.

'''java
implements Serializable
'''

이는 **Marker Interfave**로 객체가 직렬화 가능하다는 사실을 JVM에 알립니다.

---

### serialVersionUID

'''java
private static final long serialVersionUID = 1L;
'''

클래스 변경 시 발생할 수 있는 직렬화 호환성 문제를 방지하기 위해 선언합니다.

---

# Repository Pattern

초기 구조에서는 Service가 **저장 로직을 직접 처리**했습니다.

'''
Service
|- HashMap 조작
'''

이 구조는 다음 문제를 발생시킵니다.

'''
비즈니스 로직 + 저장 로직 혼합
'''

이를 해결하기 위해 **Repository 패턴을 도입했습니다.**

---

## 개선된 구조

'''
Service → Repository → Storage
'''

### 역할 분리

| Layer | Responsibility |
|---|---|
| Service | 비즈니스 로직 |
| Repository | 데이터 저장 |
| Storage | 실제 저장 방식 |

---

### Repository Interface

'''
UserRepository
ChannelRepository
MessageRepository
'''

기본 CRUD

'''
save()
findById()
findAll()
delete()
'''

# Storage Comparison
| Storage Type | Data Location | Persistence | Responsibility |
|---|---|---|---|
| JCF | Memory | X | 빠른 데이터 처리 |
| File | File System | O | 데이터 영속성 |
| Repository | Layer Structure | O | 관심사 분리 | 

---

# Project Structure

'''
com.sprint.mission.discodeit

entity
 |- User
 |- Channel
 |- Message

 service
 |- UserService
 |- ChannelService
 |- MessageService

service.jcf
 |- JCFUserService
 |- JCFChannelService
 |- JCFMessageService

service.file
 |- FileUserService
 |- FileChannelService
 |- FileMessageService

repository
 |- UserRepository
 |- ChannelRepository
 |- MessageRepository

repository.jcf
repository.file
'''

---

# Key Learnings

이 프로젝트를 통해 다음 개념을 이해했습니다.

### 1. 데이터 저장 방식

''' 
Memory -> File Persistence
'''

데이처 영속성 개념 이해

---

### 2. 객체 직렬화

Java 객체를 파일로 저장하는 방법

---

### 3. Repository Pattern

저장 로직을 분리하여 코드 유지보수성을 향상

---

### 4. 관심사 분리

''' 
Service -> Business Logic
Repository -> Persistence
'''

---

# Future Improvements

- Database 기반 저장소 (JPA)
- Spring Boot 기반 REST API
- 실제 채팅 서버 구현

---

# References

- Effective Java
- Java Serialization Documentation
