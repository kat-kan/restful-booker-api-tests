package com.github.katkan.requests.auth;

import com.github.katkan.requests.BaseRequest;
import com.github.katkan.url.RestfulBookerUrls;
import io.restassured.response.Response;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class CreateTokenRequest {

    public static Response createTokenRequest(JSONObject authData) {
        return given()
                .spec(BaseRequest.setUp())
                .body(authData.toString())
                .when()
                .post(RestfulBookerUrls.getAuthUrl())
                .then()
                .extract()
                .response();
    }
}
