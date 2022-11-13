package com.jpmc.theater.service;

import com.jpmc.theater.domain.Reservation;
import com.jpmc.theater.domain.Showing;
import com.jpmc.theater.utils.LocalDateProvider;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

public class TicketPricingService {
    private static final int MOVIE_CODE_SPECIAL = 1;
    private static final LocalDateTime START_DISCOUNT_TIME_RANGE = LocalDateTime.of(LocalDateProvider.getInstance().currentDate(), LocalTime.of(11, 0));
    private static final LocalDateTime END_DISCOUNT_TIME_RANGE = LocalDateTime.of(LocalDateProvider.getInstance().currentDate(), LocalTime.of(16, 0));
    private static final Map<Integer, BigDecimal> SEQUENCE_DISCOUNT_RATE = Map.of(
            1, BigDecimal.valueOf(3),
            2, BigDecimal.valueOf(2),
            7, BigDecimal.valueOf(1)
    );

    public BigDecimal calculateTotalFee(Reservation reservation) {
        Showing showing = reservation.getShowing();
        return getTicketPriceWithDiscount(showing).multiply(BigDecimal.valueOf(reservation.getAudienceCount()));
    }

    public BigDecimal getTicketPriceWithDiscount(Showing showing) {
        return showing.getMovie().getTicketPrice().subtract(getDiscount(showing));
    }

    private BigDecimal getDiscount(Showing showing) {
        BigDecimal maxShowVsSpecDiscount = getMaxPercentageDiscount(showing);
        BigDecimal sequenceDiscount = getSequenceDiscount(showing.getSequenceOfTheDay());
        return maxShowVsSpecDiscount.max(sequenceDiscount);
    }

    private BigDecimal getMaxPercentageDiscount(Showing showing) {
        BigDecimal showingTimeDiscount = getShowingTimeDiscount(showing);
        return showingTimeDiscount.compareTo(BigDecimal.ZERO) > 0
                ? showingTimeDiscount
                : getSpecialDiscount(showing.getMovie().getSpecialCode(), showing.getMovie().getTicketPrice());
    }

    private BigDecimal getShowingTimeDiscount(Showing showing) {
        BigDecimal showingTimeDiscount = BigDecimal.ZERO;
        LocalDateTime showStartTime = showing.getShowStartTime();
        boolean isBetweenDiscountTimeRange = showStartTime.isAfter(START_DISCOUNT_TIME_RANGE) && showStartTime.isBefore(END_DISCOUNT_TIME_RANGE);
        if (isBetweenDiscountTimeRange) {
            showingTimeDiscount = showing.getMovie().getTicketPrice().multiply(BigDecimal.valueOf(0.25));
        }
        return showingTimeDiscount;
    }

    private BigDecimal getSpecialDiscount(int specialCode, BigDecimal ticketPrice) {
        BigDecimal specialDiscount = BigDecimal.ZERO;
        if (MOVIE_CODE_SPECIAL == specialCode) {
            specialDiscount = ticketPrice.multiply(BigDecimal.valueOf(0.2));
        }
        return specialDiscount;
    }

    private BigDecimal getSequenceDiscount(int showSequence) {
        return SEQUENCE_DISCOUNT_RATE.getOrDefault(showSequence, BigDecimal.ZERO);
    }
}
