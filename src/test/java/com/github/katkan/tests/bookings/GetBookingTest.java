package com.github.katkan.tests.bookings;

import com.github.katkan.requests.bookings.GetBookingRequest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.katkan.helpers.BookingIdsHelper.getExistingId;
import static com.github.katkan.helpers.BookingIdsHelper.getNonExistingId;
import static com.github.katkan.helpers.JsonHelper.jsonKeys;
import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingTest {

    @Test
    @DisplayName("Get booking based on the existing id")
    void getExistingBookingTest() {
        int existingId = getExistingId();
        Response getBookingResponse = GetBookingRequest.getBookingRequest(existingId);
        JsonPath getBookingJsonPath = getBookingResponse.jsonPath();

        assertThat(getBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        for (String key: jsonKeys) {
            assertThat(getBookingJsonPath.getString(key)).isNotEmpty();
        }
    }

    @Test
    @DisplayName("Get booking based on the non existing id")
    void getNonExistingBookingTest() {
        int nonExistingId = getNonExistingId();
        Response getBookingResponse = GetBookingRequest.getBookingRequest(nonExistingId);

        assertThat(getBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_NOT_FOUND);
    }
}
