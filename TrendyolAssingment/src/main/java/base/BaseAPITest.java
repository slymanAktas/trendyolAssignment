package base;

import io.restassured.RestAssured;
import org.junit.Rule;
import org.junit.rules.TestRule;
import models.services.BookService;

import static config.Config.API_BASE_PATH;
import static config.Config.API_ROOT;

public class BaseAPITest {
    @Rule
    public TestRule rule = new WireMockWatcher();

    public BookService bookService = new BookService();

    public BaseAPITest() {
        RestAssured.baseURI = API_ROOT;
        RestAssured.basePath = API_BASE_PATH;
    }
}
