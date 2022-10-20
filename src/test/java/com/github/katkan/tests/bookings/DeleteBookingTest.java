package com.github.katkan.tests.bookings;

import com.github.katkan.properties.RestfulBookerProperties;
import com.github.katkan.requests.auth.CreateTokenRequest;
import com.github.katkan.requests.bookings.CreateBookingRequest;
import com.github.katkan.requests.bookings.DeleteBookingRequest;
import com.github.katkan.requests.bookings.GetBookingRequest;
import com.github.katkan.requests.bookings.GetBookingsRequest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static com.github.katkan.helpers.JsonHelper.*;
import static com.github.katkan.helpers.JsonHelper.ADDITIONAL_NEEDS;
import static org.assertj.core.api.Assertions.assertThat;

public class DeleteBookingTest {
    private static String token;

    @BeforeEach
    void auth() {
        JSONObject authData = new JSONObject();
        authData.put(USERNAME, RestfulBookerProperties.getUsername());
        authData.put(PASSWORD, RestfulBookerProperties.getPassword());
        Response createTokenResponse = CreateTokenRequest.createTokenRequest(authData);
        token = createTokenResponse.jsonPath().getString("token");
    }

    @Test
    @DisplayName("Delete existing booking that was just created")
    void deleteExistingBookingTest() {
        JSONObject booking = createNewBookingJsonObject();
        Response createBookingResponse = CreateBookingRequest.createBookingRequest(booking);
        int bookingId = Integer.parseInt(createBookingResponse.jsonPath().getString(ID));

        Response deleteBookingResponse = DeleteBookingRequest.deleteBookingRequest(bookingId, token);
        assertThat(deleteBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_CREATED);

        Response getBookingResponse = GetBookingRequest.getBookingRequest(bookingId);
        assertThat(getBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_NOT_FOUND);
    }

    @Disabled("Disabled until status code is fixed - currently returns 405 Method Not Allowed")
    @Test
    @DisplayName("Delete non existing booking")
    void deleteNonExistingBookingTest() {
        int bookingId = getNonExistingId();

        Response deleteBookingResponse = DeleteBookingRequest.deleteBookingRequest(bookingId, token);
        assertThat(deleteBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_NOT_FOUND);
    }

    private JSONObject createNewBookingJsonObject() {
        JSONObject bookingDates = new JSONObject();
        bookingDates.put(CHECKIN, "2022-10-30");
        bookingDates.put(CHECKOUT, "2022-10-31");

        JSONObject booking = new JSONObject();
        booking.put(FIRSTNAME, "Barbra");
        booking.put(LASTNAME, "Levis");
        booking.put(TOTAL_PRICE, "50000");
        booking.put(DEPOSIT_PAID, "true");
        booking.put(BOOKING_DATES, bookingDates);
        booking.put(ADDITIONAL_NEEDS, "Sleep mask in the room");
        return booking;
    }

    private int getNonExistingId() {
        List<Integer> bookingIds = getBookingIds();
        Collections.sort(bookingIds);
        return bookingIds.get((bookingIds.size()-1)) + 1;
    }

    private List<Integer> getBookingIds() {
        Response getBookingIdsResponse = GetBookingsRequest.getBookingRequest();
        JsonPath jsonPath = getBookingIdsResponse.jsonPath();
        return jsonPath.getList(ID);
    }
}
