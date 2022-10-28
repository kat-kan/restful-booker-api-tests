package com.github.katkan.tests.bookings;

import com.github.katkan.dto.request.BookingDatesDto;
import com.github.katkan.dto.request.BookingDto;
import com.github.katkan.properties.RestfulBookerProperties;
import com.github.katkan.requests.auth.CreateTokenRequest;
import com.github.katkan.requests.bookings.CreateBookingRequest;
import com.github.katkan.requests.bookings.DeleteBookingRequest;
import com.github.katkan.requests.bookings.PutBookingRequest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.katkan.helpers.BookingIdsHelper.getBookingId;
import static com.github.katkan.helpers.JsonHelper.*;
import static com.github.katkan.helpers.ValidationHelper.verifyResponseContainsCorrectData;
import static org.assertj.core.api.Assertions.assertThat;

public class PutBookingTest {

    private static String token;
    private static int bookingId;

    private BookingDto booking;

    @BeforeEach
    void setUp() {
        JSONObject authData = new JSONObject();
        authData.put(USERNAME, RestfulBookerProperties.getUsername());
        authData.put(PASSWORD, RestfulBookerProperties.getPassword());
        Response createTokenResponse = CreateTokenRequest.createTokenRequest(authData);
        token = createTokenResponse.jsonPath().getString(TOKEN);

        booking = new BookingDto();
    }

    @AfterEach
    void deleteBooking() {
        Response deleteBookingResponse = DeleteBookingRequest.deleteBookingRequest(bookingId, token);
        assertThat(deleteBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
    }

    @Test
    @DisplayName("Update booking using valid data")
    void updateBookingUsingValidDataTest() {
        String updatedFirstname = "Oliver";
        String updatedLastname = "Booker";
        int updatedTotalPrice = 200;
        boolean updatedDepositPaid = false;
        String updatedCheckin = "2023-10-10";
        String updatedCheckout = "2023-11-01";
        String updatedAdditionalNeeds = "Breakfast";

        BookingDatesDto updateBookingDates = new BookingDatesDto();
        BookingDto updateBooking = new BookingDto();

        updateBookingDates.setCheckin(updatedCheckin);
        updateBookingDates.setCheckout(updatedCheckout);
        updateBooking.setFirstname(updatedFirstname);
        updateBooking.setLastname(updatedLastname);
        updateBooking.setTotalPrice(updatedTotalPrice);
        updateBooking.setDepositPaid(updatedDepositPaid);
        updateBooking.setBookingDates(updateBookingDates);
        updateBooking.setAdditionalNeeds(updatedAdditionalNeeds);

        Response createBookingResponse = CreateBookingRequest.createBookingRequest(booking);
        JsonPath createBookingJsonPath = createBookingResponse.jsonPath();
        bookingId = getBookingId(createBookingJsonPath);

        Response putBookingResponse = PutBookingRequest.putBookingRequest(updateBooking, bookingId, token);
        assertThat(putBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        verifyResponseContainsCorrectData(putBookingResponse.jsonPath(), updateBooking);
    }

    @Test
    @DisplayName("Update booking using invalid dates data")
    void updateBookingUsingInvalidDataTest() {
        String updatedFirstname = "Oliver";
        String updatedLastname = "Booker";
        int updatedTotalPrice = 200;
        boolean updatedDepositPaid = false;
        String updatedCheckin = "20231010";
        String updatedCheckout = "2023-1101";
        String updatedAdditionalNeeds = "Breakfast";

        BookingDatesDto updateBookingDates = new BookingDatesDto();
        BookingDto updateBooking = new BookingDto();

        updateBookingDates.setCheckin(updatedCheckin);
        updateBookingDates.setCheckout(updatedCheckout);
        updateBooking.setFirstname(updatedFirstname);
        updateBooking.setLastname(updatedLastname);
        updateBooking.setTotalPrice(updatedTotalPrice);
        updateBooking.setDepositPaid(updatedDepositPaid);
        updateBooking.setBookingDates(updateBookingDates);
        updateBooking.setAdditionalNeeds(updatedAdditionalNeeds);

        Response createBookingResponse = CreateBookingRequest.createBookingRequest(booking);
        JsonPath createBookingJsonPath = createBookingResponse.jsonPath();
        bookingId = getBookingId(createBookingJsonPath);

        Response putBookingResponse = PutBookingRequest.putBookingRequest(updateBooking, bookingId, token);
        assertThat(putBookingResponse.htmlPath().getString("body")).isEqualTo("Invalid date");
    }
}
