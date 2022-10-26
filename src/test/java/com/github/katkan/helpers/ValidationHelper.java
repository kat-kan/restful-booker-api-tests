package com.github.katkan.helpers;

import com.github.katkan.dto.request.BookingDto;
import io.restassured.path.json.JsonPath;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.github.katkan.helpers.JsonHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationHelper {

    public static void verifyCreateResponseContainsCorrectData(JsonPath responseJsonPath, BookingDto requestBooking) {
        assertThat(responseJsonPath.getString(BOOKING + FIRSTNAME)).isEqualTo(requestBooking.getFirstname());
        assertThat(responseJsonPath.getString(BOOKING + LASTNAME)).isEqualTo(requestBooking.getLastname());
        assertThat(responseJsonPath.getInt(BOOKING + TOTAL_PRICE)).isEqualTo(requestBooking.getTotalPrice());
        assertThat(responseJsonPath.getBoolean(BOOKING + DEPOSIT_PAID)).isEqualTo(requestBooking.isDepositPaid());
        assertThat(responseJsonPath.getString(BOOKING_BOOKING_DATES + CHECKIN)).isEqualTo(requestBooking.getBookingDates().getCheckin());
        assertThat(responseJsonPath.getString(BOOKING_BOOKING_DATES + CHECKOUT)).isEqualTo(requestBooking.getBookingDates().getCheckout());
        assertThat(responseJsonPath.getString(BOOKING + ADDITIONAL_NEEDS)).isEqualTo(requestBooking.getAdditionalNeeds());
    }

    public static void verifyResponseContainsCorrectData(JsonPath jsonPath, BookingDto bookingDto) {
        assertThat(jsonPath.getString(FIRSTNAME)).isEqualTo(bookingDto.getFirstname());
        assertThat(jsonPath.getString(LASTNAME)).isEqualTo(bookingDto.getLastname());
        assertThat(jsonPath.getInt(TOTAL_PRICE)).isEqualTo(bookingDto.getTotalPrice());
        assertThat(jsonPath.getBoolean(DEPOSIT_PAID)).isEqualTo(bookingDto.isDepositPaid());
        assertThat(jsonPath.getString(BOOKING_DATES + "." + CHECKIN)).isEqualTo(bookingDto.getBookingDates().getCheckin());
        assertThat(jsonPath.getString(BOOKING_DATES + "." + CHECKOUT)).isEqualTo(bookingDto.getBookingDates().getCheckout());
        assertThat(jsonPath.getString(ADDITIONAL_NEEDS)).isEqualTo(bookingDto.getAdditionalNeeds());
    }
}
