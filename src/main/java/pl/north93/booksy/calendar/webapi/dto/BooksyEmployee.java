package pl.north93.booksy.calendar.webapi.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BooksyEmployee(String name,
                             @JsonProperty("resource_time_slots")
                             List<BooksyTimeSlotsDay> timeSlots)
{
}
