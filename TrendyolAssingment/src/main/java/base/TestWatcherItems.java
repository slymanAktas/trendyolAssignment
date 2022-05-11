package base;

import models.browsers.Browser;
import models.visitors.Visitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.io.IOException;
import java.util.List;

import static utils.DateUtils.getDateNow;

public class TestWatcherItems extends TestWatcher {
    private static final Logger LOGGER = LogManager.getLogger(TestWatcherItems.class);

    @Override
    public void starting(Description desc) {
        LOGGER.info("TEST STARTED: " + desc.getDisplayName());
    }

    @Override
    public void succeeded(Description desc) {
        LOGGER.info("TEST FINISHED SUCCESSFULLY: " + desc.getDisplayName());
    }

    @Override
    public void failed(Throwable e, Description desc) {
        try {
            LOGGER.error("TEST HAS BEEN FAILED: " + desc.getDisplayName());
            TestContext testContext = TestContext.get();
            String dateNow = getDateNow();
            if (testContext != null) {
                List<Visitor> allUsers = testContext.getVisitors();
                allUsers.removeIf((Visitor vi) -> vi.getBrowser() == null);
                for (Visitor visitor : allUsers) {
                    screenShotTakerForEachVisitor(visitor, dateNow, desc);
                }
            }
        } catch (IOException ioe) {
            LOGGER.error("Exception on taking screenshot! " + ioe);
        }
    }

    private static void screenShotTakerForEachVisitor(Visitor visitor, String dateNow, Description d) throws IOException {
        Browser browser = visitor.getBrowser();
        browser.takeScreenshot(d.getMethodName() + " - " + dateNow + " - " + visitor.getClass().getSimpleName());
    }
}
