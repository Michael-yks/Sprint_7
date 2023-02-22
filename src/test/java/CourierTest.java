import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class CourierTest {
    private CourierClient courierClient;
    private Courier courier;
    private int id;
    private final static String ERROR_MESSAGE_409 = "Этот логин уже используется. Попробуйте другой.";
    private final static String ERROR_MESSAGE_400 = "Учетная запись не найдена";
    private final static String ERROR_MESSAGE_400_REQUIRED = "Недостаточно данных для входа";

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getDefault();
    }

    @After
    public void cleanUp() {
        ValidatableResponse responseDelete = courierClient.delete(id);
    }

    @Test
    @DisplayName("Create courier") // имя теста
    @Description("Basic test for post request to /api/v1/courier") // описание теста
    public void courierCreatedPositive() {
        ValidatableResponse responseCreate = courierClient.create(courier);
        ValidatableResponse responseLogin = courierClient.login(Credential.from(courier));
        int actualStatusCode = responseCreate.extract().statusCode();
        id = responseLogin.extract().path("id");
        Boolean isCourierCreated = responseCreate.extract().path("ok");
        assertEquals(SC_CREATED,actualStatusCode);
        assertTrue(isCourierCreated);
    }

    @Test
    @DisplayName("Create courier with the same credentials") // имя теста
    @Description("Basic test for post request to /api/v1/courier") // описание теста
    public void sameCourierCreatedNegative() {
        courierClient.create(courier);
        ValidatableResponse  responseLogin = courierClient.login(Credential.from(courier));
        id = responseLogin.extract().path("id");
        ValidatableResponse responseCreate = courierClient.create(courier);
        int statusCode = responseCreate.extract().path("code");
        String message = responseCreate.extract().path("message");
        assertEquals(SC_CONFLICT,statusCode);
        assertEquals(ERROR_MESSAGE_409,message);
    }

    @Test
    @DisplayName("Login courier") // имя теста
    @Description("Basic test for post request to /api/v1/courier/login") // описание теста
    public void courierLoginPositive() {
        courierClient.create(courier);
        ValidatableResponse responseLogin = courierClient.login(Credential.from(courier));
        int actualStatusCode = responseLogin.extract().statusCode();
        id = responseLogin.extract().path("id");
        assertEquals(SC_OK,actualStatusCode);
        assertNotNull(id);
    }

    @Test
    @DisplayName("Login courier with wrong login") // имя теста
    @Description("Basic negative test for post request to /api/v1/courier/login") // описание теста
    public void courierWrongLogin() {
        courierClient.create(courier);
        ValidatableResponse responseCorrectLogin = courierClient.login(Credential.from(courier));
        id = responseCorrectLogin.extract().path("id");
        courier.setLogin("1");
        ValidatableResponse responseWrongLogin = courierClient.login(Credential.from(courier));
        int actualStatusCode = responseWrongLogin.extract().statusCode();
        assertEquals(SC_NOT_FOUND, actualStatusCode);
        String actualMessage = responseWrongLogin.extract().path("message");
        assertEquals(ERROR_MESSAGE_400, actualMessage);
    }

    @Test
    @DisplayName("Login courier with wrong password") // имя теста
    @Description("Basic negative test for post request to /api/v1/courier/login") // описание теста
    public void courierWrongPassword() {
        courierClient.create(courier);
        ValidatableResponse responseCorrectLogin = courierClient.login(Credential.from(courier));
        id = responseCorrectLogin.extract().path("id");
        courier.setPassword("1");
        ValidatableResponse responseWrongLogin = courierClient.login(Credential.from(courier));
        int actualStatusCode = responseWrongLogin.extract().statusCode();
        assertEquals(SC_NOT_FOUND, actualStatusCode);
        String actualMessage = responseWrongLogin.extract().path("message");
        assertEquals(ERROR_MESSAGE_400, actualMessage);
    }

    @Test
    @DisplayName("Login courier with required field login") // имя теста
    @Description("Basic negative test for post request to /api/v1/courier/login") // описание теста
    public void courierLoginWithoutRequiredFieldLogin() {
        courierClient.create(courier);
        ValidatableResponse responseCorrectLogin = courierClient.login(Credential.from(courier));
        id = responseCorrectLogin.extract().path("id");
        courier.setLogin(null);
        ValidatableResponse responseWrongLogin = courierClient.login(Credential.from(courier));
        int actualStatusCode = responseWrongLogin.extract().statusCode();
        assertEquals(SC_BAD_REQUEST, actualStatusCode);
        String actualMessage = responseWrongLogin.extract().path("message");
        assertEquals(ERROR_MESSAGE_400_REQUIRED, actualMessage);
    }
}
