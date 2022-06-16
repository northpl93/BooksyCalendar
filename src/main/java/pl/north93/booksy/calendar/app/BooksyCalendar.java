package pl.north93.booksy.calendar.app;

import static pl.north93.booksy.calendar.webapi.service.BooksyFactory.createBooksyClient;


import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import org.apache.http.impl.client.HttpClients;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.north93.booksy.calendar.app.component.RootComponent;
import pl.north93.booksy.calendar.calendar.PickersController;
import pl.north93.booksy.calendar.place.AddPlaceDialogController;
import pl.north93.booksy.calendar.place.PlacesService;
import pl.north93.booksy.calendar.webapi.service.BooksyClient;

public class BooksyCalendar extends Application
{
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void start(final Stage primaryStage)
    {
        final File configFile = new File("booksy_calendar_config.json");
        final EventBus eventBus = new AsyncEventBus(new UiThreadExecutor());

        final BooksyClient booksyClient = createBooksyClient(HttpClients.createDefault());

        final PlacesService placesService = new PlacesService(eventBus, booksyClient, this.executorService);
        eventBus.register(new AddPlaceDialogController(placesService));

        final Scene scene = new Scene(this.buildRootLayout(eventBus, booksyClient));
        scene.getStylesheets().add(this.getClass().getResource("/style.css").toExternalForm());
        primaryStage.setOnCloseRequest(event -> placesService.saveToDisk(configFile));
        primaryStage.setTitle("Booksy.com calendar");
        primaryStage.setScene(scene);
        primaryStage.show();

        placesService.loadFromDisk(configFile);
    }

    private Parent buildRootLayout(final EventBus eventBus, final BooksyClient booksyClient)
    {
        return new RootComponent(eventBus, new PickersController(eventBus, booksyClient, this.executorService));
    }

    @Override
    public void stop()
    {
        this.executorService.shutdown();
    }
}
