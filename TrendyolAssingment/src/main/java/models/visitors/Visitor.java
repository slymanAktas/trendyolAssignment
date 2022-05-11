package models.visitors;

import base.TestContext;
import models.browsers.Browser;
import models.pages.Page;

public class Visitor<V extends Visitor> {

    private Browser browser;
    private String email;
    private String password;
    private String userName;
    private boolean isOnline;
    private TestContext testContext;

    public Visitor(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Browser browser() {
        return browser;
    }

    public void closeBrowser() {
        browser.close();
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public Browser getBrowser() {
        return browser;
    }

    public TestContext getTestContext() {
        return testContext;
    }

    public void setTestContext(TestContext testContext) {
        this.testContext = testContext;
    }

    public <P extends Page> V open(P page) {
        return open(page, null);
    }

    public <P extends Page> V open(P page, String browserName) {
        this.browser = Browser.openThe(page, browserName);
        return (V) this;
    }

    public Page nowLookingAt() {
        return browser.page();
    }

    public Visitor login() {
        browser.setVisitor(this);
        isOnline = browser.page().login();
        return this;
    }
}
