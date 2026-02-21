# 🌱 Spring Advanced - 로그 추적기 & 디자인 패턴 학습 프로젝트

> 📚 김영한 - [스프링 핵심 원리 - 고급편](https://www.inflearn.com/course/스프링-핵심-원리-고급편) 강의 실습 레포지토리

---

## 📋 목차

- [프로젝트 개요](#프로젝트-개요)
- [기술 스택](#기술-스택)
- [프로젝트 구조](#프로젝트-구조)
- [학습 주제별 설명](#학습-주제별-설명)
  - [V0 - 기본 주문 흐름](#v0---기본-주문-흐름-로그-없음)
  - [V1 - HelloTraceV1 적용](#v1---hellotrace-v1-적용-독립-로그)
  - [V2 - HelloTraceV2 적용 (TraceId 파라미터 전달)](#v2---hellotrace-v2-적용-traceid-파라미터-전달)
  - [V3 - LogTrace 인터페이스 + FieldLogTrace](#v3---logtrace-인터페이스--fieldlogtrace)
  - [V4 - 템플릿 메서드 패턴 (추상 클래스)](#v4---템플릿-메서드-패턴-추상-클래스)
  - [V5 - 템플릿 콜백 패턴 (TraceCallback)](#v5---템플릿-콜백-패턴-tracecallback)
  - [ThreadLocal - 동시성 문제 해결](#threadlocal---동시성-문제-해결)
  - [전략 패턴 (Strategy Pattern)](#전략-패턴-strategy-pattern)
  - [템플릿 메서드 패턴 (Template Method Pattern)](#템플릿-메서드-패턴-template-method-pattern)
  - [템플릿 콜백 패턴 (Template Callback Pattern)](#템플릿-콜백-패턴-template-callback-pattern)
- [실행 방법](#실행-방법)
- [API 엔드포인트](#api-엔드포인트)

---

## 프로젝트 개요

이 프로젝트는 **HTTP 요청 로그 추적기**를 단계적으로 발전시켜 나가는 과정을 담고 있습니다.  
각 버전(V0 ~ V5)은 코드의 문제점을 발견하고 더 나은 설계 방법으로 리팩터링하는 과정을 보여줍니다.

또한 **ThreadLocal**, **전략 패턴(Strategy Pattern)**, **템플릿 메서드 패턴(Template Method Pattern)**, **템플릿 콜백 패턴(Template Callback Pattern)** 등의 디자인 패턴을 함께 학습합니다.

### 핵심 목표
- 로그 추적기 개발 & 반복 코드 제거
- **동시성 문제** 이해 및 `ThreadLocal`을 통한 해결
- 다양한 **디자인 패턴** 적용으로 코드 품질 향상
- **OCP(개방-폐쇄 원칙)** 준수

---

## 기술 스택

| 기술 | 버전 |
|------|------|
| Java | 17 |
| Spring Boot | 3.5.10 |
| Gradle | - |
| Lombok | - |
| JUnit 5 | 5.8.1 |

---

## 프로젝트 구조

```
src
├── main/java/hello/advanced
│   ├── AdvancedApplication.java          # 스프링 부트 메인 클래스
│   │
│   ├── hellotrace/
│   │   ├── HelloTraceV1.java             # 최초 로그 추적기 (독립 트랜잭션)
│   │   └── HelloTraceV2.java             # 트랜잭션 ID 동기화 (파라미터 전달 방식)
│   │
│   ├── trace/
│   │   ├── TraceId.java                  # 트랜잭션 ID + 호출 레벨 관리
│   │   ├── TraceStatus.java              # 시작 시각, 메시지, TraceId 보관
│   │   ├── LogTrace.java                 # 로그 추적기 인터페이스
│   │   ├── FieldLogTrace.java            # 필드 기반 구현 (동시성 문제 있음)
│   │   ├── ThreadLocalLogTrace.java      # ThreadLocal 기반 구현 (동시성 해결)
│   │   ├── LogTraceConfig.java           # LogTrace 스프링 빈 등록
│   │   │
│   │   ├── callback/
│   │   │   ├── TraceCallback.java        # 콜백 인터페이스
│   │   │   └── TraceTemplate.java        # 템플릿 콜백 패턴 구현체
│   │   │
│   │   └── strategy/
│   │       ├── Conv1Test.java            # 전략 패턴 - 필드 주입 테스트
│   │       ├── Conv2Test.java            # 전략 패턴 - 파라미터 전달 테스트
│   │       └── code/strategy/
│   │           ├── Strategy.java         # 전략 인터페이스
│   │           ├── StrategyLogic1.java   # 전략 구현체 1
│   │           ├── StrategyLogic2.java   # 전략 구현체 2
│   │           ├── ContextV1.java        # 전략을 필드에 보관하는 컨텍스트
│   │           └── ContextV2.java        # 전략을 파라미터로 전달받는 컨텍스트
│   │
│   ├── v0/   # 로그 없음 (순수 비즈니스 로직)
│   │   ├── OrderControllerV0.java
│   │   ├── OrderServiceV0.java
│   │   └── OrderRepositoryV0.java
│   │
│   ├── v1/   # HelloTraceV1 적용 (독립 트랜잭션 ID)
│   │   ├── OrderControllerV1.java
│   │   ├── OrderServiceV1.java
│   │   └── OrderRepositoryV1.java
│   │
│   ├── v2/   # HelloTraceV2 적용 (TraceId 파라미터 전달)
│   │   ├── OrderControllerV2.java
│   │   ├── OrderServiceV2.java
│   │   └── OrderRepositoryV2.java
│   │
│   ├── v3/   # LogTrace 인터페이스 적용 (FieldLogTrace)
│   │   ├── OrderControllerV3.java
│   │   ├── OrderServiceV3.java
│   │   └── OrderRepositoryV3.java
│   │
│   ├── v4/   # 템플릿 메서드 패턴 적용 (추상 클래스)
│   │   ├── TraceTemplate.java            # 추상 클래스 (제네릭)
│   │   ├── OrderControllerV4.java
│   │   ├── OrderServiceV4.java
│   │   └── OrderRepositoryV4.java
│   │
│   └── v5/   # 템플릿 콜백 패턴 적용
│       ├── OrderControllerV5.java
│       ├── OrderServiceV5.java
│       └── OrderRepositoryV5.java
│
└── test/java/hello/advanced
    ├── trace/
    │   ├── FieldLogTraceTest.java
    │   ├── ThreadLocalLogTraceTest.java
    │   └── threadlocal/
    │       ├── FieldServiceTest.java         # 동시성 문제 재현 테스트
    │       ├── ThreadLocalServiceTest.java   # ThreadLocal 해결 확인 테스트
    │       └── code/
    │           ├── FiledService.java         # 일반 필드 사용 (동시성 문제)
    │           └── ThreadLocalService.java   # ThreadLocal 사용
    │
    ├── trace/template/
    │   ├── TemplateMethodTest.java
    │   └── code/
    │       ├── AbstractTemplate.java
    │       ├── SubClassLogic1.java
    │       ├── SubClassLogic2.java
    │       └── SubClassLogic.java
    │
    └── trace/strategy/code/strategy/
        ├── Callback.java
        ├── TimeLogTemplate.java
        └── TemplateCallbackTest.java
```

---

## 학습 주제별 설명

---

### V0 - 기본 주문 흐름 (로그 없음)

> 📌 순수한 비즈니스 로직만 존재하는 기본 상태

**흐름:** `OrderControllerV0` → `OrderServiceV0` → `OrderRepositoryV0`

```java
// OrderRepositoryV0 - itemId가 "ex"이면 예외 발생, 아니면 1초 대기
public void save(String itemId) {
    if (itemId.equals("ex")) {
        throw new IllegalStateException("예외 발생!");
    }
    sleep(1000);
}
```

- 로그가 없기 때문에 요청 흐름 추적이 불가능
- 이 문제를 해결하기 위해 로그 추적기 개발 시작

---

### V1 - HelloTrace V1 적용 (독립 로그)

> 📌 각 계층에서 독립적으로 트랜잭션 ID를 생성

**핵심 클래스:** `HelloTraceV1`, `TraceId`, `TraceStatus`

```
[aaaaaaaa] OrderController.request()
[bbbbbbbb] OrderService.orderItem()     ← 다른 트랜잭션 ID!
[cccccccc] OrderRepository.save()       ← 다른 트랜잭션 ID!
```

**문제점:** 각 메서드마다 새로운 `TraceId`가 생성되어 **같은 HTTP 요���임에도 트랜잭션 ID가 다르고 들여쓰기(depth)도 적용되지 않음**

| 클래스 | 역할 |
|--------|------|
| `TraceId` | UUID 앞 8자리 트랜잭션 ID + 호출 depth |
| `TraceStatus` | 시작 시각, 메시지, TraceId 보관 |
| `HelloTraceV1` | `begin()`, `end()`, `exception()` 제공 |

---

### V2 - HelloTrace V2 적용 (TraceId 파라미터 전달)

> 📌 `TraceId`를 파라미터로 직접 전달하여 같은 트랜잭션 ID 유지

**핵심 메서드:** `HelloTraceV2.beginSync(TraceId, message)`

```java
// OrderControllerV2
status = trace.begin("OrderController.request()");
orderService.orderItem(status.getTraceId(), itemId); // TraceId 전달

// OrderServiceV2
public void orderItem(TraceId traceId, String itemId) {
    status = trace.beginSync(traceId, "OrderService.orderItem()");
    // ...
}
```

**출력 결과:**
```
[aaaaaaaa] OrderController.request()
[aaaaaaaa] |-->OrderService.orderItem()
[aaaaaaaa] |   |-->OrderRepository.save()
[aaaaaaaa] |   |<--OrderRepository.save() time=1001ms
[aaaaaaaa] |<--OrderService.orderItem() time=1001ms
[aaaaaaaa] <--OrderController.request() time=1002ms
```

**문제점:** 모든 메서드 시그니처에 `TraceId` 파라미터가 추가됨 → **인터페이스 오염** (비즈니스 로직과 무관한 파라미터)

---

### V3 - LogTrace 인터페이스 + FieldLogTrace

> 📌 `TraceId`를 파라미터로 넘기지 않고 **필드(인스턴스 변수)** 에 보관

**핵심 인터페이스:** `LogTrace`
```java
public interface LogTrace {
    TraceStatus begin(String message);
    void end(TraceStatus status);
    void exception(TraceStatus status, Exception e);
}
```

**핵심 구현체:** `FieldLogTrace`
```java
public class FieldLogTrace implements LogTrace {
    private TraceId traceIdHolder; // ← 필드에 TraceId 보관

    private void syncTraceId() {
        if (traceIdHolder == null) {
            traceIdHolder = new TraceId();
        } else {
            traceIdHolder = traceIdHolder.createNextId(); // depth+1
        }
    }
}
```

**스프링 빈 등록:**
```java
@Configuration
public class LogTraceConfig {
    @Bean
    public LogTrace logTrace() {
        return new FieldLogTrace();
    }
}
```

**문제점:** `FieldLogTrace`는 **싱글톤 빈**으로 등록되어 있기 때문에, 동시에 여러 HTTP 요청이 들어오면 **`traceIdHolder` 필드를 공유하여 동시성 문제 발생!**

---

### V4 - 템플릿 메서드 패턴 (추상 클래스)

> 📌 try-catch 반복 코드를 **추상 클래스(Template Method Pattern)** 로 제거

**반복되는 코드 (V3):**
```java
TraceStatus status = null;
try {
    status = trace.begin("...");
    // 비즈니스 로직
    trace.end(status);
} catch (Exception e) {
    trace.exception(status, e);
    throw e;
}
```

**해결 - TraceTemplate (추상 클래스, 제네릭):**
```java
public abstract class TraceTemplate<T> {
    private final LogTrace trace;

    public T execute(String message) {
        TraceStatus status = null;
        try {
            status = trace.begin(message);
            T result = call(); // 추상 메서드 호출 (비즈니스 로직)
            trace.end(status);
            return result;
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }

    protected abstract T call(); // 하위 클래스에서 구현
}
```

**사용 (익명 클래스):**
```java
// OrderControllerV4
TraceTemplate<String> template = new TraceTemplate(trace) {
    @Override
    protected String call() {
        orderService.orderItem(itemId);
        return "ok";
    }
};
return template.execute("OrderController.request()");
```

**문제점:** 익명 클래스 문법이 여전히 번거롭고, **상속 기반**이라 유연성에 한계가 있음

---

### V5 - 템플릿 콜백 패턴 (TraceCallback)

> 📌 추상 클래스 대신 **콜백 인터페이스**를 파라미터로 전달 (Strategy Pattern의 발전)

**핵심 인터페이스 & 클래스:**
```java
// TraceCallback - 콜백 인터페이스
public interface TraceCallback<T> {
    T call();
}

// TraceTemplate - 템플릿 (싱글톤으로 재사용 가능)
public class TraceTemplate {
    private LogTrace trace;

    public <T> T execute(String message, TraceCallback<T> callback) {
        TraceStatus status = null;
        try {
            status = trace.begin(message);
            T result = callback.call();
            trace.end(status);
            return result;
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
```

**사용 (익명 클래스 또는 람다):**
```java
// OrderControllerV5 - 익명 내부 클래스
return template.execute("OrderController.request()", new TraceCallback<String>() {
    @Override
    public String call() {
        orderService.orderItem(itemId);
        return "ok";
    }
});

// OrderRepositoryV5 - 람다로 간결하게!
template.execute("OrderRepository.save()", () -> {
    if ("ex".equals(itemId)) throw new IllegalStateException("예외 발생!");
    sleep(1000);
    return null;
});
```

**V4 vs V5 비교:**
| | V4 (Template Method) | V5 (Template Callback) |
|---|---|---|
| 구조 | 추상 클래스 + 상속 | 인터페이스 + 파라미터 전달 |
| 재사용 | 매번 익명 클래스 생성 | TraceTemplate 싱글톤으로 재사용 |
| 람다 지원 | ❌ | ✅ |
| 유연성 | 낮음 (상속 강제) | 높음 (조합) |

---

### ThreadLocal - 동시성 문제 해결

> 📌 `FieldLogTrace`의 동시성 문제를 `ThreadLocal`로 해결

#### 문제 재현 - FiledService (동시성 문제)

```java
public class FiledService {
    private String nameStore; // 공유 필드 → 동시성 문제!

    public String logic(String name) {
        nameStore = name; // Thread-A가 저장한 값을
        sleep(1000);
        return nameStore; // Thread-B가 덮어써버릴 수 있음
    }
}
```

**테스트 결과 (동시성 문제):**
```
thread-A | 저장 name=userA -> nameStore=null
thread-B | 저장 name=userB -> nameStore=userA  ← userA가 남아있음
thread-A | 조회 nameStore=userB              ← userB로 덮어써짐!
thread-B | 조회 nameStore=userB
```

#### 해결 - ThreadLocalService

```java
public class ThreadLocalService {
    private ThreadLocal<String> nameStore = new ThreadLocal<>(); // 스레드마다 독립적인 저장소

    public String logic(String name) {
        nameStore.set(name);
        sleep(1000);
        return nameStore.get();
    }
}
```

**테스트 결과 (정상):**
```
thread-A | 저장 name=userA
thread-B | 저장 name=userB
thread-A | 조회 nameStore=userA ← 올바르게 분리됨
thread-B | 조회 nameStore=userB
```

#### ThreadLocalLogTrace

```java
public class ThreadLocalLogTrace implements LogTrace {
    private ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>();

    private void releaseTraceId() {
        TraceId traceId = traceIdHolder.get();
        if (traceId.isFirstLevel()) {
            traceIdHolder.remove(); // ⚠️ 반드시 remove() 호출! 메모리 누수 방지
        } else {
            traceIdHolder.set(traceId.createPreviousId());
        }
    }
}
```

> ⚠️ **주의:** 스레드 풀 환경에서는 스레드가 재사용되므로, 작업 완료 후 반드시 `traceIdHolder.remove()`를 호출해야 합니다!

---

### 전략 패턴 (Strategy Pattern)

> 📌 변하는 부분(비즈니스 로직)과 변하지 않는 부분(시간 측정 등)을 분리

#### ContextV1 - 전략을 필드에 보관

```java
@Slf4j
public class ContextV1 {
    private Strategy strategy; // 필드에 전략 저장

    public ContextV1(Strategy strategy) {
        this.strategy = strategy;
    }

    public void execute() {
        long startTime = System.currentTimeMillis();
        strategy.call(); // 위임 - 실제 비즈니스 로직은 전략에게
        long resultTime = System.currentTimeMillis() - startTime;
        log.info("resultTime={}", resultTime);
    }
}
```

```java
// 사용 예시
ContextV1 contextV1 = new ContextV1(new StrategyLogic1());
contextV1.execute(); // "스프링의 DI" 방식과 유사
```

#### ContextV2 - 전략을 파라미터로 전달

```java
@Slf4j
public class ContextV2 {
    public void execute(Strategy strategy) { // 파라미터로 전달
        long startTime = System.currentTimeMillis();
        strategy.call();
        long resultTime = System.currentTimeMillis() - startTime;
        log.info("resultTime={}", resultTime);
    }
}
```

| | ContextV1 (필드 방식) | ContextV2 (파라미터 방식) |
|---|---|---|
| 전략 변경 시점 | 선조립 후 실행 | 실행 시점에 변경 가능 |
| 유연성 | 낮음 | 높음 |
| Spring DI | 유사함 | - |

---

### 템플릿 메서드 패턴 (Template Method Pattern)

> 📌 상위 클래스(템플릿)에서 전체 알고리즘 골격을 정의, 하위 클래스에서 세부 구현

```java
// 추상 클래스 (템플릿)
public abstract class AbstractTemplate {
    public void execute() {
        long startTime = System.currentTimeMillis();
        call(); // 추상 메서드 - 하위 클래스가 구현
        long resultTime = System.currentTimeMillis() - startTime;
        System.out.println("resultTime = " + resultTime + "ms");
    }
    protected abstract void call();
}

// 구체 클래스 (하위)
public class SubClassLogic1 extends AbstractTemplate {
    @Override
    protected void call() {
        log.info("비즈니스 로직1 실행");
    }
}
```

**익명 클래스로 사용:**
```java
@Test
void templateMethodV2() {
    AbstractTemplate template1 = new AbstractTemplate() {
        @Override
        protected void call() {
            log.info("비즈니스 로직1 실행");
        }
    };
    template1.execute();
}
```

**단점:** 상속을 사용하기 때문에 자식 클래스가 부모 클래스에 의존하게 됨 → 변경 시 영향도 큼

---

### 템플릿 콜백 패턴 (Template Callback Pattern)

> 📌 Spring의 `JdbcTemplate`, `RestTemplate`, `TransactionTemplate` 등에서 광범위하게 사용되는 패턴

```java
// Callback 인터페이스
public interface Callback {
    void call();
}

// TimeLogTemplate - 템플릿
public class TimeLogTemplate {
    public void execute(Callback callback) {
        long startTime = System.currentTimeMillis();
        callback.call(); // 위임
        long resultTime = System.currentTimeMillis() - startTime;
        log.info("resultTime={}", resultTime);
    }
}
```

**테스트:**
```java
@Test
void callbackV1() {
    TimeLogTemplate template = new TimeLogTemplate();
    // 익명 내부 클래스
    template.execute(new Callback() {
        @Override
        public void call() {
            log.info("비즈니스 로직1 실행");
        }
    });
}

@Test
void callbackV2() {
    TimeLogTemplate template = new TimeLogTemplate();
    // 람다 표현식
    template.execute(() -> log.info("비즈니스 로직1 실행"));
    template.execute(() -> log.info("비즈니스 로직2 실행"));
}
```

---

## 실행 방법

### 1. 클론

```bash
git clone https://github.com/0hakxx/spring-advanced.git
cd spring-advanced
```

### 2. 빌드 및 실행

```bash
./gradlew bootRun
```

또는 IDE(IntelliJ IDEA)에서 `AdvancedApplication.java`의 `main()` 메서드를 직접 실행

### 3. 테스트 실행

```bash
./gradlew test
```

---

## API 엔드포인트

서버 기본 포트: `8080`

| 버전 | URL | 설명 |
|------|-----|------|
| V0 | `GET /v0/request?itemId=hello` | 로그 없는 기본 주문 |
| V1 | `GET /v1/request?itemId=hello` | HelloTraceV1 적용 (독립 트랜잭션) |
| V2 | `GET /v2/request?itemId=hello` | HelloTraceV2 적용 (파라미터 전달) |
| V3 | `GET /v3/request?itemId=hello` | LogTrace 인터페이스 적용 |
| V4 | `GET /v4/request?itemId=hello` | 템플릿 메서드 패턴 적용 |
| V5 | `GET /v5/request?itemId=hello` | 템플릿 콜백 패턴 적용 |

### 예외 발생 테스트

```bash
# itemId=ex 로 요청하면 IllegalStateException 발생
GET /v5/request?itemId=ex
```

### 정상 요청 로그 예시 (V5)

```
[aaaaaaaa] OrderController.request()
[aaaaaaaa] |-->OrderService.orderItem()
[aaaaaaaa] |   |-->OrderRepository.save()
[aaaaaaaa] |   |<--OrderRepository.save() time=1001ms
[aaaaaaaa] |<--OrderService.orderItem() time=1001ms
[aaaaaaaa] <--OrderController.request() time=1002ms
```

### 예외 발생 로그 예시

```
[aaaaaaaa] OrderController.request()
[aaaaaaaa] |-->OrderService.orderItem()
[aaaaaaaa] |   |-->OrderRepository.save()
[aaaaaaaa] |   |<X-OrderRepository.save() time=0ms ex=java.lang.IllegalStateException: 예외 발생!
[aaaaaaaa] |<X-OrderService.orderItem() time=1ms ex=java.lang.IllegalStateException: 예외 발생!
[aaaaaaaa] <X-OrderController.request() time=2ms ex=java.lang.IllegalStateException: 예외 발생!
```

---

## 📌 핵심 개념 정리

### 로그 출력 포맷

| 심볼 | 의미 |
|------|------|
| `-->` | 메서드 호출 시작 |
| `<--` | 메서드 정상 종료 |
| `<X-` | 메서드 예외 종료 |
| `\|` | 호출 depth 들여쓰기 |

### 버전별 발전 과정 요약

```
V0: 순수 비즈니스 로직 (로그 없음)
 ↓
V1: HelloTraceV1 - 각 계층에서 독립 로그 (트랜잭션 ID 불일치)
 ↓
V2: HelloTraceV2 - TraceId를 파라미터로 전달 (메서드 시그니처 오염)
 ↓
V3: LogTrace 인터페이스 + FieldLogTrace (동시성 문제 발생)
 ↓ (ThreadLocal로 동시성 해결 → ThreadLocalLogTrace)
V4: 템플릿 메서드 패턴 (추상 클래스 - try/catch 중복 제거)
 ↓
V5: 템플릿 콜백 패턴 (인터페이스 + 람다로 최종 개선)
```

---

> 📖 **참고**: 이 프로젝트는 [인프런 - 스프링 핵심 원리 고급편 (김영한)](https://www.inflearn.com/course/스프링-핵심-원리-고급편) 강의를 바탕으로 작성되었습니다.
