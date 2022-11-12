package com.jpmc.theater;

import java.time.LocalDate;

public class LocalDateProvider {
    private static final LocalDateProvider INSTANCE = new LocalDateProvider();

    private LocalDateProvider() {
    }

    public static LocalDateProvider getInstance() {
        return INSTANCE;
    }

    public LocalDate currentDate() {
        return LocalDate.now();
    }
}
