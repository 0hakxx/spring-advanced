package hello.advanced.trace;

import java.util.UUID;

/**
 * 하나의 HTTP 요청을 추적하기 위한 식별자 클래스.
 * - id    : 같은 요청 흐름 안에서 공유되는 고유한 트랜잭션 ID (UUID 앞 8자리)
 * - level : 메서드 호출 깊이 (Controller=0, Service=1, Repository=2, ...)
 */
public class TraceId {
    private String id;    // 트랜잭션 ID (같은 요청이면 동일한 값)
    private int level;    // 호출 깊이 (들여쓰기 단계)

    /** 최초 생성 시: 새로운 UUID를 발급하고 depth 0에서 시작 */
    public TraceId(){
        this.id = createId();
        this.level = 0;
    }

    /** 기존 id와 depth를 직접 지정할 때 사용 (createNextId / createPreviousId 내부용) */
    public TraceId(String id, int level) {
        this.id = id;
        this.level = level;
    }

    /** 현재 호출이 가장 바깥쪽(첫 번째 depth)인지 확인 */
    public boolean isFirstLevel(){
       if (this.level == 0){
              return true;
       }
       else return false;
    }

    /** UUID를 생성하고 앞 8자리만 잘라서 반환 */
    private String createId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    /** 다음 depth로 이동한 새로운 TraceId 생성 (id는 유지, level+1) */
    TraceId createNextId() {
       return new TraceId(this.id, this.level+1);
    }

    /** 이전 depth로 돌아간 새로운 TraceId 생성 (id는 유지, level-1) */
    TraceId createPreviousId() {
        return new TraceId(this.id, this.level-1);
    }

    public String getId() {
        return id;
    }
    public int getLevel() {
        return level;
    }
}
