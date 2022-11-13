package com.jpmc.theater.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.Duration;

@Getter
public class Movie {
    private final String title;
    private final Duration runningTime;
    private final BigDecimal ticketPrice;
    private final int specialCode;

    public Movie(String title, Duration runningTime, BigDecimal ticketPrice, int specialCode) {
        this.title = title;
        this.runningTime = runningTime;
        this.ticketPrice = ticketPrice;
        this.specialCode = specialCode;
    }
}