package com.jpmc.theater;

import com.jpmc.theater.domain.Showing;

import java.math.BigDecimal;

public class DiscountService {

    public static BigDecimal getDiscount(Showing showing) {
        BigDecimal specialDiscount = getSpecialDiscount(showing.getMovie().getSpecialCode(), showing.getMovieFee());
        BigDecimal sequenceDiscount = getSequenceDiscount(showing.getSequenceOfTheDay());
        return specialDiscount.max(sequenceDiscount);
    }

    private static BigDecimal getSpecialDiscount(int specialCode, BigDecimal ticketPrice) {
        int MOVIE_CODE_SPECIAL = 1;
        boolean isSpecialMovie = MOVIE_CODE_SPECIAL == specialCode;
        BigDecimal specialDiscount = BigDecimal.ZERO;
        if (isSpecialMovie) {
            specialDiscount = ticketPrice.multiply(BigDecimal.valueOf(0.2));  // 20% discount for special movie
        }
        return specialDiscount;
    }

    private static BigDecimal getSequenceDiscount(int showSequence) {
        BigDecimal sequenceDiscount = BigDecimal.ZERO;
        if (showSequence == 1) {
            sequenceDiscount = BigDecimal.valueOf(3); // $3 discount for 1st show
        } else if (showSequence == 2) {
            sequenceDiscount = BigDecimal.valueOf(2); // $2 discount for 2nd show
        }
        return sequenceDiscount;
    }

}
