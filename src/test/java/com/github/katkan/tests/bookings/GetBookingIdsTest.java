package com.github.katkan.tests.bookings;

import com.github.katkan.requests.bookings.GetBookingRequest;
import com.github.katkan.requests.bookings.GetBookingsRequest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.stream.Stream;

import static com.github.katkan.helpers.JsonHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class GetBookingIdsTest {

    private Map<String, String> queryParams;

    @BeforeEach
    void setup() {
        queryParams = new HashMap<>();
    }

    @Test
    @DisplayName("Get ids for all bookings that exist")
    void getBookingIdsTest() {
        Response response = GetBookingsRequest.getBookingRequest();

        JsonPath jsonPath = response.jsonPath();
        List<Integer> bookingIds = jsonPath.getList(ID);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(bookingIds).isNotEmpty();

        int bookingIdsSize = bookingIds.size();
        log.info("Current number of all bookingIds: {}", bookingIdsSize);

        long uniqueIdsNumber = bookingIds.stream()
                .distinct()
                .count();
        assertThat(bookingIdsSize).isEqualTo(uniqueIdsNumber);
    }

    @ParameterizedTest(name = "Get ids by firstname = {0}")
    @ValueSource(strings = {"Sally", "Guoqiang"})
    @DisplayName("Get booking ids based on the specific existing firstname")
    void getBookingIdsByFirstnameTest(String firstname) {
        queryParams.put(FIRSTNAME, firstname);

        Response getBookingIdsByFirstnameResponse = GetBookingsRequest.getBookingRequestWithQueryParams(queryParams);

        JsonPath getIdsByNameJsonPath = getBookingIdsByFirstnameResponse.jsonPath();
        List<Integer> bookingIds = getIdsByNameJsonPath.getList(ID);

        assertThat(getBookingIdsByFirstnameResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(bookingIds).isNotEmpty();

        int bookingIdsSize = bookingIds.size();
        log.info("Number of bookingIds for firstname {}: {}", queryParams.get(FIRSTNAME), bookingIdsSize);

        JsonPath bookingJsonPath = getRandomBooking(bookingIds);
        assertThat(bookingJsonPath.getString(FIRSTNAME)).isEqualTo(queryParams.get(FIRSTNAME));
    }

    @ParameterizedTest(name = "Get ids by lastname = {0}")
    @ValueSource(strings = {"Parchment", "Dominguez"})
    @DisplayName("Get booking ids based on the specific existing lastname")
    void getBookingIdsByLastnameTest(String lastname) {
        queryParams.put(LASTNAME, lastname);

        Response getBookingIdsByLastnameResponse = GetBookingsRequest.getBookingRequestWithQueryParams(queryParams);

        JsonPath bookingIdsJsonPath = getBookingIdsByLastnameResponse.jsonPath();
        List<Integer> bookingIds = bookingIdsJsonPath.getList(ID);

        assertThat(getBookingIdsByLastnameResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(bookingIds).isNotEmpty();

        int bookingIdsSize = bookingIds.size();
        log.info("Number of bookingIds for lastname {}: {}", queryParams.get(LASTNAME), bookingIdsSize);

        JsonPath bookingJsonPath = getRandomBooking(bookingIds);
        assertThat(bookingJsonPath.getString(LASTNAME)).isEqualTo(queryParams.get(LASTNAME));
    }

    @ParameterizedTest(name = "Get ids by checkin date = {0}")
    @ValueSource(strings = {"2020-01-01", "2013-05-03"})
    @DisplayName("Get booking ids based on the specific existing checkin dates")
    void getBookingIdsByCheckinTest(String checkin) {
        queryParams.put(CHECKIN, checkin);

        Response getBookingIdsResponse = GetBookingsRequest.getBookingRequestWithQueryParams(queryParams);

        JsonPath getIdsByNameJsonPath = getBookingIdsResponse.jsonPath();
        List<Integer> bookingIds = getIdsByNameJsonPath.getList(ID);

        assertThat(getBookingIdsResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(bookingIds).isNotEmpty();

        int bookingIdsSize = bookingIds.size();
        log.info("Number of bookingIds for checkin date equal or later than {}: {}", queryParams.get(CHECKIN), bookingIdsSize);

        JsonPath bookingJsonPath = getRandomBooking(bookingIds);
        assertThat(bookingJsonPath.getString("bookingdates." + CHECKIN)).isGreaterThanOrEqualTo(queryParams.get(CHECKIN));
    }

    @ParameterizedTest(name = "Get ids by checkout date = {0}")
    @Disabled("Disabled until feature is fixed, currently returns random results")
    @ValueSource(strings = {"2022-01-01", "2018-06-01"})
    @DisplayName("DISABLED - Get booking ids based on the specific existing checkout dates")
    void getBookingIdsByCheckoutTest(String checkout) {
        queryParams.put(CHECKOUT, checkout);

        Response getBookingIdsResponse = GetBookingsRequest.getBookingRequestWithQueryParams(queryParams);

        JsonPath getIdsByNameJsonPath = getBookingIdsResponse.jsonPath();
        List<Integer> bookingIds = getIdsByNameJsonPath.getList(ID);

        assertThat(getBookingIdsResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(bookingIds).isNotEmpty();

        int bookingIdsSize = bookingIds.size();
        log.info("Number of bookingIds for checkout date equal or later than {}: {}", queryParams.get(CHECKOUT), bookingIdsSize);

        JsonPath bookingJsonPath = getRandomBooking(bookingIds);
        assertThat(bookingJsonPath.getString("bookingdates." + CHECKOUT)).isGreaterThanOrEqualTo(queryParams.get(CHECKOUT));
    }

    @ParameterizedTest
    @MethodSource("provideQueryParamsData")
    @DisplayName("Get booking ids based on the combinations of query params: firstname, lastname, checkin date")
    void getBookingIdsByFirstnameAndLastnameTest(Map<String, String> queryParams) {
        Response getBookingIdsResponse = GetBookingsRequest.getBookingRequestWithQueryParams(queryParams);

        JsonPath getIdsByQueryParamsJsonPath = getBookingIdsResponse.jsonPath();
        List<Integer> bookingIds = getIdsByQueryParamsJsonPath.getList(ID);

        assertThat(getBookingIdsResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(bookingIds).isNotEmpty();

        int bookingIdsSize = bookingIds.size();
        log.info("Number of bookingIds for {} {} {}: {}",
                Optional.ofNullable(queryParams.get(FIRSTNAME)).map(q -> "firstname= " + q).orElse(""),
                Optional.ofNullable(queryParams.get(LASTNAME)).map(q -> "lastname= " + q).orElse(""),
                Optional.ofNullable(queryParams.get(CHECKIN)).map(q -> "checkin date>= " + q).orElse(""),
                bookingIdsSize);

        JsonPath bookingJsonPath = getRandomBooking(bookingIds);
        queryParams.forEach((param, value) -> {
                    if (param.equals(CHECKIN)) {
                        assertThat(bookingJsonPath.getString("bookingdates." + CHECKIN)).isGreaterThanOrEqualTo(value);
                    } else {
                        assertThat(bookingJsonPath.getString(param)).isEqualTo(value);
                    }
                }
        );
    }

    private JsonPath getRandomBooking(List<Integer> bookingIds) {
        Collections.shuffle(bookingIds);
        Response getBookingById = GetBookingRequest.getBookingRequest(bookingIds.get(0));
        return getBookingById.jsonPath();
    }

    private static Stream<Arguments> provideQueryParamsData() {
        return Stream.of(
                Arguments.of(Map.of(FIRSTNAME, "Sally", LASTNAME, "Brown")),
                Arguments.of(Map.of(FIRSTNAME, "Michael", CHECKIN, "2012-01-01")),
                Arguments.of(Map.of(LASTNAME, "Dominguez", CHECKIN, "2015-10-01")),
                Arguments.of(Map.of(FIRSTNAME, "Sally", LASTNAME, "Brown", CHECKIN, "2010-12-01"))
        );
    }
}
