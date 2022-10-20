package com.github.katkan.tests.bookings;

import com.github.katkan.properties.RestfulBookerProperties;
import com.github.katkan.requests.auth.CreateTokenRequest;
import com.github.katkan.requests.bookings.CreateBookingRequest;
import com.github.katkan.requests.bookings.DeleteBookingRequest;
import com.github.katkan.requests.bookings.GetBookingRequest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static com.github.katkan.helpers.JsonHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CreateBookingTest {

    private static final String BOOKING = "booking.";
    private static final String BOOKING_BOOKING_DATES = "booking.bookingdates.";

    private String firstname = "John";
    private String lastname = "Kowalsky";
    private String totalPrice = "10150";
    private String depositPaid = "true";
    private String checkin = "2023-01-01";
    private String checkout = "2023-02-01";
    private String additionalNeeds = "Lunch";

    @Test
    @DisplayName("Create booking with invalid dates (the only validation implemented)")
    void createBookingWithInvalidDatesTest() {
        this.checkin = "20221012";
        this.checkout = "202210-12";
        JSONObject booking = getBookingJsonObject();

        Response createBookingResponse = CreateBookingRequest.createBookingRequest(booking);
        assertThat(createBookingResponse.htmlPath().getString("body")).isEqualTo("Invalid date");
    }

    @Nested
    class TestValidData {

        private static String token;
        private static int bookingId;
        
        @BeforeEach
        void auth() {
            JSONObject authData = new JSONObject();
            authData.put(USERNAME, RestfulBookerProperties.getUsername());
            authData.put(PASSWORD, RestfulBookerProperties.getPassword());
            Response createTokenResponse = CreateTokenRequest.createTokenRequest(authData);
            token = createTokenResponse.jsonPath().getString("token");
        }

        @AfterEach
        void deleteBooking() {
            Response response = DeleteBookingRequest.deleteBookingRequest(bookingId, token);
            assertThat(response.statusCode()).isEqualTo(201);
        }

        @Test
        @DisplayName("Create booking with valid data")
        void createBookingWithValidDataTest() {
            JSONObject booking = getBookingJsonObject();

            Response createBookingResponse = CreateBookingRequest.createBookingRequest(booking);
            JsonPath jsonPath = createBookingResponse.jsonPath();
            bookingId = getBookingId(jsonPath);

            assertThat(createBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
            verifyCreateResponseContainsCorrectData(jsonPath);

            JsonPath getBooking = getCreatedBookingJsonPath(jsonPath);
            verifyGetResponseContainsCorrectData(getBooking);
        }

        @ParameterizedTest(name = "Create booking with firstname = {0}")
        @ValueSource(strings = {"Yu", "Gżegżółka", "Mrs.Kate", "Rhoshandiatellyneshiaunneveshenk"})
        @DisplayName("Create booking with different valid firstnames")
        void createBookingWithDifferentNamesTest(String firstname) {
            CreateBookingTest.this.firstname = firstname;
            JSONObject booking = getBookingJsonObject();

            Response createBookingResponse = CreateBookingRequest.createBookingRequest(booking);
            JsonPath jsonPath = createBookingResponse.jsonPath();
            bookingId = getBookingId(jsonPath);

            assertThat(createBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
            verifyCreateResponseContainsCorrectData(jsonPath);

            JsonPath getBooking = getCreatedBookingJsonPath(jsonPath);
            verifyGetResponseContainsCorrectData(getBooking);
        }

        @ParameterizedTest(name = "Create booking with lastname = {0}")
        @ValueSource(strings = {"Yu", "Brzęczyszczykiewicz", "Davis", "SMITH"})
        @DisplayName("Create booking with different valid lastnames")
        void createBookingWithDifferentLastnamesTest(String lastname) {
            CreateBookingTest.this.lastname = lastname;
            JSONObject booking = getBookingJsonObject();

            Response createBookingResponse = CreateBookingRequest.createBookingRequest(booking);
            JsonPath jsonPath = createBookingResponse.jsonPath();
            bookingId = getBookingId(jsonPath);

            assertThat(createBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
            verifyCreateResponseContainsCorrectData(jsonPath);

            JsonPath getBooking = getCreatedBookingJsonPath(jsonPath);
            verifyGetResponseContainsCorrectData(getBooking);
        }

        @ParameterizedTest(name = "Create booking with total price = {0}")
        @ValueSource(strings = {"500", "1000000", "0", "999"})
        @DisplayName("Create booking with different valid total prices")
        void createBookingWithDifferentTotalPricesTest(String totalPrice) {
            CreateBookingTest.this.totalPrice = totalPrice;
            JSONObject booking = getBookingJsonObject();

            Response createBookingResponse = CreateBookingRequest.createBookingRequest(booking);
            JsonPath jsonPath = createBookingResponse.jsonPath();
            bookingId = getBookingId(jsonPath);

            assertThat(createBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
            verifyCreateResponseContainsCorrectData(jsonPath);

            JsonPath getBooking = getCreatedBookingJsonPath(jsonPath);
            verifyGetResponseContainsCorrectData(getBooking);
        }

        @Disabled("Disabled until feature is fixed - currently always set to true")
        @Test
        @DisplayName("Create booking with deposit paid set to false")
        void createBookingWithDepositUnpaidTest() {
            CreateBookingTest.this.depositPaid = "false";
            JSONObject booking = getBookingJsonObject();

            Response createBookingResponse = CreateBookingRequest.createBookingRequest(booking);
            JsonPath jsonPath = createBookingResponse.jsonPath();
            bookingId = getBookingId(jsonPath);

            assertThat(createBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
            verifyCreateResponseContainsCorrectData(jsonPath);

            JsonPath getBooking = getCreatedBookingJsonPath(jsonPath);
            verifyGetResponseContainsCorrectData(getBooking);
        }

        @ParameterizedTest(name = "Create booking with checkin = {0} and checkout = {1}")
        @MethodSource("provideBookingDatesData")
        void createBookingWithDifferentBookingDatesTest(String checkin, String checkout) {
            CreateBookingTest.this.checkin = checkin;
            CreateBookingTest.this.checkout = checkout;
            JSONObject booking = getBookingJsonObject();

            Response createBookingResponse = CreateBookingRequest.createBookingRequest(booking);
            JsonPath jsonPath = createBookingResponse.jsonPath();
            bookingId = getBookingId(jsonPath);

            assertThat(createBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
            verifyCreateResponseContainsCorrectData(jsonPath);

            JsonPath getBooking = getCreatedBookingJsonPath(jsonPath);
            verifyGetResponseContainsCorrectData(getBooking);
        }

        @ParameterizedTest(name = "Create booking with additional needs = {0}")
        @ValueSource(strings = {"Breakfast and dinner",
                "Additional bedroom for my kid",
                "Wake me up at 7:30 AM",
                "Call the office 777-555-222 and inform after arrival. Please also arrange breakfast and lunch."})
        @DisplayName("Create booking with different valid additional needs")
        void createBookingWithDifferentAdditionalNeedsTest(String additionalNeeds) {
            CreateBookingTest.this.additionalNeeds = additionalNeeds;
            JSONObject booking = getBookingJsonObject();

            Response createBookingResponse = CreateBookingRequest.createBookingRequest(booking);
            JsonPath jsonPath = createBookingResponse.jsonPath();
            bookingId = getBookingId(jsonPath);

            assertThat(createBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
            verifyCreateResponseContainsCorrectData(jsonPath);

            JsonPath getBooking = getCreatedBookingJsonPath(jsonPath);
            verifyGetResponseContainsCorrectData(getBooking);
        }

        private static Stream<Arguments> provideBookingDatesData() {
            return Stream.of(
                    Arguments.of("2022-10-20", "2022-12-02"),
                    Arguments.of("2023-01-20", "2023-02-02"),
                    Arguments.of("2024-03-20", "2024-03-20")
            );
        }

    }

    private JsonPath getCreatedBookingJsonPath(JsonPath jsonPath) {
        int bookingId = getBookingId(jsonPath);
        Response getCreatedBookingResponse = GetBookingRequest.getBookingRequest(bookingId);
        return getCreatedBookingResponse.jsonPath();
    }

    private int getBookingId(JsonPath jsonPath) {
        return Integer.parseInt(jsonPath.getString("bookingid"));
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

    private void verifyCreateResponseContainsCorrectData(JsonPath jsonPath) {
        assertThat(jsonPath.getString(BOOKING + FIRSTNAME)).isEqualTo(firstname);
        assertThat(jsonPath.getString(BOOKING + LASTNAME)).isEqualTo(lastname);
        assertThat(jsonPath.getString(BOOKING + TOTAL_PRICE)).isEqualTo(totalPrice);
        assertThat(jsonPath.getString(BOOKING + DEPOSIT_PAID)).isEqualTo(depositPaid);
        assertThat(jsonPath.getString(BOOKING_BOOKING_DATES + CHECKIN)).isEqualTo(checkin);
        assertThat(jsonPath.getString(BOOKING_BOOKING_DATES + CHECKOUT)).isEqualTo(checkout);
        assertThat(jsonPath.getString(BOOKING + ADDITIONAL_NEEDS)).isEqualTo(additionalNeeds);
    }

    private void verifyGetResponseContainsCorrectData(JsonPath jsonPath) {
        assertThat(jsonPath.getString(FIRSTNAME)).isEqualTo(firstname);
        assertThat(jsonPath.getString(LASTNAME)).isEqualTo(lastname);
        assertThat(jsonPath.getString(TOTAL_PRICE)).isEqualTo(totalPrice);
        assertThat(jsonPath.getString(DEPOSIT_PAID)).isEqualTo(depositPaid);
        assertThat(jsonPath.getString(BOOKING_DATES + "." + CHECKIN)).isEqualTo(checkin);
        assertThat(jsonPath.getString(BOOKING_DATES + "." + CHECKOUT)).isEqualTo(checkout);
        assertThat(jsonPath.getString(ADDITIONAL_NEEDS)).isEqualTo(additionalNeeds);
    }
}
