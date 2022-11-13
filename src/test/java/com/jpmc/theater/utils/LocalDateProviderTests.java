package com.jpmc.theater.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocalDateProviderTests {
    @Test
    void Given_getCurrentDate_When_GetInstance_Then_ReturnCurrentDate() {
        assertEquals(LocalDate.now(), LocalDateProvider.getInstance().currentDate());
    }
}
