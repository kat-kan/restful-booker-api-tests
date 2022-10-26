package com.github.katkan.requests.bookings;

import com.github.katkan.dto.request.BookingDto;
import com.github.katkan.url.RestfulBookerUrls;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class CreateBookingRequest {

    public static Response createBookingRequest(JSONObject booking){
        return given()
                .contentType(ContentType.JSON)
                .body(booking.toString())
                .when()
                .post(RestfulBookerUrls.createBookingUrl())
                .then()
                .extract()
                .response();
    }

    public static Response createBookingRequest(BookingDto bookingDto){
        return given()
                .contentType(ContentType.JSON)
                .body(bookingDto)
                .when()
                .post(RestfulBookerUrls.createBookingUrl())
                .then()
                .extract()
                .response();
    }
}
