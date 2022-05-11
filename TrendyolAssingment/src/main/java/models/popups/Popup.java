package models.popups;

import models.actions.Action;
import models.components.WebComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.TestException;

public abstract class Popup extends WebComponent {
    protected WebComponent opener;

    public By getLocator() {
        if (this.id != null) {
            return By.id(id);
        } else if (this.className != null) {
            return By.className(className);
        } else if (this.cssSelector != null) {
            return By.cssSelector(cssSelector);
        } else if (this.xpath != null) {
            return By.xpath(xpath);
        } else {
            throw new TestException("Popup has no container field!");
        }
    }

    public WebElement getPopupContainer() {
        return browser.findElement(getLocator());
    }

    public WebComponent clickX(){
//        run(new CloseAction());
        run(Action.close());
        return opener;
    }

    public boolean isDisplayed(int timeOutInSeconds) {
        WebElement popup = browser.presenceWaitInSafe(timeOutInSeconds, getLocator());
        if (popup == null) {
            return false;
        } else {
            return popup.isDisplayed();
        }
    }

    public boolean isVisible() {
        return browser.isElementVisible(5, getLocator());
    }

    public String text() {
        return browser.findElement(getLocator()).getText();
    }
}
