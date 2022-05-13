package pl.north93.booksy.calendar.dto;

import java.util.List;

public record EmployeeDto(String name, List<DayDto> dayDtos)
{
    @Override
    public String toString()
    {
        return this.name;
    }
}
