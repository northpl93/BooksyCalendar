package pl.north93.booksy.calendar.webapi.dto;

import java.time.LocalTime;

public record BooksyTimeSlot(LocalTime from, LocalTime to, Double interval)
{
}
