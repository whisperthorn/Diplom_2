package site.stellarburgers.api;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import site.stellarburgers.api.models.make.order.MakeOrderPojo;
import site.stellarburgers.api.models.make.order.MakeOrderSuccessPojo;
import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;

public class MakeOrderTest extends BaseTestMethods{
    List<String> burger = new ArrayList<>();

    @Test
    @DisplayName("Создаем заказ с ингредиентами и авторизацией")
    @Description("Проверяем создание заказа с ингредиентами и передачей токена авторизации.")
    public void testMakeOrderWithAuthToken(){
        // Сериализуем набор ингредиентов
        MakeOrderPojo burgerOrder = new MakeOrderPojo(getIngredients(burger));

        // Создаем заказ используя токен
        makeOrder(burgerOrder, bearerToken);

        // Проверка кода ответа на успешное создание заказа
        verifyStatusCode(response,SC_OK);

        //Десериализуем тело ответа для проверки
        MakeOrderSuccessPojo responseBody = response.as(MakeOrderSuccessPojo.class);

        SoftAssertions softly = new SoftAssertions();

        // Проверка параметра success
        softly.assertThat(responseBody.isSuccess())
                .withFailMessage("success должен быть true")
                .isTrue();

        // Проверка параметра name
        softly.assertThat(responseBody.getName())
                .withFailMessage("Name должен присутствовать в ответе")
                .isNotBlank();

        // Проверка параметра number
        softly.assertThat(responseBody.getOrder().getNumber())
                .withFailMessage("Order number должен присутствовать в ответе")
                .isNotNull();

        // Завершение проверки
        softly.assertAll();
    }

    @Test
    @DisplayName("Создаем заказ с ингредиентами без авторизациии")
    @Description("Проверяем создание заказа с ингредиентами без передачи токена авторизации.")
    public void testMakeOrderNoAuthToken(){
        //Сериализуем набор ингредиентов
        MakeOrderPojo burgerOrder = new MakeOrderPojo(getIngredients(burger));

        //Создаем заказ без использования токена
        makeOrder(burgerOrder);

        // Проверка кода ответа на создание заказа без токена авторизации
        verifyStatusCode(response,SC_OK);
    }

    @Test
    @DisplayName("Создаем заказ без ингредиентов и с авторизацией")
    @Description("Проверяем создание заказа без ингредиентов и передачи токена авторизации.")
    public void testMakeOrderNoIngredient(){
        //Сериализуем набор ингредиентов c пустым списком
        MakeOrderPojo burgerOrder = new MakeOrderPojo(burger);

        // Создаем заказ используя токен авторизации
        makeOrder(burgerOrder, bearerToken);

        // Проверка кода ответа на неуспешное создание заказа
        verifyStatusCode(response,SC_BAD_REQUEST);

        // Проверяем наличие и значение параметра success
        verifyResponseBodyParameter(response, "success", false);

        // Проверяем наличие и значение параметра message
        verifyResponseBodyParameter(response, "message", "Ingredient ids must be provided");
    }

    @Test
    @DisplayName("Создаем заказ с неверным хэшем ингредиентов и с авторизацией")
    @Description("Проверяем создание заказа с невалидным хэшем ингредиента и передачей токена авторизации.")
    public void testMakeOrderBadHash(){
        //Добавляем невалидный хэш в список ингредиентов
        burger.add("1a2s3d4f5g6h7j8k9l0a123");

        //Сериализуем набор ингредиентов c пустым списком
        MakeOrderPojo burgerOrder = new MakeOrderPojo(burger);

        // Создаем заказ используя токен авторизации
        makeOrder(burgerOrder, bearerToken);

        // Проверка кода ответа на неуспешное создание заказа
        verifyStatusCode(response,SC_INTERNAL_SERVER_ERROR);
    }

    @Step("Попытка создать новый заказ без токена авторизации")
    private void makeOrder(MakeOrderPojo burgerOrder){
        response = sendPostRequest(ST_BURGERS_ORDERS, burgerOrder);
    }
}