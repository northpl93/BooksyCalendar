package pl.north93.booksy.calendar;

import static pl.north93.booksy.calendar.webapi.service.BooksyFactory.createBooksyClient;


import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.eventbus.EventBus;

import org.apache.http.impl.client.HttpClients;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.north93.booksy.calendar.components.CalendarComponent;
import pl.north93.booksy.calendar.components.PickersComponent;
import pl.north93.booksy.calendar.controller.AddPlaceDialogController;
import pl.north93.booksy.calendar.controller.PickersController;
import pl.north93.booksy.calendar.webapi.service.BooksyClient;

public class Launcher extends Application
{
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // app size H600, W1500
    @Override
    public void start(final Stage primaryStage)
    {
        final File configFile = new File("booksy_calendar_config.json");
        final EventBus eventBus = new EventBus();

        final BooksyClient booksyClient = createBooksyClient(HttpClients.createDefault());

        final PlacesService placesService = new PlacesService(eventBus, booksyClient, this.executorService);
        eventBus.register(new AddPlaceDialogController(placesService));
        placesService.loadFromDisk(configFile);

        final Scene scene = new Scene(this.buildRootLayout(eventBus, booksyClient));
        scene.getStylesheets().add(this.getClass().getResource("/style.css").toExternalForm());
        primaryStage.setOnCloseRequest(event -> placesService.saveToDisk(configFile));
        primaryStage.setTitle("Booksy.com calendar");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Parent buildRootLayout(final EventBus eventBus, final BooksyClient booksyClient)
    {
        final VBox rootLayout = new VBox();

        final ObservableList<Node> children = rootLayout.getChildren();

        final PickersComponent pickersComponent = new PickersComponent(new PickersController(eventBus, booksyClient, this.executorService));
        eventBus.register(pickersComponent);
        children.add(pickersComponent);

        final CalendarComponent calendarComponent = new CalendarComponent(6);
        eventBus.register(calendarComponent);
        children.add(calendarComponent);

        return rootLayout;
    }

    @Override
    public void stop()
    {
        this.executorService.shutdown();
    }
}
