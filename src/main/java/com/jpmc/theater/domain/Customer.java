package com.jpmc.theater.domain;

import lombok.Getter;

@Getter
public class Customer {
    private final String name;

    public Customer(String name) {
        this.name = name;
    }
}