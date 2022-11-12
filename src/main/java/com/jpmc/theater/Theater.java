package com.jpmc.theater;

import java.math.RoundingMode;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Theater {
    LocalDateProvider provider;
    private final List<Showing> schedule;

    public Theater(LocalDateProvider provider) {
        this.provider = provider;
        this.schedule = Schedule.loader(provider);
    }

    public Reservation reserve(Customer customer, int sequence, int howManyTickets) {
        Showing showing;
        try {
            showing = schedule.get(sequence - 1);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            throw new IllegalStateException("not able to find any showing for given sequence " + sequence);
        }
        return new Reservation(customer, showing, howManyTickets);
    }

    public void printSchedule() {
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

    public static void main(String[] args) {
        Theater theater = new Theater(LocalDateProvider.getInstance());
        theater.printSchedule();
    }
}
