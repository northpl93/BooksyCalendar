package pl.north93.booksy.calendar.place.event;

import java.util.List;

import pl.north93.booksy.calendar.place.PlaceDto;

public record PlacesUpdatedEvent(List<PlaceDto> places)
{
}
