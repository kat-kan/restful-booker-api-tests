package com.github.katkan.tests.bookings;

import com.github.katkan.properties.RestfulBookerProperties;
import com.github.katkan.requests.auth.CreateTokenRequest;
import com.github.katkan.requests.bookings.CreateBookingRequest;
import com.github.katkan.requests.bookings.PutBookingRequest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.katkan.helpers.JsonHelper.*;
import static com.github.katkan.helpers.JsonHelper.ADDITIONAL_NEEDS;

public class PutBookingTest {

    private static String token;

    private String firstname = "John";
    private String lastname = "Kowalsky";
    private Integer totalPrice = 10150;
    private boolean depositPaid = true;
    private String checkin = "2023-01-01";
    private String checkout = "2023-02-01";
    private String additionalNeeds = "Lunch";

    @BeforeEach
    void auth() {
        JSONObject authData = new JSONObject();
        authData.put(USERNAME, RestfulBookerProperties.getUsername());
        authData.put(PASSWORD, RestfulBookerProperties.getPassword());
        Response createTokenResponse = CreateTokenRequest.createTokenRequest(authData);
        token = createTokenResponse.jsonPath().getString("token");
    }

    @Test
    @DisplayName("Modify booking with PUT method using valid data")
    void modifyBookingWithPUTUsingValidData() {
        JSONObject booking = getBookingJsonObject();

        Response createBookingResponse = CreateBookingRequest.createBookingRequest(booking);
        JsonPath jsonPath = createBookingResponse.jsonPath();
        int bookingId = Integer.parseInt(jsonPath.getString("bookingid"));

        booking.put("firstname", "Oliver");

        Response putBookingResponse = PutBookingRequest.putBookingRequest(booking, bookingId, token);
        Assertions.assertThat(putBookingResponse.getStatusCode()).isEqualTo(200);
        Assertions.assertThat(putBookingResponse.jsonPath().getString("firstname")).isEqualTo("Oliver");

    }

    private JSONObject getBookingJsonObject() {
        JSONObject bookingDates = new JSONObject();
        bookingDates.put(CHECKIN, checkin);
        bookingDates.put(CHECKOUT, checkout);

        JSONObject booking = new JSONObject();
        booking.put(FIRSTNAME, firstname);
        booking.put(LASTNAME, lastname);
        booking.put(TOTAL_PRICE, totalPrice);
        booking.put(DEPOSIT_PAID, depositPaid);
        booking.put(BOOKING_DATES, bookingDates);
        booking.put(ADDITIONAL_NEEDS, additionalNeeds);
        return booking;
    }
}
