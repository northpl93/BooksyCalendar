package pl.north93.booksy.calendar.controller.action;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import pl.north93.booksy.calendar.dto.DayDto;
import pl.north93.booksy.calendar.dto.EmployeeDto;
import pl.north93.booksy.calendar.dto.ServiceVariantDto;
import pl.north93.booksy.calendar.webapi.dto.BooksyEmployee;
import pl.north93.booksy.calendar.webapi.dto.BooksyTimeSlotsDay;
import pl.north93.booksy.calendar.webapi.dto.BooksyTimeSlotsResponse;
import pl.north93.booksy.calendar.webapi.service.BooksyClient;

public class DownloadCalendar implements Supplier<List<EmployeeDto>>
{
    private final BooksyClient booksyClient;
    private final ServiceVariantDto serviceVariant;

    public DownloadCalendar(final BooksyClient booksyClient, final ServiceVariantDto serviceVariant)
    {
        this.booksyClient = booksyClient;
        this.serviceVariant = serviceVariant;
    }

    @Override
    public List<EmployeeDto> get()
    {
        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = startDate.plus(Period.ofWeeks(6));

        final BooksyTimeSlotsResponse timeSlots = this.booksyClient.getTimeSlots(this.serviceVariant.serviceVariantId(), startDate, endDate);

        final List<BooksyEmployee> booksyEmployees = new ArrayList<>(timeSlots.employees());
        if (booksyEmployees.size() > 1)
        {
            booksyEmployees.add(0, new BooksyEmployee("Wszyscy", timeSlots.allTimeSlots()));
        }

        return booksyEmployees.stream().map(this::booksyEmployeeToEmployee).collect(Collectors.toList());
    }

    private EmployeeDto booksyEmployeeToEmployee(final BooksyEmployee booksyEmployee)
    {
        return new EmployeeDto(booksyEmployee.name(), this.getDayDtosForEmployee(booksyEmployee));
    }

    private List<DayDto> getDayDtosForEmployee(final BooksyEmployee employee)
    {
        final List<DayDto> dayDtos = new ArrayList<>();

        LocalDate dayToRender = LocalDate.now().with(DayOfWeek.MONDAY);
        for (int week = 0; week < 6; week++)
        {
            for (int dayOfWeek = 0; dayOfWeek < 7; dayOfWeek++)
            {
                dayDtos.add(this.prepareDayData(employee, dayToRender));
                dayToRender = dayToRender.plusDays(1);
            }
        }

        return dayDtos;
    }

    private DayDto prepareDayData(final BooksyEmployee booksyEmployee, final LocalDate dayToPrepare)
    {
        final List<LocalTime> serviceTimes = this.findSlotsForDay(booksyEmployee, dayToPrepare)
                                                 .map(this::convertTimeSlotsToLocalTime)
                                                 .orElse(List.of());
        return new DayDto(dayToPrepare, serviceTimes);
    }

    private List<LocalTime> convertTimeSlotsToLocalTime(final BooksyTimeSlotsDay booksyTimeSlotsDay)
    {
        return booksyTimeSlotsDay.timeSlots().stream().map(slotTime -> LocalTime.parse(slotTime.from())).collect(Collectors.toList());
    }

    public Optional<BooksyTimeSlotsDay> findSlotsForDay(final BooksyEmployee booksyEmployee, final LocalDate date)
    {
        for (final BooksyTimeSlotsDay timeSlot : booksyEmployee.timeSlots())
        {
            if (timeSlot.date().equals(date))
            {
                return Optional.of(timeSlot);
            }
        }

        return Optional.empty();
    }
}
