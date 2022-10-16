package com.github.katkan.tests.bookings;

import com.github.katkan.requests.bookings.GetBookingRequest;
import com.github.katkan.requests.bookings.GetBookingsRequest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class GetBookingIdsTest {

    @Test
    @DisplayName("Get ids for all bookings that exist")
    void getBookingIdsTest() {

        Response response = GetBookingsRequest.getBookingRequest();

        JsonPath jsonPath = response.jsonPath();
        List<Integer> bookingIds = jsonPath.getList("bookingid");
        int bookingIdsSize = bookingIds.size();
        log.info("Current number of bookingIds: {}", bookingIdsSize);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(bookingIds).isNotEmpty();
        long uniqueIds = bookingIds.stream()
                .distinct()
                .count();
        assertThat(bookingIdsSize).isEqualTo(uniqueIds);
    }


    @ParameterizedTest
    @MethodSource("provideGetByNameData")
    @DisplayName("Get booking ids based on the specific existing firstname and lastname")
    void getBookingIdsByNameTest(String firstname, String lastname) {

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("firstname", firstname);
        queryParams.put("lastname", lastname);

        Response getBookingIdsResponse = GetBookingsRequest.getBookingRequestWithQueryParams(queryParams);

        JsonPath getIdsByNameJsonPath = getBookingIdsResponse.jsonPath();
        List<Integer> bookingIds = getIdsByNameJsonPath.getList("bookingid");
        int bookingIdsSize = bookingIds.size();
        log.info("Number of bookingIds for {} {}: {}", queryParams.get("firstname"), queryParams.get("lastname"), bookingIdsSize);

        Response getBookingById = GetBookingRequest.getBookingRequest(bookingIds.get(0));
        JsonPath getByIdJsonPath = getBookingById.jsonPath();

        assertThat(getBookingIdsResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(bookingIds).isNotEmpty();
        assertThat(getByIdJsonPath.getString("firstname")).isEqualTo(queryParams.get("firstname"));
        assertThat(getByIdJsonPath.getString("lastname")).isEqualTo(queryParams.get("lastname"));
    }

    private static Stream<Arguments> provideGetByNameData() {
        return Stream.of(
                Arguments.of("Sally", "Brown"),
                Arguments.of("Alex", "Parchment")
        );
    }
}
