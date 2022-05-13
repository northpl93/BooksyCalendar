package pl.north93.booksy.calendar.components;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import com.google.common.eventbus.Subscribe;

import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import pl.north93.booksy.calendar.dto.DayDto;
import pl.north93.booksy.calendar.event.EmployeeSelectedEvent;

public class CalendarComponent extends GridPane
{
    private static final int FIRST_ROW = 0;
    private static final int DAYS_IN_WEEK = 7;
    private final int weeksToRender;

    public CalendarComponent(final int weeksToRender)
    {
        this.weeksToRender = weeksToRender;
        this.setId("calendar");

        this.getRowConstraints().add(this.createRowConstraints(dayOfWeekNameRow -> dayOfWeekNameRow.setMaxHeight(20)));

        final RowConstraints rowConstraint = this.createRowConstraints(rowConstraints -> rowConstraints.setPercentHeight(100d / weeksToRender));
        for (int i = 0; i < weeksToRender; i++)
        {
            this.getRowConstraints().add(rowConstraint);
        }

        final ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100d / DAYS_IN_WEEK);

        for (int i = 0; i < DAYS_IN_WEEK; i++)
        {
            this.getColumnConstraints().add(columnConstraints);
        }
    }

    private RowConstraints createRowConstraints(final Consumer<RowConstraints> configurer)
    {
        final RowConstraints rowConstraints = new RowConstraints();
        configurer.accept(rowConstraints);
        return rowConstraints;
    }

    @Subscribe
    private void handleEmployeeSelected(final EmployeeSelectedEvent event)
    {
        this.getChildren().clear();

        this.renderDayNames();
        this.renderCalendarContent(event.dayDtos());
    }

    private void renderDayNames()
    {
        for (int dayColumnIndex = 0; dayColumnIndex < DAYS_IN_WEEK; dayColumnIndex++)
        {
            final DayOfWeek dayOfWeek = DayOfWeek.values()[dayColumnIndex];
            this.add(new Label(this.getDayName(dayOfWeek)), dayColumnIndex, FIRST_ROW);
        }
    }

    private String getDayName(final DayOfWeek dayOfWeek)
    {
        return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pl-PL"));
    }

    private void renderCalendarContent(final List<DayDto> dayDtos)
    {
        final Iterator<? extends DayDto> dayDtoIterator = dayDtos.iterator();
        for (int week = 0; week < this.weeksToRender; week++)
        {
            for (int dayOfWeekColumnIndex = 0; dayOfWeekColumnIndex < DAYS_IN_WEEK; dayOfWeekColumnIndex++)
            {
                final int weekRowIndex = week + 1;
                this.add(new DayPanelComponent(dayDtoIterator.next()), dayOfWeekColumnIndex, weekRowIndex);
            }
        }
    }
}
