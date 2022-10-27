package com.github.katkan.requests.bookings;

import com.github.katkan.dto.request.BookingDto;
import com.github.katkan.requests.BaseRequest;
import com.github.katkan.url.RestfulBookerUrls;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CreateBookingRequest {

    public static Response createBookingRequest(BookingDto bookingDto){
        return given()
                .spec(BaseRequest.setUp())
                .body(bookingDto)
                .when()
                .post(RestfulBookerUrls.createBookingUrl())
                .then()
                .extract()
                .response();
    }
}
