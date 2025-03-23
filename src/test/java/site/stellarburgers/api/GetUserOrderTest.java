package site.stellarburgers.api;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import site.stellarburgers.api.models.make.order.MakeOrderPojo;
import site.stellarburgers.api.models.user.order.OrderResponsePojo;
import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class GetUserOrderTest extends BaseTestMethods{

    @Before
    public void setOrderUp(){
        makeOrder();
    }

    @Test
    @DisplayName("Получение заказов через токен авторизации пользователя.")
    @Description("Проверяем .")
    public void testGetOrderWithAuthToken(){
        //Отправляем запрос на получение списка заказов
        getOrders(bearerToken);

        // Проверка кода ответа на успешное получение заказов
        verifyStatusCode(response,SC_OK);

        //Десериализуем ответ
        OrderResponsePojo orderList = response.as(OrderResponsePojo.class);

        SoftAssertions softly = new SoftAssertions();

        // Проверка параметра success
        softly.assertThat(orderList.isSuccess())
                .withFailMessage("success должен быть true")
                .isTrue();

        // Проверка наличия orders
        softly.assertThat(orderList.getOrders())
                .withFailMessage("orders должны присутствовать в ответе")
                .isNotNull();

        // Проверка наличия списка orders
        softly.assertThat(orderList.getOrders())
                .withFailMessage("orders должны присутствовать в ответе")
                .isNotNull();

        // Проверка наличия total
        softly.assertThat(orderList.getTotal())
                .withFailMessage("total должен присутствовать в ответе")
                .isNotNull();

        // Проверка наличия totalToday
        softly.assertThat(orderList.getTotalToday())
                .withFailMessage("totalToday должен присутствовать в ответе")
                .isNotNull();

        // Завершение проверки
        softly.assertAll();

    }

    @Test
    @DisplayName("Получение заказов без токена авторизации.")
    @Description("Проверяем .")
    public void testGetOrderNoAuthToken(){
        //Отправляем запрос на получение списка заказов
        getOrders();

        // Проверка кода ответа на неуспешную авторизацию
        verifyStatusCode(response,SC_UNAUTHORIZED);

        // Проверяем наличие и значение параметра success
        verifyResponseBodyParameter(response, "success", false);

        // Проверяем наличие и значение параметра message
        verifyResponseBodyParameter(response, "message", "You should be authorised");


    }

    @Step("Создаем заказ")
    private void makeOrder(){
        List<String> burger = new ArrayList<>();

        // Сериализуем набор ингредиентов
        MakeOrderPojo burgerOrder = new MakeOrderPojo(getIngredients(burger));

        // Создаем заказ используя токен
        makeOrder(burgerOrder, bearerToken);
    }

    @Step("Получаем список заказов используя токен авторизации")
    private void getOrders(String bearerToken){
        response = sendGetRequestWithAuth(ST_BURGERS_ORDERS, bearerToken);
    }

    @Step("Получаем список заказов без использования токена авторизации")
    private void getOrders(){
        response = sendGetRequest(ST_BURGERS_ORDERS);
    }

}