package base;

import models.visitors.Visitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.openqa.selenium.WebDriverException;
import utils.TestException;

import java.io.IOException;

public class TestStopper implements TestRule {
    private static final Logger LOGGER = LogManager.getLogger(TestStopper.class);

    @Override
    public Statement apply(Statement base, Description description) {
        return statement(base, description);
    }

    private Statement statement(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Throwable caughtThrowable;
                try {
                    base.evaluate();
                    return;
                } catch (WebDriverException | TestException | AssertionError | IOException e) {
                    caughtThrowable = e;

                    if (TestContext.get() != null) {
                        for (Visitor visitor : TestContext.get().getVisitors()) {
                            LOGGER.error(
                                    "\nFailed test: " + TestContext.get().getTestMethodName() +
                                            "\n\tvisitor email: " + visitor.getEmail() +
                                            "\n\tvisitor's user name: " + visitor.getUserName()
                            );
                        }
                    }

                } finally {
                    TestContext.closeBrowsers();
                }

                throw caughtThrowable;
            }
        };
    }
}
