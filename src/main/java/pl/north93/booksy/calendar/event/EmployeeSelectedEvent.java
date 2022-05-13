package pl.north93.booksy.calendar.event;

import java.util.List;

import pl.north93.booksy.calendar.dto.DayDto;

public record EmployeeSelectedEvent(List<DayDto> dayDtos)
{
}
