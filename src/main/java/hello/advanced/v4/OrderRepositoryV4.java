package hello.advanced.v4;

import hello.advanced.trace.LogTrace;
import hello.advanced.trace.TraceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;




@Repository
@RequiredArgsConstructor
public class OrderRepositoryV4 {

    private final LogTrace trace;

    public void save(String itemId) {
        TraceTemplate<String> template = new TraceTemplate(trace) {
            @Override
            protected String call() {
                TraceStatus status = trace.begin("OrderRepositoryV5.save()");
                if ("ex".equals(itemId)) {
                    throw new IllegalStateException("예외 발생!");
                }
                sleep(1000); // 저장하는데 1초 걸린다고 가정
                return "ok";
            }
        };
        template.execute("OrderRepository.save()");
    }
    public void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
    }
}
}