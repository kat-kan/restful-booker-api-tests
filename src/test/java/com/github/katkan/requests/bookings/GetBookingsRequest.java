package com.github.katkan.requests.bookings;

import com.github.katkan.url.RestfulBookerUrls;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class GetBookingsRequest {

    public static Response getBookingRequest(){
        return given()
                .when()
                .get(RestfulBookerUrls.getBookingsUrl())
                .then()
                .extract()
                .response();
    }

    public static Response getBookingRequestWithQueryParams(Map<String, String> queryParams){
        return given()
                .when()
                .queryParams(queryParams)
                .get(RestfulBookerUrls.getBookingsUrl())
                .then()
                .extract()
                .response();
    }
}
