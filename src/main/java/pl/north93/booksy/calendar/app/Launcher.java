package pl.north93.booksy.calendar.app;

import javafx.application.Application;

/**
 * A hack to force launch JavaFX from non-modular fatjar
 */
public final class Launcher
{
    public static void main(final String[] args)
    {
        Application.launch(BooksyCalendar.class, args);
    }
}
