package com.github.katkan.url;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestfulBookerUrls {

    public static final String BASE_URL = "https://restful-booker.herokuapp.com/";
    public static final String AUTH = "auth";
    public static final String BOOKING = "booking";
    public static final String PING = "ping";

    public static String getAuthUrl(){
        return AUTH;
    }

    public static String getBookingsUrl(){
        return BOOKING;
    }

    public static String getBookingUrl(int bookingId){
        return getBookingsUrl() + "/" + bookingId;
    }

    public static String getHealthCheckUrl(){
        return PING;
    }

    public static String createBookingUrl(){
        return BOOKING;
    }
}
