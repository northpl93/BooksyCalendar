package pl.north93.booksy.calendar.components;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import pl.north93.booksy.calendar.dto.DayDto;

public class DayPanelComponent extends VBox
{
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public DayPanelComponent(final DayDto dayDto)
    {
        final Label dayLabel = new Label("" + dayDto.localDate().getDayOfMonth());
        dayLabel.getStyleClass().add("day-label");
        this.getChildren().add(dayLabel);

        final Label timeLabel = new Label(this.buildTimesString(dayDto));
        timeLabel.setWrapText(true);
        this.getChildren().add(timeLabel);
    }

    private String buildTimesString(final DayDto dayDto)
    {
        return dayDto.times().stream().map(localDateTime -> localDateTime.format(TIME_FORMATTER)).collect(Collectors.joining(", "));
    }
}
