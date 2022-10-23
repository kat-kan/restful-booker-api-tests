package com.github.katkan.tests.bookings;

import com.github.katkan.dto.request.PutBookingDatesRequestDto;
import com.github.katkan.dto.request.PutBookingRequestDto;
import com.github.katkan.properties.RestfulBookerProperties;
import com.github.katkan.requests.auth.CreateTokenRequest;
import com.github.katkan.requests.bookings.CreateBookingRequest;
import com.github.katkan.requests.bookings.PutBookingRequest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.katkan.helpers.JsonHelper.*;
import static com.github.katkan.helpers.JsonHelper.ADDITIONAL_NEEDS;
import static org.assertj.core.api.Assertions.assertThat;

public class PutBookingTest {

    private static String token;

    private String firstname = "John";
    private String lastname = "Kowalsky";
    private Integer totalPrice = 10150;
    private boolean depositPaid = true;
    private String checkin = "2023-01-01";
    private String checkout = "2023-02-01";
    private String additionalNeeds = "Lunch";

    private String updatedFirstname;
    private String updatedLastname;
    private int updatedTotalPrice;
    private boolean updatedDepositPaid;
    private String updatedCheckin;
    private String updatedCheckout;
    private String updatedAdditionalNeeds;

    @BeforeEach
    void auth() {
        JSONObject authData = new JSONObject();
        authData.put(USERNAME, RestfulBookerProperties.getUsername());
        authData.put(PASSWORD, RestfulBookerProperties.getPassword());
        Response createTokenResponse = CreateTokenRequest.createTokenRequest(authData);
        token = createTokenResponse.jsonPath().getString("token");
    }

    @Test
    @DisplayName("Modify booking with PUT method using valid data")
    void modifyBookingWithPUTUsingValidDataDtoTest() {
        JSONObject booking = getBookingJsonObject();

        updatedFirstname = "Oliver";
        updatedLastname = "Booker";
        updatedTotalPrice = 200;
        updatedDepositPaid = false;
        updatedCheckin = "2023-10-10";
        updatedCheckout = "2023-11-01";
        updatedAdditionalNeeds = "Breakfast";

        PutBookingDatesRequestDto bookingDatesDto = new PutBookingDatesRequestDto();
        PutBookingRequestDto bookingDto  = new PutBookingRequestDto();

        bookingDatesDto.setCheckin(updatedCheckin);
        bookingDatesDto.setCheckout(updatedCheckout);
        bookingDto.setFirstname(updatedFirstname);
        bookingDto.setLastname(updatedLastname);
        bookingDto.setTotalPrice(updatedTotalPrice);
        bookingDto.setDepositPaid(updatedDepositPaid);
        bookingDto.setPutBookingDatesRequestDto(bookingDatesDto);
        bookingDto.setAdditionalNeeds(updatedAdditionalNeeds);

        Response createBookingResponse = CreateBookingRequest.createBookingRequest(booking);
        JsonPath jsonPath = createBookingResponse.jsonPath();
        int bookingId = Integer.parseInt(jsonPath.getString(ID));

        Response putBookingResponse = PutBookingRequest.putBookingRequest(bookingDto, bookingId, token);
        putBookingResponse.prettyPeek();
        Assertions.assertThat(putBookingResponse.getStatusCode()).isEqualTo(200);
        verifyPutResponseContainsCorrectData(putBookingResponse.jsonPath(), bookingDto);

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

    private void verifyPutResponseContainsCorrectData(JsonPath jsonPath, PutBookingRequestDto bookingDto) {
        assertThat(jsonPath.getString(FIRSTNAME)).isEqualTo(bookingDto.getFirstname());
        assertThat(jsonPath.getString(LASTNAME)).isEqualTo(bookingDto.getLastname());
        assertThat(jsonPath.getInt(TOTAL_PRICE)).isEqualTo(bookingDto.getTotalPrice());
        assertThat(jsonPath.getBoolean(DEPOSIT_PAID)).isEqualTo(bookingDto.isDepositPaid());
        assertThat(jsonPath.getString(BOOKING_DATES + "." + CHECKIN)).isEqualTo(bookingDto.getPutBookingDatesRequestDto().getCheckin());
        assertThat(jsonPath.getString(BOOKING_DATES + "." + CHECKOUT)).isEqualTo(bookingDto.getPutBookingDatesRequestDto().getCheckout());
        assertThat(jsonPath.getString(ADDITIONAL_NEEDS)).isEqualTo(bookingDto.getAdditionalNeeds());
    }
}
