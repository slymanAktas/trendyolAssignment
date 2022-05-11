package ui;

import base.BaseUITest;
import models.visitors.buyer.Buyer;
import models.visitors.buyer.BuyerPool;
import org.junit.Test;
import models.pages.HomePage;
import utils.CsvUtils;
import utils.RequestUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;

public class BoutiqueTest extends BaseUITest {

    @Test
    public void shouldWriteBoutiqueLinksToCsv() {
        HomePage homePage = new HomePage();
        Buyer buyer = BuyerPool.anonymous().open(homePage);
        homePage = ((HomePage) buyer.nowLookingAt()).closeHomePagePopupIfExist();

        List<Map<String, Integer>> responseList = homePage.getAllBoutiqueLinks()
                .stream()
                .parallel()
                .map(RequestUtils::getStatusCode)
                .collect(Collectors.toList());

        CsvUtils.writeResponseCodesToCsv(responseList);

        File boutiquesCsv = new File("boutiquesResponseCodes.csv");
        assertThat("When buyer writes boutique response time detail to csv", boutiquesCsv.exists());
    }

    @Test
    public void shouldWriteBoutiquesLoadingTimeToCvs() {
        HomePage homePage = new HomePage();
        Buyer buyer = BuyerPool.anonymous().open(homePage, "chromeWithProxy");
        homePage = ((HomePage) buyer.nowLookingAt()).closeHomePagePopupIfExist();

        List<Map<String, String>> boutiquesImageRequests = homePage.getNetworkActivities()
                .stream()
                .parallel()
                .filter(entry -> entry.getRequest().getUrl().startsWith("https://cdn.dsmcdn.com")) // Filter just boutique image loading request
                .map(HomePage::extractDesiredFields) // Extract url, response code and response time from other fields
                .collect(Collectors.toList());

        CsvUtils.writeResponseTimesToCsv(boutiquesImageRequests);

        File boutiquesCsv = new File("boutiqueImagesResponseTime.csv");
        assertThat("When buyer writes boutique images loading time detail to csv", boutiquesCsv.exists());
    }
}
