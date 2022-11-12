package com.jpmc.theater;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Schedule {
    public static List<Showing> loader(LocalDateProvider provider) {
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), BigDecimal.valueOf(12.5), 1);
        Movie turningRed = new Movie("Turning Red", Duration.ofMinutes(85), BigDecimal.valueOf(11), 0);
        Movie theBatMan = new Movie("The Batman", Duration.ofMinutes(95), BigDecimal.valueOf(9), 0);
        return List.of(
                new Showing(turningRed, 1, LocalDateTime.of(provider.currentDate(), LocalTime.of(9, 0))),
                new Showing(spiderMan, 2, LocalDateTime.of(provider.currentDate(), LocalTime.of(11, 0))),
                new Showing(theBatMan, 3, LocalDateTime.of(provider.currentDate(), LocalTime.of(12, 50))),
                new Showing(turningRed, 4, LocalDateTime.of(provider.currentDate(), LocalTime.of(14, 30))),
                new Showing(spiderMan, 5, LocalDateTime.of(provider.currentDate(), LocalTime.of(16, 10))),
                new Showing(theBatMan, 6, LocalDateTime.of(provider.currentDate(), LocalTime.of(17, 50))),
                new Showing(turningRed, 7, LocalDateTime.of(provider.currentDate(), LocalTime.of(19, 30))),
                new Showing(spiderMan, 8, LocalDateTime.of(provider.currentDate(), LocalTime.of(21, 10))),
                new Showing(theBatMan, 9, LocalDateTime.of(provider.currentDate(), LocalTime.of(23, 0)))
        );
    }

    public void print(LocalDateProvider provider, List<Showing> schedule) {
        String bar = "===================================================";
        System.out.println(provider.currentDate());
        System.out.println(bar);
        schedule.forEach(s ->
                System.out.println(s.getSequenceOfTheDay() + ": "
                        + s.getStartTime() + " "
                        + s.getMovie().getTitle() + " "
                        + humanReadableFormat(s.getMovie().getRunningTime())
                        + " $"
                        + s.getMovieFee().setScale(2, RoundingMode.UP))
        );
        System.out.println(bar);
    }

    private String humanReadableFormat(Duration duration) {
        long hour = duration.toHours();
        long remainingMin = duration.toMinutes() - TimeUnit.HOURS.toMinutes(duration.toHours());
        return String.format("(%s hour%s %s minute%s)", hour, handlePlural(hour), remainingMin, handlePlural(remainingMin));
    }

    private String handlePlural(long value) {
        return value == 1 ? "" : "s";
    }
}
