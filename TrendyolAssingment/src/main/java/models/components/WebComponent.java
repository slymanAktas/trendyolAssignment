package models.components;

import models.actions.Action;
import models.browsers.Browser;

public class WebComponent {

    protected Browser browser;
    protected String id;
    protected String cssSelector;
    protected String xpath;
    protected String className;

    protected WebComponent() {}

    public Browser browser() {
        return this.browser;
    }

    public String getId() { return id; }

    public String getCssSelector() {
        return cssSelector;
    }

    public String getXpath() {
        String localXpath = xpath;

        if (localXpath == null && id != null) {
            localXpath = "//*[@id='" + id + "']";
        }

        return localXpath;
    }

    public void run(Action action) {
        action.execIn(this);
    }
}
