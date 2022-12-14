package com.github.katkan.tests.bookings;

import com.github.katkan.dto.request.BookingDto;
import com.github.katkan.properties.RestfulBookerProperties;
import com.github.katkan.requests.auth.CreateTokenRequest;
import com.github.katkan.requests.bookings.CreateBookingRequest;
import com.github.katkan.requests.bookings.DeleteBookingRequest;
import com.github.katkan.requests.bookings.GetBookingRequest;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.katkan.helpers.BookingIdsHelper.getBookingId;
import static com.github.katkan.helpers.BookingIdsHelper.getNonExistingId;
import static com.github.katkan.helpers.JsonHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

public class DeleteBookingTest {
    private static String token;

    @BeforeEach
    void auth() {
        JSONObject authData = new JSONObject();
        authData.put(USERNAME, RestfulBookerProperties.getUsername());
        authData.put(PASSWORD, RestfulBookerProperties.getPassword());
        Response createTokenResponse = CreateTokenRequest.createTokenRequest(authData);
        token = createTokenResponse.jsonPath().getString(TOKEN);
    }

    @Test
    @DisplayName("Delete existing booking that was just created")
    void deleteExistingBookingTest() {
        BookingDto booking = new BookingDto();
        Response createBookingResponse = CreateBookingRequest.createBookingRequest(booking);
        int bookingId = getBookingId(createBookingResponse.jsonPath());

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
}
