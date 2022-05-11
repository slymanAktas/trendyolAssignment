package models.services;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class BookService {

    public ValidatableResponse getBooks() {
        ValidatableResponse response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then();
        response.extract().response().prettyPrint();
        return response;
    }

    public ValidatableResponse getBook(int id) {
        ValidatableResponse response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .get(id + "/")
                .then();
        response.extract().response().prettyPrint();
        return response;
    }

    public ValidatableResponse insertBook(String author, String title) {
        ValidatableResponse response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(getRequestBody(author, title))
                .when()
                .put()
                .then();
        response.extract().response().prettyPrint();
        return response;
    }

    public ValidatableResponse insertBookWithId(int id, String author, String title) {
        HashMap<String, Object> requestBody = getRequestBody(author, title);
        requestBody.put("id", id);

        ValidatableResponse response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put()
                .then();
        response.extract().response().prettyPrint();
        return response;
    }

    private static HashMap<String, Object> getRequestBody(String author, String title) {
        HashMap<String, Object> requestParams = new HashMap<>();
        requestParams.put("author", author);
        requestParams.put("title", title);
        return requestParams;
    }
}
