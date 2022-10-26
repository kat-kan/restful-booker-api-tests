package com.github.katkan.tests.bookings;

import com.github.katkan.dto.request.BookingDatesDto;
import com.github.katkan.dto.request.BookingDto;
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
import static com.github.katkan.helpers.ValidationHelper.verifyCreateResponseContainsCorrectData;
import static com.github.katkan.helpers.ValidationHelper.verifyResponseContainsCorrectData;
import static org.assertj.core.api.Assertions.assertThat;

public class CreateBookingTest {

    @Test
    @DisplayName("Create booking with invalid dates (the only validation implemented)")
    void createBookingWithInvalidDatesTest() {
        BookingDto booking = new BookingDto();
        BookingDatesDto bookingDates = new BookingDatesDto();
        bookingDates.setCheckin("20221012");
        bookingDates.setCheckout("202210-12");
        booking.setBookingDates(bookingDates);

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
            BookingDto booking = new BookingDto();

            Response createBookingResponse = CreateBookingRequest.createBookingRequest(booking);
            JsonPath jsonPath = createBookingResponse.jsonPath();
            bookingId = getBookingId(jsonPath);

            assertThat(createBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
            verifyCreateResponseContainsCorrectData(jsonPath, booking);

            JsonPath getBooking = getCreatedBookingJsonPath(jsonPath);
            verifyResponseContainsCorrectData(getBooking, booking);
        }

        @ParameterizedTest(name = "Create booking with firstname = {0}")
        @ValueSource(strings = {"Yu", "Gżegżółka", "Mrs.Kate", "Rhoshandiatellyneshiaunneveshenk"})
        @DisplayName("Create booking with different valid firstnames")
        void createBookingWithDifferentNamesTest(String firstname) {
            BookingDto booking = new BookingDto();
            booking.setFirstname(firstname);

            Response createBookingResponse = CreateBookingRequest.createBookingRequest(booking);
            JsonPath jsonPath = createBookingResponse.jsonPath();
            bookingId = getBookingId(jsonPath);

            assertThat(createBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
            verifyCreateResponseContainsCorrectData(jsonPath, booking);

            JsonPath getBooking = getCreatedBookingJsonPath(jsonPath);
            verifyResponseContainsCorrectData(getBooking,booking);
        }

        @ParameterizedTest(name = "Create booking with lastname = {0}")
        @ValueSource(strings = {"Yu", "Brzęczyszczykiewicz", "Davis", "SMITH"})
        @DisplayName("Create booking with different valid lastnames")
        void createBookingWithDifferentLastnamesTest(String lastname) {
            BookingDto booking = new BookingDto();
            booking.setFirstname(lastname);

            Response createBookingResponse = CreateBookingRequest.createBookingRequest(booking);
            JsonPath jsonPath = createBookingResponse.jsonPath();
            bookingId = getBookingId(jsonPath);

            assertThat(createBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
            verifyCreateResponseContainsCorrectData(jsonPath, booking);

            JsonPath getBooking = getCreatedBookingJsonPath(jsonPath);
            verifyResponseContainsCorrectData(getBooking, booking);
        }

        @ParameterizedTest(name = "Create booking with total price = {0}")
        @ValueSource(ints = {500, 1000000, 0, 999})
        @DisplayName("Create booking with different valid total prices")
        void createBookingWithDifferentTotalPricesTest(Integer totalPrice) {
            BookingDto booking = new BookingDto();
            booking.setTotalPrice(totalPrice);

            Response createBookingResponse = CreateBookingRequest.createBookingRequest(booking);
            JsonPath jsonPath = createBookingResponse.jsonPath();
            bookingId = getBookingId(jsonPath);

            assertThat(createBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
            verifyCreateResponseContainsCorrectData(jsonPath, booking);

            JsonPath getBooking = getCreatedBookingJsonPath(jsonPath);
            verifyResponseContainsCorrectData(getBooking, booking);
        }

        @Test
        @DisplayName("Create booking with deposit paid set to false")
        void createBookingWithDepositUnpaidTest() {
            BookingDto booking = new BookingDto();
            booking.setDepositPaid(false);

            Response createBookingResponse = CreateBookingRequest.createBookingRequest(booking);
            JsonPath jsonPath = createBookingResponse.jsonPath();
            bookingId = getBookingId(jsonPath);

            assertThat(createBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
            verifyCreateResponseContainsCorrectData(jsonPath, booking);

            JsonPath getBooking = getCreatedBookingJsonPath(jsonPath);
            verifyResponseContainsCorrectData(getBooking, booking);
        }

        @ParameterizedTest(name = "Create booking with checkin = {0} and checkout = {1}")
        @MethodSource("provideBookingDatesData")
        void createBookingWithDifferentBookingDatesTest(String checkin, String checkout) {
            BookingDto booking = new BookingDto();
            BookingDatesDto bookingDates = new BookingDatesDto();
            bookingDates.setCheckin(checkin);
            bookingDates.setCheckout(checkout);
            booking.setBookingDates(bookingDates);

            Response createBookingResponse = CreateBookingRequest.createBookingRequest(booking);
            JsonPath jsonPath = createBookingResponse.jsonPath();
            bookingId = getBookingId(jsonPath);

            assertThat(createBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
            verifyCreateResponseContainsCorrectData(jsonPath, booking);

            JsonPath getBooking = getCreatedBookingJsonPath(jsonPath);
            verifyResponseContainsCorrectData(getBooking, booking);
        }

        @ParameterizedTest(name = "Create booking with additional needs = {0}")
        @ValueSource(strings = {"Breakfast and dinner",
                "Additional bedroom for my kid",
                "Wake me up at 7:30 AM",
                "Call the office 777-555-222 and inform after arrival. Please also arrange breakfast and lunch."})
        @DisplayName("Create booking with different valid additional needs")
        void createBookingWithDifferentAdditionalNeedsTest(String additionalNeeds) {
            BookingDto booking = new BookingDto();
            booking.setAdditionalNeeds(additionalNeeds);

            Response createBookingResponse = CreateBookingRequest.createBookingRequest(booking);
            JsonPath jsonPath = createBookingResponse.jsonPath();
            bookingId = getBookingId(jsonPath);

            assertThat(createBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
            verifyCreateResponseContainsCorrectData(jsonPath, booking);

            JsonPath getBooking = getCreatedBookingJsonPath(jsonPath);
            verifyResponseContainsCorrectData(getBooking, booking);
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
}
