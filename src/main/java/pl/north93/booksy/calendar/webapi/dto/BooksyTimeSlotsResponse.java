package pl.north93.booksy.calendar.webapi.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BooksyTimeSlotsResponse(@JsonProperty("time_slots")
                                      List<BooksyTimeSlotsDay> allTimeSlots,
                                      @JsonProperty("resources")
                                      List<BooksyEmployee> employees)
{
}
