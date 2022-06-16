package pl.north93.booksy.calendar.employee.event;

import java.util.List;

import pl.north93.booksy.calendar.service.dto.DayDto;

public record EmployeeSelectedEvent(List<DayDto> dayDtos)
{
}
