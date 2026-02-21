package hello.advanced.v5;

import hello.advanced.trace.LogTrace;
import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;

import hello.advanced.trace.callback.TraceTemplate;

import org.springframework.stereotype.Repository;




@Repository
public class OrderRepositoryV5 {
    private final TraceTemplate template;

    public OrderRepositoryV5(LogTrace trace) {
        this.template = new TraceTemplate(trace);
    }

    public void save(String itemId) {
        template.execute("OrderRepository.save()", () -> {
            if ("ex".equals(itemId)) {
                throw new IllegalStateException("예외 발생!");
            }
            sleep(1000); // 저장하는데 1초 걸린다고 가정
            return null;
        });
    }
    public void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
    }
}
}