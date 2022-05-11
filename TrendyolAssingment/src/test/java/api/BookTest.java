package api;

import base.BaseAPITest;
import io.restassured.response.ValidatableResponse;
import net.minidev.json.JSONObject;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class BookTest extends BaseAPITest {

    @Test
    public void shouldNoBooksStoredOnTheServerAtTheBeginningOfTest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "There are no books in store");

        stubFor(get(urlEqualTo("/api/books/")).inScenario("There are no books in server")
                .willReturn(aResponse()
                        .withStatus(404)
                        .withStatusMessage("Everything was just fine!")
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonObject.toString())));

        ValidatableResponse response = bookService.getBooks();

        assertThat("When user hit get request to books at the first time", response.extract().response().statusCode(), is(equalTo(404)));
        assertThat("When user hit get request to books at the first time", response.extract().path("message"), is(equalTo("There are no books in store")));
    }

    @Test
    public void shouldNotCreateBookWithEmptyRequiredField() {
        stubFor(put(urlEqualTo("/api/books/")).inScenario("Put a book via API")
                .withRequestBody(equalToJson("{\"author\":\"\" , \"title\":\"SRE 102\" }"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(400)
                        .withBody("{\"errorText\": \"author is required\"}")));

        ValidatableResponse response = bookService.insertBook("", "SRE 102");

        assertThat("User could add book without required fileds...", response.extract().response().statusCode(), is(equalTo(400)));
        assertThat("User could add book without required fileds...", response.extract().path("errorText"), is(equalTo("author is required")));
    }

    @Test
    public void shouldBookIdReadOnly() {
        stubFor(put(urlEqualTo("/api/books/")).inScenario("Cannot put a book via API")
                .withRequestBody(equalToJson("{ \"id\":1,\"author\": \"John Smith\", \"title\": \"SRE 101\"}"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(400)
                        .withBody("{ \"error\":\"field is read-only\"}")));

        ValidatableResponse response = bookService.insertBookWithId(1, "John Smith", "SRE 101");

        assertThat("User could add book with id...", response.extract().response().statusCode(), is(equalTo(400)));
        assertThat("User could add book with id...", response.extract().path("error"), is(equalTo("field is read-only")));
    }

    @Test
    public void shouldCreateBook() {
        stubFor(put(urlEqualTo("/api/books/")).inScenario("Put a book via API")
                .withRequestBody(equalToJson("{\"author\": \"John Smith\", \"title\": \"SRE 101\"}"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("{ \"id\":\"1\",\"author\": \"John Smith\", \"title\": \"SRE 101\"}")));

        String author = "John Smith";
        String title = "SRE 101";
        ValidatableResponse response = bookService.insertBook(author, title);

        assertThat("User could not add book with proper fileds...", response.extract().response().statusCode(), is(equalTo(200)));
        assertThat("User could not add book with proper fileds...", response.extract().path("author"), is(equalTo(author)));
        assertThat("User could not add book with proper fileds...", response.extract().path("title"), is(equalTo(title)));

        stubFor(get(urlEqualTo("/api/books/1/")).inScenario("There are some books")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("{ \"id\":\"1\",\"author\": \"John Smith\", \"title\": \"SRE 101\"}")));

        int id = Integer.parseInt(response.extract().path("id"));
        response = bookService.getBook(id);
        assertThat("User could not find created book with id...", response.extract().path("author"), is(equalTo(author)));
        assertThat("User could not find created book with id...", response.extract().path("title"), is(equalTo(title)));
    }

    @Test
    public void shouldNotCreateDuplicateBook() {
        stubFor(get(urlEqualTo("/api/books/")).inScenario("Put a book via API")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("{ \"id\":\"1\",\"author\": \"John Smith\", \"title\": \"SRE 101\"}")));

        ValidatableResponse response = bookService.getBooks();
        String duplicateAuthor = response.extract().path("author");
        String duplicateTitle = response.extract().path("title");

        stubFor(put(urlEqualTo("/api/books/")).inScenario("Put a book via API")
                .withRequestBody(equalToJson("{\"author\": \"John Smith\", \"title\": \"SRE 101\"}"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(400)
                        .withBody("{ \"error\":\"Another book with similar title and author already exist.\"}")));

        response = bookService.insertBook(duplicateAuthor, duplicateTitle);
        assertThat("User could add book with duplicate author and title...", response.extract().response().statusCode(), is(equalTo(400)));
        assertThat("User could add book with duplicate author and title...", response.extract().path("error"), is(equalTo("Another book with similar title and author already exist.")));
    }
}
