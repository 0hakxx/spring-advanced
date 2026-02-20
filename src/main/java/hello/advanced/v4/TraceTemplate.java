package hello.advanced.v4;

import hello.advanced.trace.LogTrace;
import hello.advanced.trace.TraceStatus;

public abstract class TraceTemplate<T> {

    private final LogTrace trace;

    public TraceTemplate(LogTrace trace) {
        this.trace = trace;
    }

    public T execute(String message){
        TraceStatus status = null;
        try {
            status = trace.begin(message);
            //로직 호출
            T result = call(); //상속받는 쪽에서 call() 메서드
            trace.end(status);
            return result;
        }
        catch (Exception e) {
            trace.exception(status, e);
            throw e; //예외를 꼭 다시 던져주어야 한다.
        }
    }

    protected abstract T call();
}
