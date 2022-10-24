package com.github.katkan.dto.request;

import lombok.Data;

@Data
public class BookingDatesRequestDto {
    private String checkin = "2023-01-01";
    private String checkout = "2023-02-01";
}
