package pl.north93.booksy.calendar.place;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.EventBus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.booksy.calendar.app.ApplicationConfig;
import pl.north93.booksy.calendar.place.event.PlacesUpdatedEvent;
import pl.north93.booksy.calendar.webapi.dto.BooksyBusiness;
import pl.north93.booksy.calendar.webapi.service.BooksyClient;

public class PlacesService
{
    private static final Logger log = LoggerFactory.getLogger(PlacesService.class);
    private final EventBus eventBus;
    private final BooksyClient booksyClient;
    private final ExecutorService executorService;
    private final List<PlaceDto> placeDtos = new ArrayList<>();

    public PlacesService(final EventBus eventBus, final BooksyClient booksyClient, final ExecutorService executorService)
    {
        this.eventBus = eventBus;
        this.booksyClient = booksyClient;
        this.executorService = executorService;
    }

    public void loadFromDisk(final File file)
    {
        if (! file.exists())
        {
            log.info("Places file {} doesn't exist", file.getAbsolutePath());
            return;
        }

        final ObjectMapper objectMapper = new ObjectMapper();
        try
        {
            final ApplicationConfig config = objectMapper.reader().readValue(file, ApplicationConfig.class);
            this.placeDtos.addAll(config.places());

            log.info("Loaded places from disk {}", file.getAbsolutePath());
            this.postPlacesUpdatedEvent();
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void saveToDisk(final File file)
    {
        final ObjectMapper objectMapper = new ObjectMapper();
        try
        {
            objectMapper.writer().writeValue(file, new ApplicationConfig(this.placeDtos));
            log.info("Saved places file to disk in {}", file.getAbsolutePath());
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void addPlace(final String placeId)
    {
        log.info("Requested add place for id {}", placeId);
        this.executorService.submit(() -> this.downloadPlaceDataAndAdd(placeId));
    }

    private void downloadPlaceDataAndAdd(final String placeId)
    {
        final BooksyBusiness business = this.booksyClient.getBusiness(placeId);
        log.info("Downloaded data for place {}", placeId);

        this.placeDtos.add(new PlaceDto(placeId, business.name()));
        this.postPlacesUpdatedEvent();
    }

    private void postPlacesUpdatedEvent()
    {
        this.eventBus.post(new PlacesUpdatedEvent(this.placeDtos));
    }
}
