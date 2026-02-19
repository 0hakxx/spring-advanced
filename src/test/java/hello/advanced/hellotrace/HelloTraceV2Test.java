package hello.advanced.hellotrace;

import hello.advanced.trace.TraceStatus;
import org.junit.jupiter.api.Test;

class HelloTraceV2Test {
    @Test
    void begin_end() {
        HelloTraceV2 trace = new HelloTraceV2();
        TraceStatus hello1 = trace.begin("hello1");
        TraceStatus hello2 = trace.beginSync(hello1.getTraceId(), hello1.getMessage());
        trace.end(hello2);
        trace.end(hello1);

    }
    @Test
    void begin_exception() {
        HelloTraceV2 trace = new HelloTraceV2();
        TraceStatus hello1 = trace.begin("hello1");
        TraceStatus hello2 = trace.beginSync(hello1.getTraceId(), hello1.getMessage());
        trace.exception(hello2, new IllegalStateException());
        trace.exception(hello1, new IllegalStateException());
    }
}