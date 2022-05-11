package models.pages;

import config.Config;
import org.openqa.selenium.By;

public class LoginPage extends Page {
    private static final String PAGE_URL = Config.TRENDYOL_BASE_HTTPS_URL + "/giris";
    private static final By emailTextBox = By.id("login-email");
    private static final By passwordTextBox = By.id("login-password-input");
    private static final By submitBtn = By.cssSelector("button[type='submit']");
    private static final By myAccountText = By.xpath("//*[text()='Hesabım']");

    public LoginPage() { this.setUrl(PAGE_URL); }

    @Override
    public boolean login() {
        browser
                .type(emailTextBox, browser.getVisitor().getEmail())
                .type(passwordTextBox, browser.getVisitor().getPassword())
                .clickToBy(submitBtn);

        return isBuyerOnline();
    }

    private boolean isBuyerOnline(){
        boolean isVisitorRedirectedToHomePage = browser.currentURL().equals(Config.TRENDYOL_BASE_HTTPS_URL+"/");
        boolean isMyAccountsTextDisplayed = browser.isElementPresent(myAccountText);

        return isVisitorRedirectedToHomePage && isMyAccountsTextDisplayed;
    }
}
