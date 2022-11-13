package com.jpmc.theater.service;

import com.jpmc.theater.domain.Customer;
import com.jpmc.theater.domain.Movie;
import com.jpmc.theater.domain.Reservation;
import com.jpmc.theater.domain.Showing;
import com.jpmc.theater.utils.PricingUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TicketPricingServiceTest {

    TicketPricingService underTest = new TicketPricingService();

    @Test
    void Given_SpecialMovieWith5AudCount_When_CalculateTotalFee_Then_20PercentDiscountApplies() {
        int audienceCount = 5;
        BigDecimal expectedValue = PricingUtil.getBigDecimal(10).multiply(BigDecimal.valueOf(audienceCount));
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), BigDecimal.valueOf(12.5), 1);
        Showing showing = new Showing(spiderMan, 5, LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 50)));
        Reservation reservation = new Reservation(new Customer("testing"), showing, audienceCount);
        assertEquals(expectedValue, underTest.calculateTotalFee(reservation));
    }

    @Test
    void Given_ShowTimeDiscountWith5AudCount_When_CalculateTotalFee_Then_25PercentDiscountApplies() {
        int audienceCount = 5;
        BigDecimal expectedValue = PricingUtil.getBigDecimal(12.5).multiply(BigDecimal.valueOf(0.75)).setScale(2, RoundingMode.UP).multiply(BigDecimal.valueOf(audienceCount));
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), BigDecimal.valueOf(12.5), 1);
        Showing showing = new Showing(spiderMan, 5, LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 50)));
        Reservation reservation = new Reservation(new Customer("testing"), showing, audienceCount);
        assertEquals(expectedValue, underTest.calculateTotalFee(reservation));
    }

    @Test
    void Given_SequenceDiscountWith5AudCount_When_CalculateTotalFee_Then_2DollarApplies() {
        int audienceCount = 5;
        BigDecimal expectedValue = PricingUtil.getBigDecimal(11).subtract(BigDecimal.valueOf(2)).setScale(2, RoundingMode.UP).multiply(BigDecimal.valueOf(audienceCount));
        Movie turningRed = new Movie("Turning Red", Duration.ofMinutes(85), BigDecimal.valueOf(11), 0);
        Showing showing = new Showing(turningRed, 2, LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 50)));
        Reservation reservation = new Reservation(new Customer("testing"), showing, audienceCount);
        assertEquals(expectedValue, underTest.calculateTotalFee(reservation));
    }

    @Test
    void Given_SpecialMovie_When_GetTicketPriceWithDiscount_Then_20PercentDiscountApplies() {
        BigDecimal expectedValue = PricingUtil.getBigDecimal(10);
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), BigDecimal.valueOf(12.5), 1);
        Showing showing = new Showing(spiderMan, 5, LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 50)));
        assertEquals(expectedValue, underTest.getTicketPriceWithDiscount(showing));
    }

    @Test
    void Given_ShowTimeDiscount_When_GetTicketPriceWithDiscount_Then_25PercentDiscountApplies() {
        BigDecimal expectedValue = PricingUtil.getBigDecimal(12.5).multiply(BigDecimal.valueOf(0.75)).setScale(2, RoundingMode.UP);
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), BigDecimal.valueOf(12.5), 1);
        Showing showing = new Showing(spiderMan, 5, LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 50)));
        assertEquals(expectedValue, underTest.getTicketPriceWithDiscount(showing));
    }

    @Test
    void Given_SequenceDiscount_When_GetTicketPriceWithDiscount_Then_2DollarDiscountApplies() {
        BigDecimal expectedValue = PricingUtil.getBigDecimal(11).subtract(BigDecimal.valueOf(2)).setScale(2, RoundingMode.UP);
        Movie turningRed = new Movie("Turning Red", Duration.ofMinutes(85), BigDecimal.valueOf(11), 0);
        Showing showing = new Showing(turningRed, 2, LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 50)));
        assertEquals(expectedValue, underTest.getTicketPriceWithDiscount(showing));
    }

    @Test
    void Given_SequenceDiscount_When_GetTicketPriceWithDiscount_Then_1DollarDiscountApplies() {
        BigDecimal expectedValue = PricingUtil.getBigDecimal(11).subtract(BigDecimal.valueOf(1)).setScale(2, RoundingMode.UP);
        Movie turningRed = new Movie("Turning Red", Duration.ofMinutes(85), BigDecimal.valueOf(11), 0);
        Showing showing = new Showing(turningRed, 7, LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 50)));
        assertEquals(expectedValue, underTest.getTicketPriceWithDiscount(showing));
    }

    @Test
    void Given_AllThreeDiscount_When_GetTicketPriceWithDiscount_Then_HighestDiscount25PercentApplies() {
        BigDecimal expectedValue = PricingUtil.getBigDecimal(12.5).multiply(BigDecimal.valueOf(0.75)).setScale(2, RoundingMode.UP);
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), BigDecimal.valueOf(12.5), 1);
        Showing showing = new Showing(spiderMan, 1, LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 50)));
        assertEquals(expectedValue, underTest.getTicketPriceWithDiscount(showing));
    }

    @Test
    void Given_AllThreeDiscount_When_GetTicketPriceWithDiscount_Then_HighestDiscount3DollarApplies() {
        BigDecimal expectedValue = PricingUtil.getBigDecimal(9).subtract(BigDecimal.valueOf(3)).setScale(2, RoundingMode.UP);
        Movie theBatMan = new Movie("The Batman", Duration.ofMinutes(95), BigDecimal.valueOf(9), 1);
        Showing showing = new Showing(theBatMan, 1, LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 50)));
        assertEquals(expectedValue, underTest.getTicketPriceWithDiscount(showing));
    }

    @Test
    void Given_NonSpecialMovie_When_GetTicketPriceWithDiscount_Then_NoDiscountApplies() {
        BigDecimal expectedValue = PricingUtil.getBigDecimal(12.5);
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), BigDecimal.valueOf(12.5), 0);
        Showing showing = new Showing(spiderMan, 5, LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 50)));
        assertEquals(expectedValue, underTest.getTicketPriceWithDiscount(showing));
    }

    @Test
    void Given_AllThreeDiscount_When_GetDiscount_Then_HighestDiscount3DollarApplies() {
        BigDecimal expectedValue = BigDecimal.valueOf(3);
        Movie theBatMan = new Movie("The Batman", Duration.ofMinutes(95), BigDecimal.valueOf(9), 1);
        Showing showing = new Showing(theBatMan, 1, LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 50)));
        assertEquals(expectedValue, underTest.getDiscount(showing));
    }

    @Test
    void Given_BetweenDiscountTimeRangeShowing_GetMaxPercentageDiscount_Then_25PercentDiscountApplies() {
        BigDecimal expectedValue = PricingUtil.getBigDecimal(2.5);
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), BigDecimal.valueOf(10), 1);
        Showing showing = new Showing(spiderMan, 1, LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 50)));
        assertEquals(expectedValue, underTest.getMaxPercentageDiscount(showing));
    }

    @Test
    void Given_SpecialMovie_GetMaxPercentageDiscount_Then_20PercentDiscountApplies() {
        BigDecimal expectedValue = PricingUtil.getBigDecimal(2);
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), BigDecimal.valueOf(10), 1);
        Showing showing = new Showing(spiderMan, 1, LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 50)));
        assertEquals(expectedValue, underTest.getMaxPercentageDiscount(showing));
    }

    @Test
    void Given_NoDiscount_GetMaxPercentageDiscount_Then_NoDiscountApplies() {
        BigDecimal expectedValue = BigDecimal.valueOf(0);
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), BigDecimal.valueOf(10), 0);
        Showing showing = new Showing(spiderMan, 1, LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 50)));
        assertEquals(expectedValue, underTest.getMaxPercentageDiscount(showing));
    }

    @Test
    void Given_BetweenDiscountTimeRangeShowing_GetShowingTimeDiscount_Then_25PercentDiscountApplies() {
        BigDecimal expectedValue = PricingUtil.getBigDecimal(2.5);
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), BigDecimal.valueOf(10), 1);
        Showing showing = new Showing(spiderMan, 1, LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 50)));
        assertEquals(expectedValue, underTest.getShowingTimeDiscount(showing));
    }

    @Test
    void Given_NotBetweenDiscountTimeRangeShowing_GetShowingTimeDiscount_Then_NoDiscountApplies() {
        BigDecimal expectedValue = BigDecimal.valueOf(0);
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), BigDecimal.valueOf(10), 0);
        Showing showing = new Showing(spiderMan, 1, LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 50)));
        assertEquals(expectedValue, underTest.getShowingTimeDiscount(showing));
    }

    @Test
    void Given_SpecialMovie_when_GetSpecialDiscount_Then_20PercentDiscountApplies() {
        BigDecimal expectValue = PricingUtil.getBigDecimal(2);
        assertEquals(expectValue, underTest.getSpecialDiscount(1, BigDecimal.valueOf(10)));
    }

    @Test
    void Given_NonSpecialMovie_when_GetSpecialDiscount_Then_NoDiscountApplies() {
        BigDecimal expectValue = BigDecimal.valueOf(0);
        assertEquals(expectValue, underTest.getSpecialDiscount(0, BigDecimal.valueOf(10)));
    }

    @Test
    void Given_FirstShowing_when_GetSequenceDiscount_Then_Return3() {
        BigDecimal expectValue = BigDecimal.valueOf(3);
        assertEquals(expectValue, underTest.getSequenceDiscount(1));
    }

    @Test
    void Given_SecondShowing_when_GetSequenceDiscount_Then_Return2() {
        BigDecimal expectValue = BigDecimal.valueOf(2);
        assertEquals(expectValue, underTest.getSequenceDiscount(2));
    }

    @Test
    void Given_SeventhShowing_when_GetSequenceDiscount_Then_Return1() {
        BigDecimal expectValue = BigDecimal.valueOf(1);
        assertEquals(expectValue, underTest.getSequenceDiscount(7));
    }

    @Test
    void Given_NonDiscountedShowing_when_GetSequenceDiscount_Then_Return0() {
        BigDecimal expectValue = BigDecimal.ZERO;
        assertEquals(expectValue, underTest.getSequenceDiscount(5));
    }
}