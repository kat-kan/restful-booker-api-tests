package com.github.katkan.tests.bookings;

import com.github.katkan.requests.bookings.GetBookingRequest;
import com.github.katkan.requests.bookings.GetBookingsRequest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static com.github.katkan.helpers.JsonHelper.ID;
import static com.github.katkan.helpers.JsonHelper.jsonKeys;
import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingTest {

    @Test
    @DisplayName("Get booking based on the existing id")
    void getExistingBookingTest() {
        int existingId = getExistingId();
        Response response = GetBookingRequest.getBookingRequest(existingId);
        JsonPath jsonPath = response.jsonPath();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
        for (String key: jsonKeys) {
            assertThat(jsonPath.getString(key)).isNotEmpty();
        }
    }

    @Test
    @DisplayName("Get booking based on the non existing id")
    void getNonExistingBookingTest() {
        int nonExistingId = getNonExistingId();
        Response response = GetBookingRequest.getBookingRequest(nonExistingId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_NOT_FOUND);
    }

    private int getExistingId() {
        List<Integer> bookingIds = getBookingIds();
        Collections.shuffle(bookingIds);
        return bookingIds.get(0);
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
