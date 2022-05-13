package pl.north93.booksy.calendar.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record DayDto(LocalDate localDate, List<LocalTime> times)
{
}
