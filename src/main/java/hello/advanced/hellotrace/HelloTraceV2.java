package hello.advanced.hellotrace;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class HelloTraceV2 {

    // 로그 prefix 상수 - 호출 시작 / 정상 종료 / 예외 종료를 시각적으로 구분
    private static final String START_PREFIX    = "-->";  // 메서드 시작
    private static final String COMPLETE_PREFIX = "<--";  // 정상 완료
    private static final String EX_PREFIX       = "<X-";  // 예외 발생

    /**
     * 메서드 호출 시작을 기록한다.
     * - 새로운 TraceId(고유 ID + level=0)를 생성한다.
     * - 시작 시각을 기록하고, TraceStatus를 반환한다.
     * - 반환된 TraceStatus는 반드시 end() 또는 exception()에 전달해야 한다.
     */
    public TraceStatus begin(String message) {
        TraceId traceId = new TraceId();                          // 새 트랜잭션 ID 생성 (level=0)
        Long startTimeMs = System.currentTimeMillis();            // 시작 시각 기록
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);    // 상태 객체 반환
    }

    /**
     * 메서드가 정상 종료되었을 때 호출한다.
     * 내부적으로 complete()를 통해 실행 시간을 로그로 출력한다.
     */
    public void end(TraceStatus status) {
        complete(status, null);  // 예외 없이 정상 종료
    }

    /**
     * 메서드 실행 중 예외가 발생했을 때 호출한다.
     * 예외 정보를 함께 로그로 출력한다.
     */
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);  // 예외와 함께 종료
    }

    // V2 추가
    public TraceStatus beginSync(TraceId traceId, String message) {
        TraceId syncTraceId = new TraceId(traceId.getId(), traceId.getLevel() + 1); // 기존 ID 유지, level만 1 증가
        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", syncTraceId.getId(), addSpace(START_PREFIX, syncTraceId.getLevel()), message);
        return new TraceStatus(syncTraceId, startTimeMs, message);
    }

    /**
     * end() / exception() 공통 처리 메서드.
     * - 경과 시간(ms)을 계산해 로그에 출력한다.
     * - e가 null이면 정상 종료 prefix(<--), null이 아니면 예외 prefix(<X-)를 사용한다.
     */
    private void complete(TraceStatus status, Exception e) {
        long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();  // 실행 시간 = 종료 - 시작
        TraceId traceId = status.getTraceId();

        if (e == null) {
            // 정상 종료: "<-- message time=Xms"
            log.info("[{}] {}{} time={}ms",
                    traceId.getId(), addSpace(COMPLETE_PREFIX, traceId.getLevel()),
                    status.getMessage(), resultTimeMs);
        } else {
            // 예외 종료: "<X- message time=Xms ex=예외메시지"
            log.info("[{}] {}{} time={}ms ex={}",
                    traceId.getId(), addSpace(EX_PREFIX, traceId.getLevel()),
                    status.getMessage(), resultTimeMs, e.toString());
        }
    }
    /**
     * 출력 예시:
     *   level=0 → ""           (들여쓰기 없음, Controller 단계)
     *   level=1 → "|-->"       (Service 단계)
     *   level=2 → "|  |-->"    (Repository 단계)
     */
    private String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < level; i++) {
            boolean isLastDepth = (i == level - 1);

            if (isLastDepth) {
                sb.append("|").append(prefix); // 현재 depth 표시: "|-->" 또는 "|<--" 등
            } else {
                sb.append("|  ");              // 상위 depth 구분선: 세로선 + 공백 2칸
            }
        }

        return sb.toString();
    }
}
