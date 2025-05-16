package br.com.vinnilmg.quarkussocial.rest;

import br.com.vinnilmg.quarkussocial.rest.request.CreateUserRequest;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.net.URL;
import java.util.List;
import java.util.Map;

import static br.com.vinnilmg.quarkussocial.rest.response.ErrorResponse.UNPROCESSABLE_ENTITY_STATUS_CODE;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static jakarta.ws.rs.core.Response.Status.CREATED;
import static jakarta.ws.rs.core.Response.Status.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Configuração de ordem de execução
class UserResourceTest {

    // URL default para os testes
    @TestHTTPResource("/users")
   private URL ENDPOINT;

    @Test
    @DisplayName("Should create an user successfully")
    @Order(1) // Primeiro a executar
    void createUserTest() {
        final var user = new CreateUserRequest("Fulano", 25);
        final var response = given()
                .contentType(JSON)
                .body(user)
                .when()
                .post(ENDPOINT)
                .then()
                .extract()
                .response();

        assertEquals(CREATED.getStatusCode(), response.statusCode());
        assertNotNull(response.jsonPath().getString("id"));
    }

    @Test
    @DisplayName("should return error when json is not valid")
    @Order(2) // Segundo a executar
    void createUserValidationErrorTest() {
        final var user = new CreateUserRequest(null, null);
        final var response = given()
                .contentType(JSON)
                .body(user)
                .when()
                .post(ENDPOINT)
                .then()
                .extract()
                .response();

        assertEquals(UNPROCESSABLE_ENTITY_STATUS_CODE, response.statusCode());
        assertEquals("Validation Error", response.jsonPath().getString("message"));

        final List<Map<String, String>> errors = response.jsonPath().getList("errors");
        assertNotNull(errors.get(0).get("message"));
        assertNotNull(errors.get(1).get("message"));
    }

    @Test
    @DisplayName("Should list all users")
    @Order(3) // Terceiro a executar
    void listAllUsersTest() {
        given()
                .contentType(JSON)
                .when()
                .get(ENDPOINT)
                .then()
                .statusCode(OK.getStatusCode())
                .body("size()", Matchers.is(1));
    }
}