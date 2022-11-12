package com.jpmc.theater;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Showing {
    private final Movie movie;
    private final int sequenceOfTheDay;
    private final LocalDateTime showStartTime;

    public Showing(Movie movie, int sequenceOfTheDay, LocalDateTime showStartTime) {
        this.movie = movie;
        this.sequenceOfTheDay = sequenceOfTheDay;
        this.showStartTime = showStartTime;
    }

    public Movie getMovie() {
        return movie;
    }

    public LocalDateTime getStartTime() {
        return showStartTime;
    }

    public boolean isSequence(int sequence) {
        return this.sequenceOfTheDay == sequence;
    }

    public BigDecimal getMovieFee() {
        return movie.getTicketPrice();
    }

    public int getSequenceOfTheDay() {
        return sequenceOfTheDay;
    }

    private BigDecimal calculateFee(int audienceCount) {
        return movie.calculateTicketPrice(this).multiply(BigDecimal.valueOf(audienceCount));
    }
}
