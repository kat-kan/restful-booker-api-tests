package com.github.katkan.tests.e2e;

import com.github.katkan.dto.request.BookingDatesDto;
import com.github.katkan.dto.request.BookingDto;
import com.github.katkan.properties.RestfulBookerProperties;
import com.github.katkan.requests.auth.CreateTokenRequest;
import com.github.katkan.requests.bookings.CreateBookingRequest;
import com.github.katkan.requests.bookings.DeleteBookingRequest;
import com.github.katkan.requests.bookings.GetBookingRequest;
import com.github.katkan.requests.bookings.PatchBookingRequest;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import static com.github.katkan.helpers.BookingIdsHelper.getBookingId;
import static com.github.katkan.helpers.JsonHelper.*;
import static com.github.katkan.helpers.ValidationHelper.verifyCreateResponseContainsCorrectData;
import static com.github.katkan.helpers.ValidationHelper.verifyResponseContainsCorrectData;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UpdateBookingE2ETest {
    private static String token;
    private static int bookingId;
    private static BookingDto booking;

    @Order(1)
    @Test
    @DisplayName("Create token and authenticate")
    void createToken() {
        JSONObject authData = new JSONObject();
        authData.put(USERNAME, RestfulBookerProperties.getUsername());
        authData.put(PASSWORD, RestfulBookerProperties.getPassword());

        Response createTokenResponse = CreateTokenRequest.createTokenRequest(authData);

        assertThat(createTokenResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        token = createTokenResponse.jsonPath().getString(TOKEN);
    }

    @Order(2)
    @Test
    @DisplayName("Create booking with data from the customer")
    void createBooking() {
        String firstname = "Anne";
        String lastname = "Green";
        int totalPrice = 100;
        boolean depositPaid = false;
        String checkin = "2022-11-01";
        String checkout = "2022-11-10";
        String additionalNeeds = "";

        booking = new BookingDto();
        BookingDatesDto bookingDates = new BookingDatesDto();
        booking.setFirstname(firstname);
        booking.setLastname(lastname);
        booking.setTotalPrice(totalPrice);
        booking.setDepositPaid(depositPaid);
        bookingDates.setCheckin(checkin);
        bookingDates.setCheckout(checkout);
        booking.setBookingDates(bookingDates);
        booking.setAdditionalNeeds(additionalNeeds);

        Response createBookingResponse = CreateBookingRequest.createBookingRequest(booking);
        bookingId = getBookingId(createBookingResponse.jsonPath());

        assertThat(createBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        verifyCreateResponseContainsCorrectData(createBookingResponse.jsonPath(), booking);
    }

    @Order(3)
    @Test
    @DisplayName("Read created booking")
    void getBooking() {
        Response getBookingResponse = GetBookingRequest.getBookingRequest(bookingId);

        assertThat(getBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        verifyResponseContainsCorrectData(getBookingResponse.jsonPath(), booking);
    }

    @Order(4)
    @Test
    @DisplayName("Update deposit paid flag after customer paid the deposit")
    void updateBookingWithDepositPaid() {
        JSONObject updatedBookingProperty = new JSONObject();
        updatedBookingProperty.put(DEPOSIT_PAID, true);

        Response patchBookingResponse = PatchBookingRequest.patchBookingRequest(updatedBookingProperty, bookingId, token);
        assertThat(patchBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(patchBookingResponse.jsonPath().getBoolean(DEPOSIT_PAID)).isEqualTo(true);
    }

    @Order(5)
    @Test
    @DisplayName("Update booking with new additional need and total price")
    void updateBookingWithAdditionalNeedsAndTotalPrice() {
        JSONObject updatedBookingProperty = new JSONObject();
        int updatedTotalPrice = 250;
        String updatedAdditionalNeeds = "Breakfast & lunch";
        updatedBookingProperty.put(TOTAL_PRICE, updatedTotalPrice);
        updatedBookingProperty.put(ADDITIONAL_NEEDS, updatedAdditionalNeeds);

        Response patchBookingResponse = PatchBookingRequest.patchBookingRequest(updatedBookingProperty, bookingId, token);
        assertThat(patchBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(patchBookingResponse.jsonPath().getInt(TOTAL_PRICE)).isEqualTo(updatedTotalPrice);
        assertThat(patchBookingResponse.jsonPath().getString(ADDITIONAL_NEEDS)).isEqualTo(updatedAdditionalNeeds);
    }

    @Order(6)
    @Test
    @DisplayName("Delete booking after it's finished")
    void deleteBooking() {
        Response deleteBookingResponse = DeleteBookingRequest.deleteBookingRequest(bookingId, token);
        assertThat(deleteBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
    }
}
