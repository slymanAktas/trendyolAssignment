package models.browsers;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ScheduledUtils;

import java.util.List;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class SeleniumBrowser {
    private static final Logger LOGGER = LogManager.getLogger(SeleniumBrowser.class);

    protected WebDriver webDriver;

    public void waitForAjax() {
        try {
            WebDriverWait myWait = new WebDriverWait(webDriver, 60);
            ExpectedCondition<Boolean> conditionToCheck = input -> {
                JavascriptExecutor jsDriver = (JavascriptExecutor) webDriver;
                boolean stillRunningAjax = (Boolean) jsDriver
                        .executeScript("return (window.jQuery != undefined && ($(':animated').length != 0 || jQuery.active != 0)) || document.readyState != \"complete\"");
                return !stillRunningAjax;
            };

            myWait.until(conditionToCheck);
        } catch (RuntimeException rte) {
            LOGGER.error("" + rte);
        }
    }

    public WebElement findElement(By by) {
        return findElement(by, null);
    }

    public WebElement findElement(int timeout, By by) {
        waitForPresenceOf(timeout, by);
        return findElement(by, null);
    }

    private WebElement findElement(By by, WebElement element) {
        List<WebElement> elements;
        if (element != null) {
            elements = element.findElements(by);
        } else {
            elements = webDriver.findElements(by);
        }
        return elements.isEmpty() ? null : elements.get(0);
    }

    public void waitForPresenceOf(int seconds, By elementLocator) {
        WebDriverWait wait = new WebDriverWait(webDriver, seconds);
        wait.until(visibilityOfElementLocated(elementLocator));
    }

    public WebElement visibilityWait(int timeoutInSeconds, WebElement element) {
        Wait<WebDriver> wait = new FluentWait<>(webDriver).
                withTimeout(ofSeconds(timeoutInSeconds)).
                pollingEvery(ofMillis(500)).
                ignoring(NotFoundException.class).ignoring(NoSuchElementException.class);
        return wait.until(visibilityOf(element));
    }

    public WebElement waitForClickableOf(int timeoutInSeconds, WebElement elementLocator) {
        Wait<WebDriver> wait = new FluentWait<>(webDriver).
                withTimeout(ofSeconds(timeoutInSeconds)).
                pollingEvery(ofMillis(500)).
                ignoring(NotFoundException.class).ignoring(NoSuchElementException.class);
        return wait.until(elementToBeClickable(elementLocator));
    }

    public boolean isElementPresent(By by) {
        return !webDriver.findElements(by).isEmpty();
    }

    public void waitForUrlHasChanged(int seconds, String urlTextBefore) {
        WebDriverWait wait = new WebDriverWait(webDriver, seconds);
        wait.until(not(urlContains(urlTextBefore)));
        waitForDialog();
    }

    public void waitForDialog() {
        int counter = 0;
        boolean isPageLoaded = isElementPresent(By.cssSelector("div.q-loader"));
        while (counter < 5 && isPageLoaded) {
            counter++;
            wait(1);
            isPageLoaded = isElementPresent(By.cssSelector("div.q-loader"));
        }
    }

    public void wait(int seconds) {
        waitMs(seconds * 1000);
    }

    public void waitMs(int milliSeconds) {
        try {
            ScheduledUtils.threadWaiter(milliSeconds);
        } catch (RuntimeException rte) {
            LOGGER.error("Exception on selenium waiting, " + rte);
        }
    }

    public String currentURL() {
        try {
            return webDriver.getCurrentUrl();
        } catch (TimeoutException toe) {
            LOGGER.error("Timeout when trying to get currentUrl!");
            return null;
        } catch (WebDriverException wde) {
            LOGGER.error("Exception occured while trying to get currentUrl, " + wde);
            return null;
        }
    }

    public List<WebElement> findElements(By by) {
        return webDriver.findElements(by);
    }

    public boolean isElementVisible(long timeoutInSeconds, By by) {
        Wait<WebDriver> wait = new FluentWait<>(webDriver).
                withTimeout(ofSeconds(timeoutInSeconds)).
                pollingEvery(ofMillis(500)).
                ignoring(NotFoundException.class).ignoring(NoSuchElementException.class);
        try {
            wait.until(visibilityOfElementLocated(by));
            return true;
        } catch (WebDriverException wde) {
            return false;
        }
    }

    public WebElement presenceWaitInSafe(int timeoutInSeconds, By by) {
        if (isElementPresent(timeoutInSeconds, by)) {
            return findElement(by);
        } else {
            return null;
        }
    }

    public boolean isElementPresent(long timeoutInSeconds, By by) {
        Wait<WebDriver> wait = new FluentWait<>(webDriver).
                withTimeout(ofSeconds(timeoutInSeconds)).
                pollingEvery(ofMillis(500)).
                ignoring(NotFoundException.class).ignoring(NoSuchElementException.class);
        try {
            wait.until(presenceOfElementLocated(by));
            return true;
        } catch (TimeoutException toe) {
            return false;
        }
    }
}
