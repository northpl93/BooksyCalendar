package pl.north93.booksy.calendar.app;

import java.util.concurrent.Executor;

import javafx.application.Platform;

public class UiThreadExecutor implements Executor
{
    @Override
    public void execute(final Runnable command)
    {
        Platform.runLater(command);
    }
}
