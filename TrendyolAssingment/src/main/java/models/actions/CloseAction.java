package models.actions;

import models.components.WebComponent;
import org.openqa.selenium.By;

public class CloseAction implements Action {
    private static final By xButton = By.xpath("//div[@class='modal-close']");

    @Override
    public void execIn(WebComponent component) {
        component.browser().clickToBy(xButton);
    }
}
