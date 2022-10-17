package com.github.katkan.requests.bookings;

import com.github.katkan.url.RestfulBookerUrls;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GetBookingRequest {

    public static Response getBookingRequest(int bookingId){
        return given()
                .when()
                .get(RestfulBookerUrls.getBookingUrl(bookingId))
                .then()
                .extract()
                .response();
    }
}
