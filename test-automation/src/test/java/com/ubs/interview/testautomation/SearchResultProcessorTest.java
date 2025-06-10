package com.ubs.interview.testautomation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SearchResultProcessorTest {

    Map<LocalDate, Map<String, String>> searchResult;
    SearchResultProcessor searchResultProcessor = new SearchResultProcessor();

    @BeforeEach
    public void setUp(){
        searchResult = Map.of(
                LocalDate.of(2025, 6, 1),
                Map.of("user1", "free",
                        "user2", "unavailable"),
                LocalDate.of(2025, 6, 2),
                Map.of("user1", "free",
                        "user2", "free",
                        "user3", "free"),
                LocalDate.of(2025, 6, 4),
                Map.of("user1", "unavailable",
                        "user2", "unavailable",
                        "user3", "unavailable")
        );
    }

    @Test
    public void isUserFreeOnDate_PositiveTest() {
        assertTrue(
                searchResultProcessor.isUserFreeOnDate(searchResult, LocalDate.of(2025, 6, 1), "user1"));
        assertFalse(
                searchResultProcessor.isUserFreeOnDate(searchResult, LocalDate.of(2025, 6, 2), "user1"));
        assertTrue(
                searchResultProcessor.isUserFreeOnDate(searchResult, LocalDate.of(2025, 6, 2), "user3"));
    }

    @Test
    public void isUserFreeOnDate_NegativeTest() {
        assertFalse(
                searchResultProcessor.isUserFreeOnDate(searchResult, LocalDate.of(2025, 6, 3), "user1"),
                "All search results should be equal");
        assertFalse(
                searchResultProcessor.isUserFreeOnDate(searchResult, LocalDate.of(2025, 6, 1), "user2"),
                "All search results should be equal");
        assertFalse(
                searchResultProcessor.isUserFreeOnDate(searchResult, LocalDate.of(2025, 6, 1), "user3"),
                "All search results should be equal");
    }

    @Test
    public void findDateWithMostAvailability_Test() {
        LocalDate expectedDate = LocalDate.of(2025, 6, 2);
        LocalDate actualDate = searchResultProcessor.findDateWithMostAvailability(searchResult);

        assertEquals(expectedDate, actualDate,
                "Should return the date with the most users having 'free' status");
    }
}
