package com.github.katkan.requests;

import com.github.katkan.url.RestfulBookerUrls;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseRequest {

    public static RequestSpecification setUp(){
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri(RestfulBookerUrls.BASE_URL);
        requestSpecBuilder.setContentType(ContentType.JSON);
        requestSpecBuilder.addFilter(new AllureRestAssured());

        return requestSpecBuilder.build();
    }
}
