package com.jpmc.theater.domain;

import com.jpmc.theater.DiscountService;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Objects;

@Getter
public class Movie {
    private final String title;
    private String description;
    private final Duration runningTime;
    private final BigDecimal ticketPrice;
    private final int specialCode;

    public Movie(String title, Duration runningTime, BigDecimal ticketPrice, int specialCode) {
        this.title = title;
        this.runningTime = runningTime;
        this.ticketPrice = ticketPrice;
        this.specialCode = specialCode;
    }

    public BigDecimal calculateTicketPrice(Showing showing) {
        return ticketPrice.subtract(DiscountService.getDiscount(showing));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return movie.ticketPrice.compareTo(ticketPrice) == 0
                && Objects.equals(title, movie.title)
                && Objects.equals(description, movie.description)
                && Objects.equals(runningTime, movie.runningTime)
                && Objects.equals(specialCode, movie.specialCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, runningTime, ticketPrice, specialCode);
    }
}