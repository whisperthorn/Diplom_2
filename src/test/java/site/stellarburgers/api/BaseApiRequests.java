package site.stellarburgers.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BaseApiRequests {

    protected static final String ST_BURGERS_BASE_URL = "https://stellarburgers.nomoreparties.site";
    protected static final String ST_BURGERS_LOGIN = "/api/auth/login";
    protected static final String ST_BURGERS_REGISTER = "/api/auth/register";
    protected static final String ST_BURGERS_USER = "/api/auth/user";
    protected static final String ST_BURGERS_ORDERS = "/api/orders";
    protected static final String ST_BURGERS_INGREDIENTS_LIST = "/api/ingredients";

    @Step("Отправляем GET-запрос на {endpoint}")
    public Response sendGetRequest(String endpoint) {
        return given()
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    @Step("Отправляем GET-запрос на {endpoint} используя токен авторизации")
    public Response sendGetRequestWithAuth(String endpoint, String bearerToken) {
        return given()
                .header("Authorization", bearerToken)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    @Step("Отправляем POST запрос на {endpoint}")
    public Response sendPostRequest(String endpoint, Object jsonBody){
        return given()
                .header("Content-type", "application/json")
                .body(jsonBody)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
    }

    @Step("Отправляем POST запрос на {endpoint} используя токен авторизации")
    public Response sendPostRequestWithAuth(String endpoint, Object jsonBody, String bearerToken){
        return given()
                .header("Authorization", bearerToken)
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
    }

    @Step("Отправляем PATCH запрос на {endpoint} используя токен авторизации")
    public Response sendPatchRequestWithAuth(String endpoint, Object jsonBody, String bearerToken){
        return given()
                .header("Authorization", bearerToken)
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .patch(endpoint)
                .then()
                .extract()
                .response();
    }

    @Step("Отправляем PATCH запрос на {endpoint}")
    public Response sendPatchRequest(String endpoint, Object jsonBody){
        return given()
                .header("Content-type", "application/json")
                .body(jsonBody)
                .when()
                .patch(endpoint)
                .then()
                .extract()
                .response();
    }

    @Step("Отправляем DELETE запрос на {endpoint} c токеном авторизации")
    public Response sendDeleteRequest(String endpoint, String bearerToken){
        return given()
                .header("Authorization", bearerToken)
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
    }

    @Step("Проверяем, что код ответа равен {expectedStatusCode}")
    public void verifyStatusCode(Response response, int expectedStatusCode) {
        response.then()
                .statusCode(expectedStatusCode);
    }

    @Step("Проверяем, что параметр {jsonPath} имеет значение {expectedValue}")
    public void verifyResponseBodyParameter(Response response, String jsonPath,Object expectedValue){
        response.then()
                .body(jsonPath, equalTo(expectedValue));
    }
}