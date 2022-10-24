package com.github.katkan.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BookingRequestDto {
    private String firstname = "John";
    private String lastname = "Kowalsky";
    @JsonProperty("totalprice")
    private int totalPrice = 10150;
    @JsonProperty("depositpaid")
    private boolean depositPaid = true;
    @JsonProperty("bookingdates")
    private BookingDatesRequestDto bookingDates = new BookingDatesRequestDto();
    @JsonProperty("additionalneeds")
    private String additionalNeeds = "Lunch";
}
