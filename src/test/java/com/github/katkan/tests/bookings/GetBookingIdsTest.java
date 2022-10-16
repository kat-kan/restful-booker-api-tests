package com.github.katkan.tests.bookings;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class GetBookingIdsTest {

    @Test
    @DisplayName("Get ids for all bookings that exist")
    void getBookingIdsTest() {

        Response response = given()
                .when()
                .get("https://restful-booker.herokuapp.com/booking")
                .then()
                .extract()
                .response();

        JsonPath jsonPath = response.jsonPath();
        List<Integer> bookingIds = jsonPath.getList("bookingid");
        int bookingIdsSize = bookingIds.size();
        log.info("Current number of bookingIds: {}", bookingIdsSize);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(bookingIds).isNotEmpty();
        long uniqueIds = bookingIds.stream()
                .distinct()
                .count();
        assertThat(bookingIdsSize).isEqualTo(uniqueIds);
    }
}
