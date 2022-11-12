package com.jpmc.theater;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Objects;

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

    public String getTitle() {
        return title;
    }

    public Duration getRunningTime() {
        return runningTime;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public BigDecimal calculateTicketPrice(Showing showing) {
        return ticketPrice.subtract(getDiscount(showing.getSequenceOfTheDay()));
    }

    private BigDecimal getDiscount(int showSequence) {
        BigDecimal specialDiscount = getSpecialDiscount();
        BigDecimal sequenceDiscount = getSequenceDiscount(showSequence);
        return specialDiscount.max(sequenceDiscount);}

    private BigDecimal getSpecialDiscount() {
        int MOVIE_CODE_SPECIAL = 1;
        boolean isSpecialMovie = MOVIE_CODE_SPECIAL == specialCode;
        BigDecimal specialDiscount = BigDecimal.ZERO;
        if (isSpecialMovie) {
            specialDiscount = ticketPrice.multiply(BigDecimal.valueOf(0.2));  // 20% discount for special movie
        }
        return specialDiscount;
    }

    private static BigDecimal getSequenceDiscount(int showSequence) {
        BigDecimal sequenceDiscount = BigDecimal.ZERO;
        if (showSequence == 1) {
            sequenceDiscount = BigDecimal.valueOf(3); // $3 discount for 1st show
        } else if (showSequence == 2) {
            sequenceDiscount = BigDecimal.valueOf(2); // $2 discount for 2nd show
        }
        return sequenceDiscount;
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