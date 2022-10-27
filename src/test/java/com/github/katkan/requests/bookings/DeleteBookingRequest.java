package com.github.katkan.requests.bookings;

import com.github.katkan.requests.BaseRequest;
import com.github.katkan.url.RestfulBookerUrls;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class DeleteBookingRequest {

    public static Response deleteBookingRequest(int bookingId, String token) {
        return given()
                .spec(BaseRequest.setUp())
                .header("Cookie", "token=" + token)
                .when()
                .delete(RestfulBookerUrls.getBookingUrl(bookingId))
                .then()
                .extract()
                .response();
    }
}
