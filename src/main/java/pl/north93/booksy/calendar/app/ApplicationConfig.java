package pl.north93.booksy.calendar.app;

import java.util.List;

import pl.north93.booksy.calendar.place.PlaceDto;

public record ApplicationConfig(List<PlaceDto> places)
{
}
