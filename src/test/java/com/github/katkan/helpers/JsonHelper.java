package com.github.katkan.helpers;

import io.restassured.path.json.JsonPath;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonHelper {
    public static final String FIRSTNAME = "firstname";
    public static final String LASTNAME = "lastname";
    public static final String TOTAL_PRICE = "totalprice";
    public static final String DEPOSIT_PAID = "depositpaid";
    public static final String CHECKIN = "checkin";
    public static final String CHECKOUT = "checkout";
    public static final String ADDITIONAL_NEEDS = "additionalneeds";
    public static final String BOOKING_DATES = "bookingdates";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String ID = "bookingid";

    public static final String BOOKING = "booking.";
    public static final String BOOKING_BOOKING_DATES = "booking.bookingdates.";

    public static final String TOKEN = "token";

    public static final List<String> jsonKeys = List.of(FIRSTNAME, LASTNAME,
            TOTAL_PRICE, DEPOSIT_PAID, BOOKING_DATES + "." + CHECKIN, BOOKING_DATES + "." + CHECKOUT, ADDITIONAL_NEEDS);

    public static int getBookingId(JsonPath jsonPath) {
        return Integer.parseInt(jsonPath.getString(ID));
    }
}
