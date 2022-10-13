package com.github.katkan.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class HealthCheckTest {

    @Test
    @DisplayName("HealthCheck - verify whether API is up and running")
    void ping() {

        given()
                .when()
                .get("https://restful-booker.herokuapp.com/ping")
                .then()
                .statusCode(201);
    }
}
