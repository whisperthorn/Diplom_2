package site.stellarburgers.api;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Test;
import site.stellarburgers.api.models.user.change.info.ChangeUserInfoPojo;
import site.stellarburgers.api.models.user.change.info.ChangeUserInfoSuccessPojo;

import static org.apache.http.HttpStatus.*;
import static site.stellarburgers.api.BaseApiRequests.ST_BURGERS_USER;

public class ChangeUserInfoTest extends BaseTestMethods{
    private String bearerTokenTest = "";

    @Test
    @DisplayName("Изменение данных в поле email с авторизацией.")
    @Description("Проверяем, что данные в поле email изменяются отправляя запрос используя токен авторизации.")
    public void testChangeEmailWithAuthToken(){
        //Готовим новый email
        email = "new" + email;

        //Сериализуем данные для отправки в запросе
        ChangeUserInfoPojo changeCredentials = new ChangeUserInfoPojo(email, firstName);

        //Обновляем данные пользователя
        changeUserInfo(changeCredentials, bearerToken);

        // Проверка кода ответа на успешное изменение данных
        api.verifyStatusCode(response,SC_OK);

        // Десериализуем тело ответа для проверки
        ChangeUserInfoSuccessPojo responseBody = response.as(ChangeUserInfoSuccessPojo.class);

        SoftAssertions softly = new SoftAssertions();

        // Проверка параметра success
        softly.assertThat(responseBody.isSuccess())
                .withFailMessage("success должен быть true")
                .isTrue();

        // Проверка почтового ящика пользователя
        softly.assertThat(responseBody.getUser().getEmail())
                .withFailMessage("Email должен быть %s", email)
                .isEqualTo(email);

        // Проверка имени пользователя
        softly.assertThat(responseBody.getUser().getName())
                .withFailMessage("Name должен быть %s", firstName)
                .isEqualTo(firstName);
    }

    @Test
    @DisplayName("Изменение данных в поле name с авторизацией.")
    @Description("Проверяем, что данные в поле name изменяются отправляя запрос используя токен авторизации.")
    public void testChangeNameWithAuthToken(){
        //Готовим новый name
        firstName = "new" + firstName;

        //Сериализуем данные для отправки в запросе
        ChangeUserInfoPojo changeCredentials = new ChangeUserInfoPojo(email, firstName);

        //Обновляем данные пользователя
        changeUserInfo(changeCredentials, bearerToken);

        // Проверка кода ответа на успешное изменение данных
        api.verifyStatusCode(response,SC_OK);

        // Десериализуем тело ответа для проверки
        ChangeUserInfoSuccessPojo responseBody = response.as(ChangeUserInfoSuccessPojo.class);

        SoftAssertions softly = new SoftAssertions();

        // Проверка параметра success
        softly.assertThat(responseBody.isSuccess())
                .withFailMessage("success должен быть true")
                .isTrue();

        // Проверка почтового ящика пользователя
        softly.assertThat(responseBody.getUser().getEmail())
                .withFailMessage("Email должен быть %s", email)
                .isEqualTo(email);

        // Проверка имени пользователя
        softly.assertThat(responseBody.getUser().getName())
                .withFailMessage("Name должен быть %s", firstName)
                .isEqualTo(firstName);

    }

    @Test
    @DisplayName("Изменение данных в поле email на существующий в базе адрес с авторизацией.")
    @Description("Проверяем, что система возвращает ошибку при попытке изменить email на существующий в базе адрес " +
                "используя токен авторизации.")
    public void testChangeToExistingEmail(){
        //Создаем второй тестовый аккаунт
        String newEmail = "new" + email;
        createAccount(newEmail, password, firstName);

        //Сохраняем токен второго аккаунта для последующего удаления
        bearerTokenTest = response.jsonPath().getString("accessToken");

        //Сериализуем данные для отправки в запросе используя email второго аккаунта
        ChangeUserInfoPojo changeCredentials = new ChangeUserInfoPojo(newEmail, firstName);

        //Обновляем данные пользователя
        changeUserInfo(changeCredentials, bearerToken);

        // Проверка кода ответа на неуспешное изменение данных
        api.verifyStatusCode(response,SC_FORBIDDEN);

        // Проверяем наличие и значение параметра success
        api.verifyResponseBodyParameter(response, "success", false);

        // Проверяем наличие и значение параметра message
        api.verifyResponseBodyParameter(response, "message", "User with such email already exists");
    }

    @Test
    @DisplayName("Изменение данных в поле email без авторизации.")
    @Description("Проверяем, что система возвращает ошибку при попытке изменить данные в поле email без токена авторизации.")
    public void testChangeUnauthorizedUserEmail(){
        //Готовим новый email
        email = "new" + email;

        //Сериализуем данные для отправки в запросе
        ChangeUserInfoPojo changeCredentials = new ChangeUserInfoPojo(email, firstName);

        //Обновляем данные пользователя
        changeUserInfo(changeCredentials);

        // Проверка кода ответа на неуспешное изменение данных
        api.verifyStatusCode(response,SC_UNAUTHORIZED);

        // Проверяем наличие и значение параметра success
        api.verifyResponseBodyParameter(response, "success", false);

        // Проверяем наличие и значение параметра message
        api.verifyResponseBodyParameter(response, "message", "You should be authorised");
    }

    @Test
    @DisplayName("Изменение данных в поле name без авторизацией.")
    @Description("Проверяем, что система возвращает ошибку при попытке изменить данные в поле name без токена авторизации.")
    public void testChangeUnauthorizedUserName(){
        //Готовим новый name
        firstName = "new" + firstName;

        //Сериализуем данные для отправки в запросе
        ChangeUserInfoPojo changeCredentials = new ChangeUserInfoPojo(email, firstName);

        //Обновляем данные пользователя
        changeUserInfo(changeCredentials);

        // Проверка кода ответа на неуспешное изменение данных
        api.verifyStatusCode(response,SC_UNAUTHORIZED);

        // Проверяем наличие и значение параметра success
        api.verifyResponseBodyParameter(response, "success", false);

        // Проверяем наличие и значение параметра message
        api.verifyResponseBodyParameter(response, "message", "You should be authorised");
    }

    @Step("Попытка обновить данные пользователя используя токен авторизации")
    private void changeUserInfo(ChangeUserInfoPojo changeCredentials, String bearerToken){
        response = api.sendPatchRequestWithAuth(ST_BURGERS_USER, changeCredentials, bearerToken);
    }

    @Step("Попытка обновить данные пользователя без токена авторизации")
    private void changeUserInfo(ChangeUserInfoPojo changeCredentials){
        response = api.sendPatchRequest(ST_BURGERS_USER, changeCredentials);
    }

    @After
    public void tearDown(){
        //Запускаем tearDown() BaseTestMethods
        super.tearDown();

        //Удаляем второй тестовый аккаунт
        deleteAccount(bearerTokenTest);
    }
}