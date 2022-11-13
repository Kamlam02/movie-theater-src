package com.jpmc.theater.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jpmc.theater.domain.Movie;
import com.jpmc.theater.domain.Showing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class MovieScheduleHandlerTest {

    @Test
    void initShowings() {
    }

    @Test
    void Given_JsonProcessingException_When_PrintShowingsJson_Then_ThrowRuntimeException() {
        try {
            ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
            ObjectWriter objectWriter = Mockito.mock(ObjectWriter.class);
            MovieScheduleHandler movieScheduleHandler = new MovieScheduleHandler(objectMapper);
            when(objectMapper.writerWithDefaultPrettyPrinter()).thenReturn(objectWriter);
            doThrow(JsonProcessingException.class).when(objectWriter).writeValueAsString(Mockito.any());
            assertThrows(RuntimeException.class, () -> movieScheduleHandler.printShowingsJson(Collections.emptyList()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void Given_ListOfShowing_When_PrintShowingsJson_Then_PrintShowingInJsonFormat() {
        String expectedValue = "\"movie\" : {\n" +
                "    \"title\" : \"Turning Red\",\n" +
                "    \"runningTime\" : 5100.000000000,\n" +
                "    \"ticketPrice\" : 11.00,\n" +
                "    \"specialCode\" : 0\n";
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), BigDecimal.valueOf(10), 0);
        Movie turningRed = new Movie("Turning Red", Duration.ofMinutes(85), BigDecimal.valueOf(11), 0);
        List<Showing> showings = List.of(
                new Showing(turningRed, 1, LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 50))),
                new Showing(spiderMan, 2, LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 50))));
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        new MovieScheduleHandler().printShowingsJson(showings);
        assertTrue(outContent.toString().contains(expectedValue));
    }

    @Test
    void Given_ListOfShowing_When_printShowingsText_Then_PrintShowingInStringFormat() {
        String expectedValue = "3: 2022-11-13T10:50 Turning Red (1 hour 25 minutes) $11.00\n" +
                "5: 2022-11-13T17:50 Spider-Man: No Way Home (1 hour 30 minutes) $10.00";
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), BigDecimal.valueOf(10), 0);
        Movie turningRed = new Movie("Turning Red", Duration.ofMinutes(85), BigDecimal.valueOf(11), 0);
        List<Showing> showings = List.of(
                new Showing(turningRed, 3, LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 50))),
                new Showing(spiderMan, 5, LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 50))));
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        MovieScheduleHandler.printShowingsText(showings);
        assertTrue(outContent.toString().contains(expectedValue));
    }

    @Test
    void Given_Showing_When_GetShowingReadableString_Then_ReturnShowingInStringFormat() {
        String expectedValue = "3: 2022-11-13T17:50 Spider-Man: No Way Home (1 hour 30 minutes) $10.00";
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), BigDecimal.valueOf(10), 0);
        Showing showing = new Showing(spiderMan, 3, LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 50)));
        Assertions.assertEquals(expectedValue, MovieScheduleHandler.getShowingReadableString(showing));
    }

    @Test
    void Given_Duration_When_humanReadableFormat_Then_ReturnString() {
        String expectedValue = "(1 hour 30 minutes)";
        assertEquals(expectedValue, MovieScheduleHandler.humanReadableFormat(Duration.ofMinutes(90)));
    }

    @Test
    void Given_LongNot1_When_handlePlural_Then_ReturnS() {
        String expectedValue = "s";
        assertEquals(expectedValue, MovieScheduleHandler.handlePlural(2));
    }

    @Test
    void Given_Long1_When_handlePlural_Then_ReturnEmpty() {
        String expectedValue = "";
        assertEquals(expectedValue, MovieScheduleHandler.handlePlural(1));
    }
}