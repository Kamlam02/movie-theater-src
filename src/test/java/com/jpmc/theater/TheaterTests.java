package com.jpmc.theater;

import com.jpmc.theater.domain.Customer;
import com.jpmc.theater.domain.Reservation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TheaterTests {
    @Test
    void totalFeeForCustomer() {
        Theater theater = new Theater();
        Customer john = new Customer("John Doe");
        Reservation reservation = theater.reserve(john, 2, 4);
        Assertions.assertEquals(reservation.getTotalFee(), BigDecimal.valueOf(40).setScale(2, RoundingMode.UP));
    }

    @Test
    void printMovieSchedule() {
        Theater theater = new Theater();
        theater.printSchedule();
    }
}
