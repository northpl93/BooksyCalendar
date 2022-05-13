package pl.north93.booksy.calendar.controller;

import static java.util.concurrent.CompletableFuture.supplyAsync;


import java.util.concurrent.ExecutorService;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import pl.north93.booksy.calendar.controller.action.DownloadCalendar;
import pl.north93.booksy.calendar.controller.action.DownloadServiceVariants;
import pl.north93.booksy.calendar.dto.EmployeeDto;
import pl.north93.booksy.calendar.dto.ServiceVariantDto;
import pl.north93.booksy.calendar.event.EmployeeSelectedEvent;
import pl.north93.booksy.calendar.event.EmployeesDownloadedEvent;
import pl.north93.booksy.calendar.event.RequestAddPlaceDialogEvent;
import pl.north93.booksy.calendar.event.ServicesVariantsDownloadedEvent;
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
        this.postEventOnUiThread(new RequestAddPlaceDialogEvent());
    }

    public void requestServiceVariantDownload(final String placeId)
    {
        Preconditions.checkNotNull(placeId, "Place ID can't be null");
        log.info("Requested service variants download for place {}", placeId);
        supplyAsync(new DownloadServiceVariants(this.booksyClient, placeId), this.executorService)
                .whenComplete((variantDtos, throwable) -> this.postEventOnUiThread(new ServicesVariantsDownloadedEvent(variantDtos)));
    }

    public void requestCalendarDownload(final ServiceVariantDto serviceVariantDto)
    {
        Preconditions.checkNotNull(serviceVariantDto, "Service variant can't be null");
        log.info("Requested calendar download for serviceVariant {}", serviceVariantDto.serviceVariantId());
        supplyAsync(new DownloadCalendar(this.booksyClient, serviceVariantDto), this.executorService)
                .whenComplete((dayDtos, throwable) -> this.postEventOnUiThread(new EmployeesDownloadedEvent(dayDtos)));
    }

    public void requestEmployeeSelect(final EmployeeDto selectedEmployee)
    {
        Preconditions.checkNotNull(selectedEmployee, "Selected employee can't be null");
        log.info("Requested employee select for {}", selectedEmployee.name());
        this.postEventOnUiThread(new EmployeeSelectedEvent(selectedEmployee.dayDtos()));
    }

    private void postEventOnUiThread(final Object event)
    {
        Platform.runLater(() -> this.eventBus.post(event));
    }
}
