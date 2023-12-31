package ru.netology.patterns.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;
import org.junit.jupiter.api.BeforeAll;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON) // тип отправляемого и принимаего контента
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL) // уровень логирования
            .build();

    private static final Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }

    private static RegistrationDto sendRequest(RegistrationDto user) { // принимаем метод на вход параметром
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users") // на какой путь относительно BaseUri отправляем запрос
                .then()
                .statusCode(200);
        return user; // возвращаем зарегистрированного пользователя
    }

    public static String getRandomLogin() {
        return faker.name().username();
    }

    public static String getRandomPassword() {
        return faker.internet().password();
    }

    public static class Registration { // вложенный статичный класс для генерации случайного и зарегистрированного пользователя
        private Registration() {
        }

        public static RegistrationDto getUser(String status) {
            return new RegistrationDto(getRandomLogin(), getRandomPassword(), status);
        }

        public static RegistrationDto getRegisteredUser(String status) {
            return sendRequest(getUser(status));
        }
    }

    @Value
    public static class RegistrationDto {
        String login; // структура полей соответствует структуре запроса
        String password;
        String status;
    }
}
