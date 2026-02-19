package hello.advanced.trace;

/**
 * 하나의 메서드 호출 시작 시점의 상태를 담는 클래스.
 * begin() 호출 시 생성되어, end() / exception() 호출 시 경과 시간 계산에 사용된다.
 *
 * - traceId     : 현재 요청의 트랜잭션 ID와 호출 depth
 * - startTimeMs : 메서드가 시작된 시각 (밀리초, System.currentTimeMillis())
 * - message     : 로그에 출력할 메서드명 등의 설명 문자열
 */
public class TraceStatus {
    private TraceId traceId;      // 어떤 요청의 몇 번째 depth인지 식별
    private long startTimeMs;     // 시작 시각 → end 시점과의 차이로 실행 시간 측정
    private String message;       // 로그에 표시할 메시지 (보통 "ClassName.methodName()")

    public TraceStatus(TraceId traceId, Long startTimeMs,  String message)  {
        this.traceId = traceId;
        this.message = message;
        this.startTimeMs = startTimeMs;
    }
    public TraceId getTraceId() {
        return traceId;
    }
    public String getMessage() {
        return message;
    }
    public long getStartTimeMs() {
        return startTimeMs;
    }
}
