package hello.advanced.trace.template.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubClassLogic extends AbstractTemplate {
    class Logic1 extends AbstractTemplate {
        @Override
        protected void call() {
            log.info("비즈니스 로직1 실행");
        }
    }
    class Logic2 extends AbstractTemplate {
        @Override
        protected void call() {
            log.info("비즈니스 로직2 실행");
        }
    }

     @Override
    protected void call() {
        log.info("비즈니스 로직 실행");
    }
}
