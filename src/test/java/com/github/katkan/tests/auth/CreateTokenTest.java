package com.github.katkan.tests.auth;

import com.github.katkan.properties.RestfulBookerProperties;
import com.github.katkan.requests.auth.CreateTokenRequest;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.katkan.helpers.JsonHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CreateTokenTest {
    private JSONObject authData;

    @BeforeEach
    void setup(){
        authData = new JSONObject();
    }

    @Test
    @DisplayName("Create token with valid username and password")
    void createTokenWithValidUsernameAndPasswordTest() {
        authData.put(USERNAME, RestfulBookerProperties.getUsername());
        authData.put(PASSWORD, RestfulBookerProperties.getPassword());
        Response createTokenResponse = CreateTokenRequest.createTokenRequest(authData);

        assertThat(createTokenResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(createTokenResponse.jsonPath().getString(TOKEN)).isNotEmpty();
    }

    @Test
    @DisplayName("Create token with invalid username and password")
    void createTokenWithInvalidUsernameAndPasswordTest() {
        authData.put(USERNAME, "user");
        authData.put(PASSWORD, "123123");
        Response createTokenResponse = CreateTokenRequest.createTokenRequest(authData);

        assertThat(createTokenResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(createTokenResponse.jsonPath().getString("reason")).isEqualTo("Bad credentials");
    }
}
