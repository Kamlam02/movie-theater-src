package com.jpmc.theater.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

public class ReservationTests {

    @Test
    void totalFee() {
        var customer = new Customer("unused-id", "John Doe");
        var showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), BigDecimal.valueOf(12.5), 1),
                1,
                LocalDateTime.now()
        );
        Assertions.assertEquals(new Reservation(customer, showing, 3).totalFee(), BigDecimal.valueOf(37.5));
    }
}