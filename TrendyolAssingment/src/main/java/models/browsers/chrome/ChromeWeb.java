package models.browsers.chrome;

import base.TestContext;
import org.openqa.selenium.chrome.ChromeOptions;
import utils.DateUtils;

import java.util.*;

import static org.openqa.selenium.remote.CapabilityType.PLATFORM_NAME;
import static config.Config.platform;

public class ChromeWeb extends Chrome{
    @Override
    public ChromeOptions getOptions(boolean isHeadless) {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        options.setCapability(PLATFORM_NAME, platform);
        options.setCapability("acceptInsecureCerts", true);
        options.setCapability("name", TestContext.get().getTestMethodName() + " - " + DateUtils.getDateNow("ddMMyyyy hhmmss"));

        List<String> arguments = new ArrayList<>(Arrays.asList(
                "--headless",
                "--start-maximized",
                "--test-type",
                "--no-sandbox",
                "--ignore-certificate-errors",
                "--disable-popup-blocking",
                "--disable-default-apps",
                "--disable-extensions-file-access-check",
                "--incognito",
                "--disable-gpu",
                "--disable-features=CookiesWithoutSameSiteMustBeSecure,SameSiteByDefaultCookies,#CookiesWithoutSameSiteMustBeSecure",
                "window-position=1620,0",
                "--disable-notifications",
                "disable-infobars"
        ));

        int index = isHeadless ? 0 : 10;

        for (int i = index ; i < arguments.size(); i++){
            options.addArguments(arguments.get(i));
        }

        return options;
    }
}
