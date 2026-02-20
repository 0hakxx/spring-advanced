package hello.advanced.trace.template.code;

public abstract class AbstractTemplate {
    public void execute() {
        long startTime = System.currentTimeMillis();

        // 비즈니스 로직 실행
        call();

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        System.out.println("resultTime = " + resultTime + "ms");
    }

    protected abstract void call();

}
