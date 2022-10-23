package com.github.katkan.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PutBookingRequestDto {
    private String firstname = "John";
    private String lastname = "Kowalsky";
    @JsonProperty("totalprice")
    private Integer totalPrice = 10150;
    @JsonProperty("depositpaid")
    private boolean depositPaid = true;
    @JsonProperty("bookingdates")
    private PutBookingDatesRequestDto putBookingDatesRequestDto;
    @JsonProperty("additionalneeds")
    private String additionalNeeds = "Lunch";
}
