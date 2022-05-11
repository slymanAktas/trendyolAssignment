package models.pages;

import config.Config;
import models.popups.HomePagePopup;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.core.har.HarEntry;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static models.browsers.chrome.ChromeWithProxy.getProxy;

public class HomePage extends Page {
    private static final String PAGE_URL = Config.TRENDYOL_BASE_HTTPS_URL;
    private static final By footer = By.tagName("footer");
    private static final By loader = By.cssSelector("div.loader");
    private static final By allBoutiqueLinks = By.cssSelector("div.component-list article a");

    public HomePage() {
        this.setUrl(PAGE_URL);
    }

    private void scrollAtTheEndOfThePage() {
        int counter = 0; // In case of endless loop
        int updatedBoutiqueCount = 0;
        int currentBoutiqueCount = browser.findElements(allBoutiqueLinks).size();
        while (counter < 150 && currentBoutiqueCount != updatedBoutiqueCount) {
            counter++;
            scrollPageUntilFooter5Times();
            currentBoutiqueCount = updatedBoutiqueCount;
            updatedBoutiqueCount = browser.findElements(allBoutiqueLinks).size();

        }
    }

    private void scrollPageUntilFooter5Times(){
        for (int i = 0; i < 5; i++) {
            browser.scrollUntilElement(footer);
            browser.waitMs(200);
        }
    }

    public List<String> getAllBoutiqueLinks() {
        scrollAtTheEndOfThePage();

        return browser.findElements(allBoutiqueLinks)
                .stream()
                .parallel()
                .map(p -> p.getAttribute("href"))
                .collect(Collectors.toList());
    }

    public HomePage closeHomePagePopupIfExist() {
        HomePagePopup homePagePopup = new HomePagePopup(this);
        if (homePagePopup.isDisplayed(5)) homePagePopup.clickX();
        return this;
    }

    public List<HarEntry> getNetworkActivities() {
        BrowserMobProxy proxy = getProxy();
        proxy.newHar();

        scrollAtTheEndOfThePage();

        List<HarEntry> entries = proxy.getHar().getLog().getEntries();
        proxy.stop();
        return entries;
    }

    public List<Map<String, String>> filterBoutiqueImageRequestFrom(List<HarEntry> allActivities) {
        List<Map<String, String>> boutiquesImageRequests = new ArrayList<>();

        for (HarEntry entry : allActivities) {
            if (entry.getRequest().getUrl().startsWith("https://cdn.dsmcdn.com")) {
                Map<String, String> eachBoutiqueImage = new HashMap<>();
                eachBoutiqueImage.put("URL", entry.getRequest().getUrl());
                eachBoutiqueImage.put("Response Code", String.valueOf(entry.getResponse().getStatus()));
                eachBoutiqueImage.put("Loading Time", String.valueOf(entry.getStartedDateTime().getTime() + entry.getTime()));

                boutiquesImageRequests.add(eachBoutiqueImage);
            }
        }

        return boutiquesImageRequests;
    }

    public static Map<String, String> extractDesiredFields(HarEntry entry) {
        Map<String, String> eachBoutiqueImage = new HashMap<>();
        eachBoutiqueImage.put("URL", entry.getRequest().getUrl());
        eachBoutiqueImage.put("Response Code", String.valueOf(entry.getResponse().getStatus()));
        eachBoutiqueImage.put("Loading Time", String.valueOf(entry.getStartedDateTime().getTime() + entry.getTime()));

        return eachBoutiqueImage;
    }

}
