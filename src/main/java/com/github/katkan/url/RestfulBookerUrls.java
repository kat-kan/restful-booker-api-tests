package com.github.katkan.url;

public class RestfulBookerUrls {

    public static final String BASE_URL = "https://restful-booker.herokuapp.com/";
    public static final String AUTH = "auth";
    public static final String BOOKING = "booking";
    public static final String PING = "ping";

    public static String getAuthUrl(){
        return BASE_URL + AUTH;
    }

    public static String getBookingsUrl(){
        return BASE_URL + BOOKING;
    }

    public static String getBookingUrl(int bookingId){
        return getBookingsUrl() + "/" + bookingId;
    }

    public static String deleteBookingUrl(int bookingId){
        return BASE_URL + BOOKING + "/" + bookingId;
    }

    public static String getHealthCheckUrl(){
        return BASE_URL + PING;
    }

    public static String createBookingUrl(){
        return BASE_URL + BOOKING;
    }
}
