package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class ScheduledUtils {

    private static final Logger LOGGER = LogManager.getLogger(ScheduledUtils.class);

    private ScheduledUtils() {
    }

    public static void threadWaiter(int milliSeconds) {
        Callable callableDelayedTask = () -> " ms waited...";
        ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(1);

        ScheduledFuture sf = scheduledPool.schedule(callableDelayedTask, milliSeconds, MILLISECONDS);

        String value = null;
        try {
            value = (String) sf.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.throwing(e);
            Thread.currentThread().interrupt();
        }

        scheduledPool.shutdown();
        LOGGER.trace(milliSeconds + value);
    }
}