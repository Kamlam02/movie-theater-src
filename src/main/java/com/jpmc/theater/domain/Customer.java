package com.jpmc.theater.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Customer {
    private final String name;

    public Customer(String name) {
        this.name = name;
    }
}