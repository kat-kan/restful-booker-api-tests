package com.github.katkan.requests.bookings;

import com.github.katkan.dto.request.BookingDto;
import com.github.katkan.url.RestfulBookerUrls;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class PatchBookingRequest {

    public static Response patchBookingRequest(BookingDto booking, int bookingId, String token){
        return given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .body(booking)
                .when()
                .patch(RestfulBookerUrls.getBookingUrl(bookingId))
                .then()
                .extract()
                .response();
    }

    public static Response patchBookingRequest(JSONObject booking, int bookingId, String token){
        return given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .body(booking.toString())
                .when()
                .patch(RestfulBookerUrls.getBookingUrl(bookingId))
                .then()
                .extract()
                .response();
    }
}
