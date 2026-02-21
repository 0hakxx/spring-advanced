package hello.advanced.trace.strategy.code.strategy;

public class StrategyLogic2 implements Strategy {
    @Override
    public void call() {
        System.out.println("비즈니스 로직2 실행");
    }
}
