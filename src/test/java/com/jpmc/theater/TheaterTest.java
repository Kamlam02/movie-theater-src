package com.jpmc.theater;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jpmc.theater.domain.Customer;
import com.jpmc.theater.domain.Reservation;
import com.jpmc.theater.domain.Showing;
import com.jpmc.theater.utils.LocalDateProvider;
import com.jpmc.theater.utils.MovieScheduleHandler;
import com.jpmc.theater.utils.PricingUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TheaterTest {

    Theater underTest = new Theater();

    @Test
    void Given_NewReservationRequest_When_Reserve_Then_ReturnReservationPrice() {
        BigDecimal expectedValue = PricingUtil.getBigDecimal(40);
        Customer john = new Customer("John Doe");
        Reservation reservation = underTest.reserve(john, 2, 4);
        assertEquals(expectedValue, reservation.getTotalFee());
    }

    @Test
    void Given_NewReservationRequest_When_Reserve_Then_ReturnCustomerName() {
        String expectedValue = "John Doe";
        Customer john = new Customer(expectedValue);
        Reservation reservation = underTest.reserve(john, 2, 4);
        assertEquals(expectedValue, reservation.getCustomer().getName());
    }

    @Test
    void Given_InvalidSequence_When_Reserve_Then_ThrowIllegalStateException() {
        Customer john = new Customer("John Doe");
        Assertions.assertThrows(IllegalStateException.class, () ->underTest.reserve(john, 0, 4));
    }

    @Test
    void Given_TextMovieSchedule_When_PrintSchedule_Then_PrintText() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String expectedValue = getExpectedTextStringValue();
        System.out.println(expectedValue);
        underTest.printSchedule();
        assertTrue(outContent.toString().contains(expectedValue));
    }

    private String getExpectedTextStringValue() {
        List<Showing> showings = MovieScheduleHandler.initShowings(LocalDateProvider.getInstance());
        return MovieScheduleHandler.getShowingReadableString(showings.get(0));
    }

    @Test
    void Given_JsonMovieSchedule_When_PrintSchedule_Then_PrintJson() {
        try {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));
            String expectedValue = getExpectedJsonStringValue();
            underTest.printSchedule();
            assertTrue(outContent.toString().contains(expectedValue));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getExpectedJsonStringValue() throws JsonProcessingException {
        List<Showing> showings = MovieScheduleHandler.initShowings(LocalDateProvider.getInstance());
        ObjectMapper objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(showings);
    }
}