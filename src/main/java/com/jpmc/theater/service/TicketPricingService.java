package com.jpmc.theater.service;

import com.jpmc.theater.domain.Reservation;
import com.jpmc.theater.domain.Showing;

import java.math.BigDecimal;
import java.util.Map;

public class TicketPricingService {

    public BigDecimal calculateTotalFee(Reservation reservation) {
        Showing showing = reservation.getShowing();
        return getTicketPriceWithDiscount(showing).multiply(BigDecimal.valueOf(reservation.getAudienceCount()));
    }

    public BigDecimal getTicketPriceWithDiscount(Showing showing) {
        return showing.getMovieFee().subtract(getDiscount(showing));
    }

    private BigDecimal getDiscount(Showing showing) {
        BigDecimal specialDiscount = getSpecialDiscount(showing.getMovie().getSpecialCode(), showing.getMovieFee());
        BigDecimal sequenceDiscount = getSequenceDiscount(showing.getSequenceOfTheDay());
        return specialDiscount.max(sequenceDiscount);
    }

    private BigDecimal getSpecialDiscount(int specialCode, BigDecimal ticketPrice) {
        int MOVIE_CODE_SPECIAL = 1;
        boolean isSpecialMovie = MOVIE_CODE_SPECIAL == specialCode;
        BigDecimal specialDiscount = BigDecimal.ZERO;
        BigDecimal apply20PercentDiscount = ticketPrice.multiply(BigDecimal.valueOf(0.2));
        if (isSpecialMovie) {
            specialDiscount = apply20PercentDiscount;
        }
        return specialDiscount;
    }

    private BigDecimal getSequenceDiscount(int showSequence) {
        BigDecimal sequenceDiscount = BigDecimal.ZERO;
        var sequenceDiscountMap = showingSequenceDiscountMap();
        if (sequenceDiscountMap.containsKey(showSequence)) {
            sequenceDiscount = sequenceDiscountMap.get(showSequence);
        }
        return sequenceDiscount;
    }

    private Map<Integer, BigDecimal> showingSequenceDiscountMap() {
        return Map.of(
                1, dollarDiscount(3),
                2, dollarDiscount(2),
                7, dollarDiscount(1)
        );
    }

    private BigDecimal dollarDiscount(int dollar) {
        return BigDecimal.valueOf(dollar);
    }

}
