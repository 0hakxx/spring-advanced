package hello.advanced.trace.strategy;

import hello.advanced.trace.strategy.code.strategy.ContextV1;
import hello.advanced.trace.strategy.code.strategy.ContextV2;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic1;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class Conv2Test {
    @Test
    void Test(){
        StrategyLogic1 strategyLogic1 = new StrategyLogic1();
        ContextV2 contextv2 = new ContextV2();
        contextv2.execute(strategyLogic1);
    }
}
