package ru.gav19770210.javapro.task05.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.flywaydb.core.Flyway;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import ru.gav19770210.javapro.task05.entities.UserEntity;
import ru.gav19770210.javapro.task05.services.UserServiceImpl;

import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(PostgreSQLContainerExtension.class)
@DirtiesContext
public class UserIntegrationTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @LocalServerPort
    private Integer port;
    private UserEntity userTest;

    @BeforeEach
    void beforeEach(@Autowired Flyway flyway) {
        RestAssured.baseURI = "http://localhost:" + port;

        flyway.clean();
        flyway.migrate();

        userTest = new UserEntity();
        userTest.setName("Test_0123456789");
    }

    @AfterEach
    void afterEach(@Autowired Flyway flyway) {
        userTest = null;

        flyway.clean();
    }

    @Test
    @DisplayName("Интеграционный тест")
    public void testTest() throws JsonProcessingException {
        System.out.println("--> Получение пользователя с заданным <name>");

        RestAssured.given()
                .when()
                .log().all()
                .contentType(ContentType.JSON)
                .pathParam("name", userTest.getName())
                .get("/user/{name}/get-by-name")
                .then()
                .log().all()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", Matchers.equalTo(String.format(UserServiceImpl.msgUserNotFoundByName, userTest.getName())));

        System.out.println("--> Создание пользователя");

        UserEntity userCreate = RestAssured.given()
                .when()
                .log().all()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(userTest))
                .post("/user/create")
                .then()
                .log().all()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.OK.value())
                .body("name", Matchers.equalTo(userTest.getName()))
                .body("id", Matchers.notNullValue())
                .extract()
                .as(UserEntity.class);

        System.out.println("--> Получение пользователя с заданным <id>");

        UserEntity userGet = RestAssured.given()
                .when()
                .log().all()
                .contentType(ContentType.JSON)
                .pathParam("id", userCreate.getId())
                .get("/user/{id}/get")
                .then()
                .log().all()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.OK.value())
                .body("name", Matchers.equalToObject(userCreate.getName()))
                .body("id", Matchers.notNullValue())
                .extract()
                .as(UserEntity.class);

        Assertions.assertEquals(userCreate, userGet);

        System.out.println("--> Удаление пользователя с заданным <id>");

        RestAssured.given()
                .when()
                .log().all()
                .contentType(ContentType.JSON)
                .pathParam("id", userCreate.getId())
                .delete("/user/{id}/delete")
                .then()
                .log().all()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.OK.value());

        System.out.println("--> Получение пользователя с заданным <id>");

        RestAssured.given()
                .when()
                .log().all()
                .contentType(ContentType.JSON)
                .pathParam("id", userCreate.getId())
                .get("/user/{id}/get")
                .then()
                .log().all()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", Matchers.equalTo(String.format(UserServiceImpl.msgUserNotFoundById, userCreate.getId())));

        System.out.println("--> Получение списка пользователей");

        UserEntity[] userEntities = RestAssured.given()
                .when()
                .log().all()
                .contentType(ContentType.JSON)
                .get("/user/get-all")
                .then()
                .log().all()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(UserEntity[].class);

        System.out.println(userEntities.length);
        UserEntity userGetAny = Arrays.stream(userEntities).findAny().get();

        System.out.println("--> Обновление пользователя");

        userGetAny.setName(userTest.getName());

        UserEntity userUpdate = RestAssured.given()
                .when()
                .log().all()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(userGetAny))
                .post("/user/update")
                .then()
                .log().all()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.OK.value())
                .body("name", Matchers.equalTo(userGetAny.getName()))
                .extract()
                .as(UserEntity.class);

        Assertions.assertEquals(userUpdate, userGetAny);
    }
}
