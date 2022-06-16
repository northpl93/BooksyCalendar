package pl.north93.booksy.calendar.calendar;

import static java.util.concurrent.CompletableFuture.supplyAsync;


import java.util.concurrent.ExecutorService;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.booksy.calendar.employee.dto.EmployeeDto;
import pl.north93.booksy.calendar.employee.event.EmployeeSelectedEvent;
import pl.north93.booksy.calendar.employee.event.EmployeesDownloadedEvent;
import pl.north93.booksy.calendar.place.event.RequestAddPlaceDialogEvent;
import pl.north93.booksy.calendar.service.ServiceVariantsSupplier;
import pl.north93.booksy.calendar.service.ServicesVariantsDownloadedEvent;
import pl.north93.booksy.calendar.service.dto.ServiceVariantDto;
import pl.north93.booksy.calendar.webapi.service.BooksyClient;

public class PickersController
{
    private static final Logger log = LoggerFactory.getLogger(PickersController.class);
    private final EventBus eventBus;
    private final BooksyClient booksyClient;
    private final ExecutorService executorService;

    public PickersController(final EventBus eventBus, final BooksyClient booksyClient, final ExecutorService executorService)
    {
        this.eventBus = eventBus;
        this.booksyClient = booksyClient;
        this.executorService = executorService;
    }

    public void requestAddPlaceDialog()
    {
        log.info("Requesting add place dialog");
        this.eventBus.post(new RequestAddPlaceDialogEvent());
    }

    public void requestServiceVariantDownload(final String placeId)
    {
        Preconditions.checkNotNull(placeId, "Place ID can't be null");
        log.info("Requested service variants download for place {}", placeId);
        supplyAsync(new ServiceVariantsSupplier(this.booksyClient, placeId), this.executorService)
                .whenComplete((variantDtos, throwable) -> this.eventBus.post(new ServicesVariantsDownloadedEvent(variantDtos)));
    }

    public void requestCalendarDownload(final ServiceVariantDto serviceVariantDto)
    {
        Preconditions.checkNotNull(serviceVariantDto, "Service variant can't be null");
        log.info("Requested calendar download for serviceVariant {}", serviceVariantDto.serviceVariantId());
        supplyAsync(new CalendarSupplier(this.booksyClient, serviceVariantDto), this.executorService)
                .whenComplete((dayDtos, throwable) -> this.eventBus.post(new EmployeesDownloadedEvent(dayDtos)));
    }

    public void requestEmployeeSelect(final EmployeeDto selectedEmployee)
    {
        Preconditions.checkNotNull(selectedEmployee, "Selected employee can't be null");
        log.info("Requested employee select for {}", selectedEmployee.name());
        this.eventBus.post(new EmployeeSelectedEvent(selectedEmployee.dayDtos()));
    }
}
