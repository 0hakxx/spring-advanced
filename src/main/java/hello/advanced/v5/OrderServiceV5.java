package hello.advanced.v5;

import hello.advanced.trace.LogTrace;
import hello.advanced.trace.callback.TraceCallback;
import hello.advanced.trace.callback.TraceTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceV5 {

    private final OrderRepositoryV5 orderRepository;
    private final TraceTemplate template;

    public OrderServiceV5(OrderRepositoryV5 orderRepository, LogTrace trace) {
        this.orderRepository = orderRepository;
        this.template = new TraceTemplate(trace);
    }


    public void orderItem(String itemId) {
        template.execute("OrderService.orderItem()", new TraceCallback<Void>() {
            @Override
            public Void call() {
                orderRepository.save(itemId);
                return null;
            }
        });
    }
}