package pl.north93.booksy.calendar.employee.event;

import java.util.List;

import pl.north93.booksy.calendar.employee.dto.EmployeeDto;

public record EmployeesDownloadedEvent(List<EmployeeDto> employees)
{
}
