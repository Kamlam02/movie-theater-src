package com.jpmc.theater.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jpmc.theater.domain.Movie;
import com.jpmc.theater.domain.Showing;
import com.jpmc.theater.service.TicketPricingService;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor
public class MovieScheduleHandler {
    private ObjectMapper objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

    protected MovieScheduleHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public static List<Showing> initShowings(LocalDateProvider localDateProvider) {
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), BigDecimal.valueOf(12.5), 1);
        Movie turningRed = new Movie("Turning Red", Duration.ofMinutes(85), BigDecimal.valueOf(11), 0);
        Movie theBatMan = new Movie("The Batman", Duration.ofMinutes(95), BigDecimal.valueOf(9), 0);
        return List.of(
                new Showing(turningRed, 1, LocalDateTime.of(localDateProvider.currentDate(), LocalTime.of(9, 0))),
                new Showing(spiderMan, 2, LocalDateTime.of(localDateProvider.currentDate(), LocalTime.of(11, 0))),
                new Showing(theBatMan, 3, LocalDateTime.of(localDateProvider.currentDate(), LocalTime.of(12, 50))),
                new Showing(turningRed, 4, LocalDateTime.of(localDateProvider.currentDate(), LocalTime.of(14, 30))),
                new Showing(spiderMan, 5, LocalDateTime.of(localDateProvider.currentDate(), LocalTime.of(16, 10))),
                new Showing(theBatMan, 6, LocalDateTime.of(localDateProvider.currentDate(), LocalTime.of(17, 50))),
                new Showing(turningRed, 7, LocalDateTime.of(localDateProvider.currentDate(), LocalTime.of(19, 30))),
                new Showing(spiderMan, 8, LocalDateTime.of(localDateProvider.currentDate(), LocalTime.of(21, 10))),
                new Showing(theBatMan, 9, LocalDateTime.of(localDateProvider.currentDate(), LocalTime.of(23, 0)))
        );
    }

    public void printShowingsJson(List<Showing> showings) {
        try {
            String jsonOutput = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(showings);
            System.out.println(jsonOutput);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printShowingsText(List<Showing> showings) {
        String bar = "===================================================";
        System.out.println(LocalDateProvider.getInstance().currentDate());
        System.out.println(bar);
        showings.forEach(s ->
                System.out.println(getShowingReadableString(s))
        );
        System.out.println(bar);
    }

    public static String getShowingReadableString(Showing showing) {
        return showing.getSequenceOfTheDay() + ": "
                + showing.getShowStartTime() + " "
                + showing.getMovie().getTitle() + " "
                + humanReadableFormat(showing.getMovie().getRunningTime())
                + " $"
                + showing.getMovie().getTicketPrice()
                + showDiscountedPrice(showing);
//
    }

    private static String showDiscountedPrice(Showing showing) {
        BigDecimal discountedTicketPrice = new TicketPricingService().getTicketPriceWithDiscount(showing);
        return showing.getMovie().getTicketPrice().equals(discountedTicketPrice) ? "" : " discounted price $" + discountedTicketPrice;
    }

    protected static String humanReadableFormat(Duration duration) {
        long hour = duration.toHours();
        long remainingMin = duration.toMinutes() - TimeUnit.HOURS.toMinutes(duration.toHours());
        return String.format("(%s hour%s %s minute%s)", hour, handlePlural(hour), remainingMin, handlePlural(remainingMin));
    }

    protected static String handlePlural(long value) {
        return value == 1 ? "" : "s";
    }
}
