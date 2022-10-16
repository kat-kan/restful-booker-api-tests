package com.github.katkan.url;

public class RestfulBookerUrls {

    public static final String BASE_URL = "https://restful-booker.herokuapp.com/";
    public static final String BOOKING = "booking";
    public static final String PING = "ping";

    public static String getBookingsUrl(){
        return BASE_URL + BOOKING;
    }

    public static String getBookingUrl(int bookingId){
        return getBookingsUrl() + "/" + bookingId;
    }

    public static String getHealthCheckUrl(){
        return BASE_URL + PING;
    }
}
