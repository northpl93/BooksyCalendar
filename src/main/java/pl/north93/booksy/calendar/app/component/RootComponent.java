package pl.north93.booksy.calendar.app.component;

import com.google.common.eventbus.EventBus;

import javafx.scene.layout.VBox;
import pl.north93.booksy.calendar.calendar.PickersController;
import pl.north93.booksy.calendar.calendar.component.CalendarComponent;
import pl.north93.booksy.calendar.calendar.component.PickersComponent;

public class RootComponent extends VBox
{
    public RootComponent(final EventBus eventBus, final PickersController pickersController)
    {
        final PickersComponent pickersComponent = new PickersComponent(pickersController);
        eventBus.register(pickersComponent);
        this.getChildren().add(pickersComponent);

        final CalendarComponent calendarComponent = new CalendarComponent(6);
        eventBus.register(calendarComponent);
        this.getChildren().add(calendarComponent);
    }
}
