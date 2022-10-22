package com.github.katkan.requests.bookings;

import com.github.katkan.url.RestfulBookerUrls;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class PutBookingRequest {

    public static Response putBookingRequest(JSONObject booking, int bookingId, String token){
        return given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .body(booking.toString())
                .when()
                .put(RestfulBookerUrls.getBookingUrl(bookingId))
                .then()
                .extract()
                .response();
    }
}
