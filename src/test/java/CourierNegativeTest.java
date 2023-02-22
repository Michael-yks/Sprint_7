import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CourierNegativeTest {
    private CourierClient courierClient;
    private int id;

    private Courier courier;
    private int statusCode;
    private String message;

    private final static String ERROR_MESSAGE_400 = "Недостаточно данных для создания учетной записи";


    @Before
    public void setUp() {

        courierClient = new CourierClient();

    }

    public CourierNegativeTest(Courier courier, int statusCode, String message) {
        this.courier = courier;
        this.statusCode = statusCode;
        this.message = message;
    }

    @Parameterized.Parameters(name = "Тестовые данные: курьер: {0}; ожидаемый ответ сервера: {1}; ожидаемое сообщение: {2}")
    public static Object[][] getTestData() {
        return new Object[][] {
                {CourierGenerator.getWithNameOnly(),SC_BAD_REQUEST, ERROR_MESSAGE_400},
                {CourierGenerator.getWithLoginOnly(), SC_BAD_REQUEST, ERROR_MESSAGE_400},
                {CourierGenerator.getWithPasswordOnly(), SC_BAD_REQUEST, ERROR_MESSAGE_400}
        };
    }



    @Test
    @DisplayName("Create courier") // имя теста
    @Description("Basic test for post request to /api/v1/courier") // описание теста
    public void courierCreatedNegative() {

        ValidatableResponse responseCreate = courierClient.create(courier);
        int actualStatusCode = responseCreate.extract().path("code");
        String actualMessage = responseCreate.extract().path("message");
        assertEquals(statusCode,actualStatusCode);
        assertEquals(message,actualMessage);

    }
}
