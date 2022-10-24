package com.github.katkan.tests.bookings;

import com.github.katkan.dto.request.BookingDatesRequestDto;
import com.github.katkan.dto.request.BookingRequestDto;
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
import static org.assertj.core.api.Assertions.assertThat;

public class PutBookingTest {

    private static String token;
    private static int bookingId;

    private BookingRequestDto createBookingDto;

    @BeforeEach
    void setUp() {
        JSONObject authData = new JSONObject();
        authData.put(USERNAME, RestfulBookerProperties.getUsername());
        authData.put(PASSWORD, RestfulBookerProperties.getPassword());
        Response createTokenResponse = CreateTokenRequest.createTokenRequest(authData);
        token = createTokenResponse.jsonPath().getString("token");

        createBookingDto = new BookingRequestDto();
    }

    @AfterEach
    void deleteBooking() {
        Response response = DeleteBookingRequest.deleteBookingRequest(bookingId, token);
        assertThat(response.statusCode()).isEqualTo(201);
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

        BookingDatesRequestDto putBookingDatesDto = new BookingDatesRequestDto();
        BookingRequestDto putBookingDto  = new BookingRequestDto();

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
        verifyPutResponseContainsCorrectData(putBookingResponse.jsonPath(), putBookingDto);
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

        BookingDatesRequestDto putBookingDatesDto = new BookingDatesRequestDto();
        BookingRequestDto putBookingDto  = new BookingRequestDto();

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

    private void verifyPutResponseContainsCorrectData(JsonPath jsonPath, BookingRequestDto bookingDto) {
        assertThat(jsonPath.getString(FIRSTNAME)).isEqualTo(bookingDto.getFirstname());
        assertThat(jsonPath.getString(LASTNAME)).isEqualTo(bookingDto.getLastname());
        assertThat(jsonPath.getInt(TOTAL_PRICE)).isEqualTo(bookingDto.getTotalPrice());
        assertThat(jsonPath.getBoolean(DEPOSIT_PAID)).isEqualTo(bookingDto.isDepositPaid());
        assertThat(jsonPath.getString(BOOKING_DATES + "." + CHECKIN)).isEqualTo(bookingDto.getBookingDates().getCheckin());
        assertThat(jsonPath.getString(BOOKING_DATES + "." + CHECKOUT)).isEqualTo(bookingDto.getBookingDates().getCheckout());
        assertThat(jsonPath.getString(ADDITIONAL_NEEDS)).isEqualTo(bookingDto.getAdditionalNeeds());
    }
}
