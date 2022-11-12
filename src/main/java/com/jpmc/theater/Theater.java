package com.jpmc.theater;

import java.util.List;

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
        new Schedule().print(provider,schedule);
    }

    public static void main(String[] args) {
        Theater theater = new Theater(LocalDateProvider.getInstance());
        theater.printSchedule();
    }
}
