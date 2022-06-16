package pl.north93.booksy.calendar.calendar.component;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import pl.north93.booksy.calendar.service.dto.DayDto;

public class DayPanelComponent extends VBox
{
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public DayPanelComponent(final DayDto dayDto)
    {
        final Label dayLabel = new Label(this.buildDayString(dayDto));
        dayLabel.getStyleClass().add("day-label");
        this.getChildren().add(dayLabel);

        final Label timeLabel = new Label(this.buildTimesString(dayDto));
        timeLabel.getStyleClass().add("time-label");
        this.getChildren().add(timeLabel);
    }

    private String buildDayString(final DayDto dayDto)
    {
        return String.valueOf(dayDto.localDate().getDayOfMonth());
    }

    private String buildTimesString(final DayDto dayDto)
    {
        return dayDto.times().stream()
                     .map(localDateTime -> localDateTime.format(TIME_FORMATTER))
                     .collect(Collectors.joining(", "));
    }
}
