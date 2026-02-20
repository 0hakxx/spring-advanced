package hello.advanced.v4;

import hello.advanced.trace.LogTrace;
import hello.advanced.trace.TraceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV4 {
    private final OrderServiceV4 orderService;
        private final LogTrace trace;

        @GetMapping("/v4/request")
        public String request(String itemId) {
            TraceTemplate<String> template = new TraceTemplate(trace) {
                @Override
                protected String call() {
                    orderService.orderItem(itemId);
                    return "ok";
                }
            };
            String result = template.execute("OrderController.request()");
            return result;
    }
}