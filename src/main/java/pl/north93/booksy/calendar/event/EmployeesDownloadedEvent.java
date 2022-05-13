package pl.north93.booksy.calendar.event;

import java.util.List;

import pl.north93.booksy.calendar.dto.EmployeeDto;

public record EmployeesDownloadedEvent(List<EmployeeDto> employees)
{
}
