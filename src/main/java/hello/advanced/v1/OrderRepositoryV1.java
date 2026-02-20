package hello.advanced.v1;

import hello.advanced.hellotrace.HelloTraceV1;
import hello.advanced.trace.TraceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;




@Repository
@RequiredArgsConstructor
public class OrderRepositoryV1 {

    private final HelloTraceV1 trace;
    public void save(String itemId)  {
        // 저장 로직
        try {
            TraceStatus status = trace.begin("OrderRepositoryV4.save()");
            if ("ex".equals(itemId)) {
                throw new IllegalStateException("예외 발생!");
            }
             sleep(1000); // 저장하는데 1초 걸린다고 가정
             trace.end(status);
        }
        catch (Exception e) {
            trace.exception(null, e);
            throw e; //예외를 꼭 다시 던져주어야 한다.
        }
    }
    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();}
    }
}
