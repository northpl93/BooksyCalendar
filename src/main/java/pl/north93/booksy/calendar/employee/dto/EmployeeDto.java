package pl.north93.booksy.calendar.employee.dto;

import java.util.List;

import pl.north93.booksy.calendar.service.dto.DayDto;

public record EmployeeDto(String name, List<DayDto> dayDtos)
{
    @Override
    public String toString()
    {
        return this.name;
    }
}
