package com.github.katkan.tests.e2e;

import com.github.katkan.dto.request.BookingDatesDto;
import com.github.katkan.dto.request.BookingDto;
import com.github.katkan.helpers.ValidationHelper;
import com.github.katkan.properties.RestfulBookerProperties;
import com.github.katkan.requests.auth.CreateTokenRequest;
import com.github.katkan.requests.bookings.CreateBookingRequest;
import com.github.katkan.requests.bookings.DeleteBookingRequest;
import com.github.katkan.requests.bookings.GetBookingRequest;
import com.github.katkan.requests.bookings.PatchBookingRequest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import static com.github.katkan.helpers.JsonHelper.*;
import static com.github.katkan.helpers.ValidationHelper.verifyResponseContainsCorrectData;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UpdateBookingE2ETest {
    private static String token;
    private static int bookingId;
    private static BookingDto booking;
    private static BookingDatesDto bookingDates;

    private static String firstname;
    private static String lastname;
    private static int totalPrice;
    private static boolean depositPaid;
    private static String checkin;
    private static String checkout;
    private static String additionalNeeds;

    @Order(1)
    @Test
    @DisplayName("Create token and authenticate")
    void createTokenTest() {
        JSONObject authData = new JSONObject();
        authData.put(USERNAME, RestfulBookerProperties.getUsername());
        authData.put(PASSWORD, RestfulBookerProperties.getPassword());

        Response createTokenResponse = CreateTokenRequest.createTokenRequest(authData);

        assertThat(createTokenResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        token = createTokenResponse.jsonPath().getString(TOKEN);
        log.info("Auth token created");
    }

    @Order(2)
    @Test
    @DisplayName("Create booking with data from the customer")
    void createBookingTest() {
        firstname = "Anne";
        lastname = "Green";
        totalPrice = 100;
        depositPaid = false;
        checkin = "2022-11-01";
        checkout = "2022-11-10";
        additionalNeeds = "";

        booking = new BookingDto();
        bookingDates = new BookingDatesDto();
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
        verifyCreateResponseContainsCorrectData(createBookingResponse.jsonPath());
    }

    @Order(3)
    @Test
    @DisplayName("Read created booking")
    void getBookingTest() {
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
    void deleteBookingTest() {
        Response response = DeleteBookingRequest.deleteBookingRequest(bookingId, token);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
    }

    private void verifyCreateResponseContainsCorrectData(JsonPath jsonPath) {
        assertThat(jsonPath.getString(BOOKING + FIRSTNAME)).isEqualTo(firstname);
        assertThat(jsonPath.getString(BOOKING + LASTNAME)).isEqualTo(lastname);
        assertThat(jsonPath.getInt(BOOKING + TOTAL_PRICE)).isEqualTo(totalPrice);
        assertThat(jsonPath.getBoolean(BOOKING + DEPOSIT_PAID)).isEqualTo(depositPaid);
        assertThat(jsonPath.getString(BOOKING_BOOKING_DATES + CHECKIN)).isEqualTo(checkin);
        assertThat(jsonPath.getString(BOOKING_BOOKING_DATES + CHECKOUT)).isEqualTo(checkout);
        assertThat(jsonPath.getString(BOOKING + ADDITIONAL_NEEDS)).isEqualTo(additionalNeeds);
    }
}
