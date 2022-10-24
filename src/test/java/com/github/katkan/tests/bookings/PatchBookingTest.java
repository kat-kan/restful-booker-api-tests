package com.github.katkan.tests.bookings;

import com.github.katkan.dto.request.BookingDatesRequestDto;
import com.github.katkan.dto.request.BookingRequestDto;
import com.github.katkan.properties.RestfulBookerProperties;
import com.github.katkan.requests.auth.CreateTokenRequest;
import com.github.katkan.requests.bookings.CreateBookingRequest;
import com.github.katkan.requests.bookings.DeleteBookingRequest;
import com.github.katkan.requests.bookings.PatchBookingRequest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.katkan.helpers.JsonHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PatchBookingTest {

    private static String token;
    private static int bookingId;

    private BookingRequestDto createBookingDto;
    private BookingRequestDto patchBookingDto;

    @BeforeEach
    void auth() {
        JSONObject authData = new JSONObject();
        authData.put(USERNAME, RestfulBookerProperties.getUsername());
        authData.put(PASSWORD, RestfulBookerProperties.getPassword());
        Response createTokenResponse = CreateTokenRequest.createTokenRequest(authData);
        token = createTokenResponse.jsonPath().getString("token");

        createBookingDto = new BookingRequestDto();
        patchBookingDto = new BookingRequestDto();
    }

    @AfterEach
    void deleteBooking() {
        Response response = DeleteBookingRequest.deleteBookingRequest(bookingId, token);
        assertThat(response.statusCode()).isEqualTo(201);
    }

    @Test
    @DisplayName("Partially update booking with valid firstname")
    void partiallyUpdateBookingWithValidFirstname() {
        Response createBookingResponse = CreateBookingRequest.createBookingRequest(createBookingDto);
        JsonPath jsonPath = createBookingResponse.jsonPath();
        bookingId = getBookingId(jsonPath);

        String updatedFirstname = "Lila";
        patchBookingDto.setFirstname(updatedFirstname);

        Response patchBookingResponse = PatchBookingRequest.patchBookingRequest(patchBookingDto, bookingId, token);

        assertThat(patchBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(patchBookingResponse.jsonPath().getString(FIRSTNAME)).isEqualTo(updatedFirstname);
    }

    @Test
    @DisplayName("Partially update booking with valid lastname")
    void partiallyUpdateBookingWithValidLastname() {
        Response createBookingResponse = CreateBookingRequest.createBookingRequest(createBookingDto);
        JsonPath jsonPath = createBookingResponse.jsonPath();
        bookingId = getBookingId(jsonPath);

        String updatedLastname = "Jones";
        patchBookingDto.setLastname(updatedLastname);

        Response patchBookingResponse = PatchBookingRequest.patchBookingRequest(patchBookingDto, bookingId, token);

        assertThat(patchBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(patchBookingResponse.jsonPath().getString(LASTNAME)).isEqualTo(updatedLastname);
    }

    @Test
    @DisplayName("Partially update booking with valid total price")
    void partiallyUpdateBookingWithValidTotalPriceValue() {
        Response createBookingResponse = CreateBookingRequest.createBookingRequest(createBookingDto);
        JsonPath jsonPath = createBookingResponse.jsonPath();
        bookingId = getBookingId(jsonPath);

        int updatedTotalPrice = 2678;
        patchBookingDto.setTotalPrice(updatedTotalPrice);

        Response patchBookingResponse = PatchBookingRequest.patchBookingRequest(patchBookingDto, bookingId, token);

        assertThat(patchBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(patchBookingResponse.jsonPath().getInt(TOTAL_PRICE)).isEqualTo(updatedTotalPrice);
    }

    @Test
    @DisplayName("Partially update booking with valid deposit paid")
    void partiallyUpdateBookingWithValidDepositPaidValue() {
        Response createBookingResponse = CreateBookingRequest.createBookingRequest(createBookingDto);
        JsonPath jsonPath = createBookingResponse.jsonPath();
        bookingId = getBookingId(jsonPath);

        boolean updatedDepositPaid = false;
        patchBookingDto.setDepositPaid(updatedDepositPaid);

        Response patchBookingResponse = PatchBookingRequest.patchBookingRequest(patchBookingDto, bookingId, token);

        assertThat(patchBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(patchBookingResponse.jsonPath().getBoolean(DEPOSIT_PAID)).isEqualTo(updatedDepositPaid);
    }

    @Test
    @DisplayName("Partially update booking with valid checkin date")
    void partiallyUpdateBookingWithValidCheckinDate() {
        Response createBookingResponse = CreateBookingRequest.createBookingRequest(createBookingDto);
        JsonPath jsonPath = createBookingResponse.jsonPath();
        bookingId = getBookingId(jsonPath);

        BookingDatesRequestDto bookingDatesDto = new BookingDatesRequestDto();
        String updatedCheckin = "2022-11-20";
        bookingDatesDto.setCheckin(updatedCheckin);
        patchBookingDto.setBookingDates(bookingDatesDto);

        Response patchBookingResponse = PatchBookingRequest.patchBookingRequest(patchBookingDto, bookingId, token);

        assertThat(patchBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(patchBookingResponse.jsonPath().getString(BOOKING_DATES + "." + CHECKIN)).isEqualTo(updatedCheckin);
    }

    @Test
    @DisplayName("Partially update booking with valid checkout date")
    void partiallyUpdateBookingWithValidCheckoutDate() {
        Response createBookingResponse = CreateBookingRequest.createBookingRequest(createBookingDto);
        JsonPath jsonPath = createBookingResponse.jsonPath();
        bookingId = getBookingId(jsonPath);

        BookingDatesRequestDto bookingDatesDto = new BookingDatesRequestDto();
        String updatedCheckout = "2022-10-31";
        bookingDatesDto.setCheckout(updatedCheckout);
        patchBookingDto.setBookingDates(bookingDatesDto);

        Response patchBookingResponse = PatchBookingRequest.patchBookingRequest(patchBookingDto, bookingId, token);

        assertThat(patchBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(patchBookingResponse.jsonPath().getString(BOOKING_DATES + "." + CHECKOUT)).isEqualTo(updatedCheckout);
    }

    @Test
    @DisplayName("Partially update booking with valid additional needs")
    void partiallyUpdateBookingWithValidAdditionalNeeds() {
        Response createBookingResponse = CreateBookingRequest.createBookingRequest(createBookingDto);
        JsonPath jsonPath = createBookingResponse.jsonPath();
        bookingId = getBookingId(jsonPath);

        String updatedAdditionalNeeds = "Breakfast in the hotel room";
        patchBookingDto.setAdditionalNeeds(updatedAdditionalNeeds);

        Response patchBookingResponse = PatchBookingRequest.patchBookingRequest(patchBookingDto, bookingId, token);

        assertThat(patchBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(patchBookingResponse.jsonPath().getString(ADDITIONAL_NEEDS)).isEqualTo(updatedAdditionalNeeds);
    }

    @Test
    @DisplayName("Partially update booking with two valid fields - firstname and additional needs")
    void partiallyUpdateBookingWithTwoValidFields() {
        Response createBookingResponse = CreateBookingRequest.createBookingRequest(createBookingDto);
        JsonPath jsonPath = createBookingResponse.jsonPath();
        bookingId = getBookingId(jsonPath);

        String updatedFirstname = "Bobby";
        String updatedAdditionalNeeds = "Champaigne in the room";
        patchBookingDto.setFirstname(updatedFirstname);
        patchBookingDto.setFirstname(updatedAdditionalNeeds);

        Response patchBookingResponse = PatchBookingRequest.patchBookingRequest(patchBookingDto, bookingId, token);

        assertThat(patchBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(patchBookingResponse.jsonPath().getString(FIRSTNAME)).isEqualTo(updatedFirstname);
        assertThat(patchBookingResponse.jsonPath().getString(ADDITIONAL_NEEDS)).isEqualTo(updatedAdditionalNeeds);
    }

    private int getBookingId(JsonPath jsonPath) {
        return Integer.parseInt(jsonPath.getString(ID));
    }
}
