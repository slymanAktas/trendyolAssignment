package base;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import utils.WireMockUtils;

public class WireMockWatcher implements TestRule {

    private static Statement statement(Statement base) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try{
                    WireMockUtils.start();
                    base.evaluate();

                }finally {
                    WireMockUtils.stop();
                }
            }
        };
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return statement(base);
    }
}
