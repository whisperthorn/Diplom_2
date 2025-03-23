package site.stellarburgers.api;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import site.stellarburgers.api.models.create.account.CreateAccountPojo;
import site.stellarburgers.api.models.create.account.CreateAccountSuccessPojo;

import static org.apache.http.HttpStatus.*;

public class CreateAccountTest extends BaseTestMethods {

    @Override
    @Before
    public void setUp(){
        generateCredentials();
        bearerToken = "";
    }

    @Test
    @DisplayName("Регистрация уникального пользователя.")
    @Description("Проверяем успешную регистрацию нового аккаунта и тело ответа")
    public void testCreateUniqueAccount(){
        // Сериализуем личные данные в json
        credentials = new CreateAccountPojo(email, password, firstName);

        // Отправляем POST-запрос для регистрации пользователя
        response = sendPostRequest(ST_BURGERS_REGISTER, credentials);

        // Проверка кода ответа на успешную регистрацию
        verifyStatusCode(response,SC_OK);

        // Десериализуем тело ответа для проверки
        CreateAccountSuccessPojo responseBody = response.as(CreateAccountSuccessPojo.class);

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

        // Проверка наличия accessToken
        softly.assertThat(responseBody.getAccessToken())
                .withFailMessage("Access token должен присутствовать в ответе")
                .isNotBlank();

        // Проверка наличия refreshToken
        softly.assertThat(responseBody.getRefreshToken())
                .withFailMessage("Refresh token должен присутствовать в ответе")
                .isNotBlank();

        //Сохраняем токен для удаления аккаунта
        bearerToken = responseBody.getAccessToken();

        // Завершение проверки
        softly.assertAll();
    }

    @Test
    @DisplayName("Повторная регистрация с одинаковыми данными.")
    @Description("Проверяем, что регистрация с использованием данных существующего аккаунта не осуществляется.")
    public void testCreateNonUniqueAccount(){
        // Сериализуем личные данные в json
        credentials = new CreateAccountPojo(email, password, firstName);

        // Отправляем POST-запрос для регистрации пользователя
        response = sendPostRequest(ST_BURGERS_REGISTER, credentials);

        //Сохраняем токен для удаления аккаунта
        bearerToken = response.jsonPath().getString("accessToken");

        // Отправляем второй POST-запрос используя идентичные данные
        response = sendPostRequest(ST_BURGERS_REGISTER, credentials);

        // Проверка кода ответа на неудачную регистрацию
        verifyStatusCode(response,SC_FORBIDDEN);

        // Проверяем наличие и значение параметра success
        verifyResponseBodyParameter(response, "success", false);

        // Проверяем наличие и значение параметра message
        verifyResponseBodyParameter(response, "message", "User already exists");
    }

    @Test
    @DisplayName("Создание пользователя с пустым полем email.")
    @Description("Проверяем, что регистрация с пустым полем email не осуществляется.")
    public void testCreateAccountEmptyEmail(){
        // Очищаем поле email
        email = "";

        // Сериализуем личные данные в json
        credentials = new CreateAccountPojo(email, password, firstName);

        // Отправляем POST-запрос для регистрации пользователя
        response = sendPostRequest(ST_BURGERS_REGISTER, credentials);

        // Проверка кода ответа на неудачную регистрацию
        verifyStatusCode(response,SC_FORBIDDEN);

        // Проверяем наличие и значение параметра success
        verifyResponseBodyParameter(response, "success", false);

        // Проверяем наличие и значение параметра message
        verifyResponseBodyParameter(response, "message",
                "Email, password and name are required fields");
    }

    @Test
    @DisplayName("Создание пользователя с пустым полем password.")
    @Description("Проверяем, что регистрация с пустым полем password не осуществляется.")
    public void testCreateAccountEmptyPassword(){
        // Очищаем поле password
        password = "";

        // Сериализуем личные данные в json
        credentials = new CreateAccountPojo(email, password, firstName);

        // Отправляем POST-запрос для регистрации пользователя
        response = sendPostRequest(ST_BURGERS_REGISTER, credentials);

        // Проверка кода ответа на неудачную регистрацию
        verifyStatusCode(response,SC_FORBIDDEN);

        // Проверяем наличие и значение параметра success
        verifyResponseBodyParameter(response, "success", false);

        // Проверяем наличие и значение параметра message
        verifyResponseBodyParameter(response, "message",
                "Email, password and name are required fields");
    }

    @Test
    @DisplayName("Создание пользователя с пустым полем name.")
    @Description("Проверяем, что регистрация с пустым полем name не осуществляется.")
    public void testCreateAccountEmptyName(){
        // Очищаем поле name
        firstName = "";

        // Сериализуем личные данные в json
        credentials = new CreateAccountPojo(email, password, firstName);

        // Отправляем POST-запрос для регистрации пользователя
        response = sendPostRequest(ST_BURGERS_REGISTER, credentials);

        // Проверка кода ответа на неудачную регистрацию
        verifyStatusCode(response,SC_FORBIDDEN);

        // Проверяем наличие и значение параметра success
        verifyResponseBodyParameter(response, "success", false);

        // Проверяем наличие и значение параметра message
        verifyResponseBodyParameter(response, "message",
                "Email, password and name are required fields");

    }
}
