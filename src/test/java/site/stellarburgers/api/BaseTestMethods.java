package site.stellarburgers.api;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import site.stellarburgers.api.models.create.account.CreateAccountPojo;
import site.stellarburgers.api.models.make.order.IngredientListPojo;
import site.stellarburgers.api.models.make.order.IngredientPojo;
import site.stellarburgers.api.models.make.order.MakeOrderPojo;
import java.util.ArrayList;
import java.util.List;

public class BaseTestMethods extends BaseApiRequests {
    protected String email;
    protected String password;
    protected String firstName;
    protected CreateAccountPojo credentials;
    protected Response response;
    protected String bearerToken;


    @Before
    public void setUp(){
        //Генерируем персональные данные
        generateCredentials();

        // Регистрируем нового пользователя
        createAccount(email, password, firstName);

        //Сохраняем токен авторизации
        bearerToken = response.jsonPath().getString("accessToken");
    }

    @After
    public void tearDown(){
        //Удаляем тестовый аккаунт
        deleteAccount(bearerToken);
    }

    @Step("Подготавливаем персональные данные для аккаунта")
    public void generateCredentials() {
        Faker faker = new Faker();
        email = faker.number().digits(4)+faker.internet().emailAddress();
        password = faker.internet().password(11,12,true,true,true);
        firstName = faker.name().firstName();
    }

    @Step("Регистрируем новый аккаунт")
    public void createAccount(String email, String password, String firstName){
        //Сериализуем данные в json
        credentials = new CreateAccountPojo(email, password, firstName);

        // Отправляем POST запрос на регистрацию
        response = sendPostRequest(ST_BURGERS_REGISTER, credentials);
    }

    @Step("Удаляем тестовый аккаунт")
    public void deleteAccount(String bearerToken){
        // Отправляем запрос на удаление аккаунта
        sendDeleteRequest(ST_BURGERS_USER, bearerToken);
    }

    @Step("Подготавливаем список ингредиентов для заказа")
    public List<String> getIngredients(List<String> burger){
        List<IngredientPojo> ingredients;
        List<String> main = new ArrayList<>();
        List<String> bun = new ArrayList<>();
        List<String> sauce = new ArrayList<>();

        // Отправляем запрос на получение списка ингредиентов
        response = sendGetRequest(ST_BURGERS_INGREDIENTS_LIST);

        // Десериализуем ингредиенты
        IngredientListPojo ingredientList = response.as(IngredientListPojo.class);
        ingredients = ingredientList.getData();

        // Сортируем полученные ингредиенты по типу
        for(IngredientPojo ingredient : ingredients ){
            switch (ingredient.getType()) {
                case "main":
                    main.add(ingredient.getId());
                    break;
                case "bun":
                    bun.add(ingredient.getId());
                    break;
                case "sauce":
                    sauce.add(ingredient.getId());
                    break;
                default:
                    break;
            }
        }

        //Создаем бургер с ингредиентом каждого типа
        burger.add(bun.get(0));
        burger.add(main.get(0));
        burger.add(sauce.get(0));
        return burger;
    }

    @Step("Попытка создать новый заказ используя токен авторизации")
    public void makeOrder(MakeOrderPojo burgerOrder, String bearerToken){
        response = sendPostRequestWithAuth(ST_BURGERS_ORDERS, burgerOrder, bearerToken);
    }
}
