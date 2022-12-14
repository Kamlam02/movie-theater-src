package com.jpmc.theater;

import com.jpmc.theater.domain.Customer;
import com.jpmc.theater.domain.Reservation;
import com.jpmc.theater.domain.Showing;
import com.jpmc.theater.utils.MovieScheduleHandler;
import com.jpmc.theater.service.TicketPricingService;
import com.jpmc.theater.utils.LocalDateProvider;

import java.util.List;

public class Theater {
    private final List<Showing> showings;

    public Theater() {
        this.showings = MovieScheduleHandler.initShowings(LocalDateProvider.getInstance());
    }

    public Reservation reserve(Customer customer, int sequence, int howManyTickets) {
        try {
            Showing showing = showings.get(sequence - 1);
            Reservation reservation = new Reservation(customer, showing, howManyTickets);
            reservation.setTotalFee(new TicketPricingService().calculateTotalFee(reservation));
            return reservation;
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            throw new IllegalStateException("not able to find any showing for given sequence " + sequence);
        }
    }

    public void printSchedule() {
        MovieScheduleHandler.printShowingsText(showings);
        new MovieScheduleHandler().printShowingsJson(showings);
    }

    public static void main(String[] args) {
        Theater theater = new Theater();
        theater.printSchedule();
    }
}
