package com.jpmc.theater.domain;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Customer {
    private final String id;
    private final String name;

    public Customer(String id, String name) {
        this.id = id; // NOTE - id is not used anywhere at the moment
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(name, customer.name) && Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }

    @Override
    public String toString() {
        return "name: " + name;
    }
}