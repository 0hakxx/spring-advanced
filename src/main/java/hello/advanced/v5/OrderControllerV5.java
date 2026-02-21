package hello.advanced.v5;

import hello.advanced.trace.LogTrace;
import hello.advanced.trace.callback.TraceCallback;
import hello.advanced.trace.callback.TraceTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.callback.Callback;

@RestController
public class OrderControllerV5 {
    private final OrderServiceV5 orderService;
    private final TraceTemplate template;

    public OrderControllerV5(OrderServiceV5 orderService, LogTrace trace) {
        this.orderService = orderService;
        this.template = new TraceTemplate(trace);
    }

    @GetMapping("/v5/request")
    public String request(String itemId) {
        return template.execute("OrderController.request()", new TraceCallback<String>() {
            @Override
            public String call() {
                orderService.orderItem(itemId);
                return "ok";
            }
        }
        );
    }
}