package pl.north93.booksy.calendar.webapi.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BooksyTimeSlotsDay(LocalDate date,
                                 @JsonProperty("time_slots")
                                 List<BooksyTimeSlot> timeSlots)
{
}
