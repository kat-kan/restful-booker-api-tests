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

import static com.github.katkan.helpers.JsonHelper.*;
import static com.github.katkan.helpers.ValidationHelper.verifyResponseContainsCorrectData;
import static org.assertj.core.api.Assertions.assertThat;

public class PutBookingTest {

    private static String token;
    private static int bookingId;

    private BookingDto createBookingDto;

    @BeforeEach
    void setUp() {
        JSONObject authData = new JSONObject();
        authData.put(USERNAME, RestfulBookerProperties.getUsername());
        authData.put(PASSWORD, RestfulBookerProperties.getPassword());
        Response createTokenResponse = CreateTokenRequest.createTokenRequest(authData);
        token = createTokenResponse.jsonPath().getString("token");

        createBookingDto = new BookingDto();
    }

    @AfterEach
    void deleteBooking() {
        Response response = DeleteBookingRequest.deleteBookingRequest(bookingId, token);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
    }

    @Test
    @DisplayName("Modify booking with PUT method using valid data")
    void modifyBookingWithPUTUsingValidDataTest() {
        String updatedFirstname = "Oliver";
        String updatedLastname = "Booker";
        int updatedTotalPrice = 200;
        boolean updatedDepositPaid = false;
        String updatedCheckin = "2023-10-10";
        String updatedCheckout = "2023-11-01";
        String updatedAdditionalNeeds = "Breakfast";

        BookingDatesDto putBookingDatesDto = new BookingDatesDto();
        BookingDto putBookingDto  = new BookingDto();

        putBookingDatesDto.setCheckin(updatedCheckin);
        putBookingDatesDto.setCheckout(updatedCheckout);
        putBookingDto.setFirstname(updatedFirstname);
        putBookingDto.setLastname(updatedLastname);
        putBookingDto.setTotalPrice(updatedTotalPrice);
        putBookingDto.setDepositPaid(updatedDepositPaid);
        putBookingDto.setBookingDates(putBookingDatesDto);
        putBookingDto.setAdditionalNeeds(updatedAdditionalNeeds);

        Response createBookingResponse = CreateBookingRequest.createBookingRequest(createBookingDto);
        JsonPath jsonPath = createBookingResponse.jsonPath();
        bookingId = getBookingId(jsonPath);

        Response putBookingResponse = PutBookingRequest.putBookingRequest(putBookingDto, bookingId, token);
        assertThat(putBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        verifyResponseContainsCorrectData(putBookingResponse.jsonPath(), putBookingDto);
    }

    @Test
    @DisplayName("Modify booking with PUT method using invalid dates data")
    void modifyBookingWithPUTUsingInvalidDataTest() {
        String updatedFirstname = "Oliver";
        String updatedLastname = "Booker";
        int updatedTotalPrice = 200;
        boolean updatedDepositPaid = false;
        String updatedCheckin = "20231010";
        String updatedCheckout = "2023-1101";
        String updatedAdditionalNeeds = "Breakfast";

        BookingDatesDto putBookingDatesDto = new BookingDatesDto();
        BookingDto putBookingDto  = new BookingDto();

        putBookingDatesDto.setCheckin(updatedCheckin);
        putBookingDatesDto.setCheckout(updatedCheckout);
        putBookingDto.setFirstname(updatedFirstname);
        putBookingDto.setLastname(updatedLastname);
        putBookingDto.setTotalPrice(updatedTotalPrice);
        putBookingDto.setDepositPaid(updatedDepositPaid);
        putBookingDto.setBookingDates(putBookingDatesDto);
        putBookingDto.setAdditionalNeeds(updatedAdditionalNeeds);

        Response createBookingResponse = CreateBookingRequest.createBookingRequest(createBookingDto);
        JsonPath jsonPath = createBookingResponse.jsonPath();
        bookingId = getBookingId(jsonPath);

        Response putBookingResponse = PutBookingRequest.putBookingRequest(putBookingDto, bookingId, token);
        assertThat(putBookingResponse.htmlPath().getString("body")).isEqualTo("Invalid date");
    }
}
