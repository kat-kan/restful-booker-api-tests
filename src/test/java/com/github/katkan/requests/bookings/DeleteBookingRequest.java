package com.github.katkan.requests.bookings;

import com.github.katkan.url.RestfulBookerUrls;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class DeleteBookingRequest {

    public static Response deleteBookingRequest(int bookingId, String token) {
        return given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .when()
                .delete(RestfulBookerUrls.deleteBookingUrl(bookingId))
                .then()
                .log()
                .ifError()
                .extract()
                .response();
    }
}
