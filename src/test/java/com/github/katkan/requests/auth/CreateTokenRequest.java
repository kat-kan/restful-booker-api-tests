package com.github.katkan.requests.auth;

import com.github.katkan.url.RestfulBookerUrls;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class CreateTokenRequest {

    public static Response createTokenRequest(JSONObject auth) {
        return given()
                .contentType(ContentType.JSON)
                .body(auth.toString())
                .when()
                .post(RestfulBookerUrls.getAuthUrl())
                .then()
                .extract()
                .response();
    }
}
