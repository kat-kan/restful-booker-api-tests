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
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class GetBookingIdsTest {

    public static final String BOOKING_ID = "bookingid";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String CHECKIN = "checkin";
    private static final String CHECKOUT = "checkout";
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
        List<Integer> bookingIds = jsonPath.getList(BOOKING_ID);
        int bookingIdsSize = bookingIds.size();
        log.info("Current number of all bookingIds: {}", bookingIdsSize);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(bookingIds).isNotNull();
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
        List<Integer> bookingIds = getIdsByNameJsonPath.getList(BOOKING_ID);
        int bookingIdsSize = bookingIds.size();
        log.info("Number of bookingIds for firstname {}: {}", queryParams.get(FIRSTNAME), bookingIdsSize);

        Collections.shuffle(bookingIds);
        Response getBookingById = GetBookingRequest.getBookingRequest(bookingIds.get(0));
        JsonPath bookingJsonPath = getBookingById.jsonPath();

        assertThat(getBookingIdsByFirstnameResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(bookingIds).isNotNull();
        assertThat(bookingJsonPath.getString(FIRSTNAME)).isEqualTo(queryParams.get(FIRSTNAME));
    }

    @ParameterizedTest(name = "Get ids by lastname = {0}")
    @ValueSource(strings = {"Parchment", "Dominguez"})
    @DisplayName("Get booking ids based on the specific existing lastname")
    void getBookingIdsByLastnameTest(String lastname) {

        queryParams.put(LASTNAME, lastname);

        Response getBookingIdsByLastnameResponse = GetBookingsRequest.getBookingRequestWithQueryParams(queryParams);

        JsonPath bookingIdsJsonPath = getBookingIdsByLastnameResponse.jsonPath();
        List<Integer> bookingIds = bookingIdsJsonPath.getList(BOOKING_ID);
        int bookingIdsSize = bookingIds.size();
        log.info("Number of bookingIds for lastname {}: {}", queryParams.get(LASTNAME), bookingIdsSize);

        Collections.shuffle(bookingIds);
        Response getBookingById = GetBookingRequest.getBookingRequest(bookingIds.get(0));
        JsonPath bookingJsonPath = getBookingById.jsonPath();

        assertThat(getBookingIdsByLastnameResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(bookingIds).isNotNull();
        assertThat(bookingJsonPath.getString(LASTNAME)).isEqualTo(queryParams.get(LASTNAME));
    }

    @ParameterizedTest(name = "Get ids by checkin date = {0}")
    @ValueSource(strings = {"2020-01-01", "2013-05-03"})
    @DisplayName("Get booking ids based on the specific existing checkin dates")
    void getBookingIdsByCheckinTest(String checkin) {

        queryParams.put(CHECKIN, checkin);

        Response getBookingIdsResponse = GetBookingsRequest.getBookingRequestWithQueryParams(queryParams);

        JsonPath getIdsByNameJsonPath = getBookingIdsResponse.jsonPath();
        List<Integer> bookingIds = getIdsByNameJsonPath.getList(BOOKING_ID);
        int bookingIdsSize = bookingIds.size();
        log.info("Number of bookingIds for checkin date equal or later than {}: {}", queryParams.get(CHECKIN), bookingIdsSize);

        Collections.shuffle(bookingIds);
        Response getBookingById = GetBookingRequest.getBookingRequest(bookingIds.get(0));
        JsonPath getByIdJsonPath = getBookingById.jsonPath();

        assertThat(getBookingIdsResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(bookingIds).isNotNull();
        assertThat(getByIdJsonPath.getString("bookingdates." + CHECKIN)).isGreaterThanOrEqualTo(queryParams.get(CHECKIN));
    }

    @ParameterizedTest(name = "Get ids by checkout date = {0}")
    @Disabled("Disabled until feature is fixed, currently returns random results")
    @ValueSource(strings = {"2022-01-01", "2018-06-01"})
    @DisplayName("DISABLED - Get booking ids based on the specific existing checkout dates")
    void getBookingIdsByCheckoutTest(String checkout) {

        queryParams.put(CHECKOUT, checkout);

        Response getBookingIdsResponse = GetBookingsRequest.getBookingRequestWithQueryParams(queryParams);

        JsonPath getIdsByNameJsonPath = getBookingIdsResponse.jsonPath();
        List<Integer> bookingIds = getIdsByNameJsonPath.getList(BOOKING_ID);
        int bookingIdsSize = bookingIds.size();
        log.info("Number of bookingIds for checkout date equal or later than {}: {}", queryParams.get(CHECKOUT), bookingIdsSize);

        Collections.shuffle(bookingIds);
        Response getBookingById = GetBookingRequest.getBookingRequest(bookingIds.get(0));
        JsonPath getByIdJsonPath = getBookingById.jsonPath();

        assertThat(getBookingIdsResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(bookingIds).isNotNull();
        assertThat(getByIdJsonPath.getString("bookingdates." + CHECKOUT)).isGreaterThanOrEqualTo(queryParams.get(CHECKOUT));
    }

 /*   @ParameterizedTest
    @MethodSource("provideGetByNameData")
    @DisplayName("Get booking ids based on the specific existing firstname and lastname")
    void getBookingIdsByFirstnameAndLastnameTest(String firstname, String lastname) {

        queryParams.put("firstname", firstname);
        queryParams.put("lastname", lastname);

        Response getBookingIdsResponse = GetBookingsRequest.getBookingRequestWithQueryParams(queryParams);

        JsonPath getIdsByNameJsonPath = getBookingIdsResponse.jsonPath();
        List<Integer> bookingIds = getIdsByNameJsonPath.getList(BOOKING_ID);
        int bookingIdsSize = bookingIds.size();
        log.info("Number of bookingIds for {} {}: {}", queryParams.get("firstname"), queryParams.get("lastname"), bookingIdsSize);

        Response getBookingById = GetBookingRequest.getBookingRequest(bookingIds.get(0));
        JsonPath getByIdJsonPath = getBookingById.jsonPath();

        assertThat(getBookingIdsResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(bookingIds).isNotNull();
        assertThat(getByIdJsonPath.getString("firstname")).isEqualTo(queryParams.get("firstname"));
        assertThat(getByIdJsonPath.getString("lastname")).isEqualTo(queryParams.get("lastname"));
    }

    private static Stream<Arguments> provideGetByNameData() {
        return Stream.of(
                Arguments.of("Sally", "Brown"),
                Arguments.of("Alex", "Parchment")
        );
    }*/
}
