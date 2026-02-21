package hello.advanced.trace.callback;

import hello.advanced.trace.LogTrace;
import hello.advanced.trace.TraceStatus;

public class TraceTemplate {
    private LogTrace trace;

    public TraceTemplate(LogTrace trace) {
        this.trace = trace;
    }

    public <T> T execute(String message, TraceCallback<T> callback) {
        TraceStatus status = null;
        try {
            status = trace.begin(message);
            // 로직 호출
            T result = callback.call();
            trace.end(status);
            return result;
        } catch (Exception e) {
            trace.exception(status, e);
            throw e; // 예외를 다시 던져야 한다.
        }
    }
}
