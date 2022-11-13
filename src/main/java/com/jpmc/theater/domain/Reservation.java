package com.jpmc.theater.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
public class Reservation {
    private final Customer customer;
    private final Showing showing;
    private final int audienceCount;
    @Setter
    private BigDecimal totalFee;

    public Reservation(Customer customer, Showing showing, int audienceCount) {
        this.customer = customer;
        this.showing = showing;
        this.audienceCount = audienceCount;
    }
}