package site.stellarburgers.api;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import site.stellarburgers.api.models.user.authentication.LoginPojo;
import site.stellarburgers.api.models.user.authentication.LoginSuccessPojo;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static site.stellarburgers.api.BaseApiRequests.ST_BURGERS_LOGIN;

public class LoginTest extends BaseTestMethods{

    @Test
    @DisplayName("Авторизация используя верные email и password.")
    @Description("Проверяем успешную авторизацию и тело ответа")
    public void testLoginCorrectEmailPassword(){
        // Сериализуем личные данные в json
        LoginPojo authCredentials = new LoginPojo(email, password);

        //Попытка авторизации
        logIn(authCredentials);

        // Проверка кода ответа на успешную авторизацию
        api.verifyStatusCode(response,SC_OK);

        // Десериализуем тело ответа для проверки
        LoginSuccessPojo responseBody = response.as(LoginSuccessPojo.class);

        SoftAssertions softly = new SoftAssertions();

        // Проверка параметра success
        softly.assertThat(responseBody.isSuccess())
                .withFailMessage("success должен быть true")
                .isTrue();

        // Проверка наличия accessToken
        softly.assertThat(responseBody.getAccessToken())
                .withFailMessage("Access token должен присутствовать в ответе")
                .isNotBlank();

        // Проверка наличия refreshToken
        softly.assertThat(responseBody.getRefreshToken())
                .withFailMessage("Refresh token должен присутствовать в ответе")
                .isNotBlank();

        // Проверка почтового ящика пользователя
        softly.assertThat(responseBody.getUser().getEmail())
                .withFailMessage("Email должен быть %s", email)
                .isEqualTo(email);

        // Проверка имени пользователя
        softly.assertThat(responseBody.getUser().getName())
                .withFailMessage("Name должен быть %s", firstName)
                .isEqualTo(firstName);

        //Сохраняем токен для удаления аккаунта
        bearerToken = responseBody.getAccessToken();

        // Завершение проверки
        softly.assertAll();

    }

    @Test
    @DisplayName("Заходим в несуществующий аккаунт.")
    @Description("Проверяем неуспешную авторизацию используя несуществующий email.")
    public void testLoginBadEmail(){
        //Меняем email
        email = "nonexistent"+email;

        // Сериализуем личные данные в json
        LoginPojo authCredentials = new LoginPojo(email, password);

        //Попытка авторизации
        logIn(authCredentials);

        // Проверка кода ответа на неуспешную авторизацию
        api.verifyStatusCode(response,SC_UNAUTHORIZED);

        // Проверяем наличие и значение параметра success
        api.verifyResponseBodyParameter(response, "success", false);

        // Проверяем наличие и значение параметра message
        api.verifyResponseBodyParameter(response, "message", "email or password are incorrect");
    }

    @Test
    @DisplayName("Заходим в аккаунт используя верный email и неверный password.")
    @Description("Проверяем неуспешную авторизацию используя существующий email и неверный пароль")
    public void testLoginBadPassword(){
        //Меняем password
        password = "nonexistent"+password;

        // Сериализуем личные данные в json
        LoginPojo authCredentials = new LoginPojo(email, password);

        //Попытка авторизации
        logIn(authCredentials);

        // Проверка кода ответа на неуспешную авторизацию
        api.verifyStatusCode(response,SC_UNAUTHORIZED);

        // Проверяем наличие и значение параметра success
        api.verifyResponseBodyParameter(response, "success", false);

        // Проверяем наличие и значение параметра message
        api.verifyResponseBodyParameter(response, "message", "email or password are incorrect");
    }

    @Test
    @DisplayName("Заходим в аккаунт с пустыми полями email и password.")
    @Description("Проверяем неуспешную авторизацию с пустыми полями email и пароля.")
    public void testLoginEmptyEmailPassword(){
        //Получаем пустые поля
        email = "";
        password = "";

        // Сериализуем личные данные в json
        LoginPojo authCredentials = new LoginPojo(email, password);

        //Попытка авторизации
        logIn(authCredentials);

        // Проверка кода ответа на неуспешную авторизацию
        api.verifyStatusCode(response,SC_UNAUTHORIZED);

        // Проверяем наличие и значение параметра success
        api.verifyResponseBodyParameter(response, "success", false);

        // Проверяем наличие и значение параметра message
        api.verifyResponseBodyParameter(response, "message", "email or password are incorrect");
    }

    @Step("Попытка авторизации в аккаунт")
    private void logIn(LoginPojo authCredentials){
        //Отправляем запрос на авторизацию
        response = api.sendPostRequest(ST_BURGERS_LOGIN, authCredentials);
    }
}