package com.github.katkan.requests.bookings;

import com.github.katkan.dto.request.BookingDto;
import com.github.katkan.requests.BaseRequest;
import com.github.katkan.url.RestfulBookerUrls;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PutBookingRequest {

    public static Response putBookingRequest(BookingDto bookingDto, int bookingId, String token){
        return given()
                .spec(BaseRequest.setUp())
                .header("Cookie", "token=" + token)
                .body(bookingDto)
                .when()
                .put(RestfulBookerUrls.getBookingUrl(bookingId))
                .then()
                .extract()
                .response();
    }
}
