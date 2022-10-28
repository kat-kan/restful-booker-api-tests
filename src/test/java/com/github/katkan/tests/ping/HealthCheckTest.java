package com.github.katkan.tests.ping;

import com.github.katkan.requests.BaseRequest;
import com.github.katkan.url.RestfulBookerUrls;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class HealthCheckTest {

    @Test
    @DisplayName("HealthCheck - verify whether API is up and running")
    void pingTest() {
        given()
                .spec(BaseRequest.setUp())
                .when()
                .get(RestfulBookerUrls.getHealthCheckUrl())
                .then()
                .statusCode(HttpStatus.SC_CREATED);
    }
}
