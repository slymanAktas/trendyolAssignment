package models.browsers.firefox;

import io.github.bonigarcia.wdm.FirefoxDriverManager;
import models.browsers.Browser;
import models.browsers.RemoteDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import static org.openqa.selenium.remote.CapabilityType.PLATFORM_NAME;
import static config.Config.platform;

public class Firefox extends Browser {

    private static FirefoxOptions getOptions() {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("network.proxy.type", 1);
        profile.setPreference("network.proxy.http", "localhost");
        profile.setPreference("network.proxy.http_port", 1080);

        FirefoxOptions options = new FirefoxOptions();
        options.setCapability(PLATFORM_NAME, platform);
        options.setCapability("firefox_profile", profile);
        return options;
    }

    @Override
    public void initInLocal() {
        FirefoxDriverManager.getInstance().setup();
        webDriver = new FirefoxDriver(getOptions());
    }

    @Override
    public void initInGrid() {
        webDriver = RemoteDriver.create(getOptions());
    }
}
