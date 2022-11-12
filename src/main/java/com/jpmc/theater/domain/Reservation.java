package com.jpmc.theater.domain;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Reservation {
    private final Customer customer;
    private final Showing showing;
    private final int audienceCount;

    public Reservation(Customer customer, Showing showing, int audienceCount) {
        this.customer = customer;
        this.showing = showing;
        this.audienceCount = audienceCount;
    }

    public BigDecimal totalFee() {
        return showing.getMovieFee().multiply(BigDecimal.valueOf(audienceCount));
    }
}