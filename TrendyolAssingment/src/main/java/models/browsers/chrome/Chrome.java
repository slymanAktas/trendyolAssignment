package models.browsers.chrome;

import models.browsers.Browser;
import models.browsers.RemoteDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public abstract class Chrome extends Browser {
    public abstract ChromeOptions getOptions(boolean isHeadless);

    @Override
    public void initInLocal() {
//        ChromeDriverManager.getInstance().setup();
//        ChromeDriverManager.getInstance().version("98.0.4758.102").setup();
        System.setProperty("webdriver.chrome.driver", "src/main/java/models/browsers/chrome/chromedriver");
        webDriver = new ChromeDriver(getOptions(false));
        System.setProperty("webdriver.chrome.silentOutput", "true"); // İgnore ChromeDriver warnings
        webDriver.manage().window().maximize();
    }

    @Override
    public void initInGrid() {
        webDriver = RemoteDriver.create(getOptions(true));
    }
}
