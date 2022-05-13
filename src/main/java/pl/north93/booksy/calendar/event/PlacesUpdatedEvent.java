package pl.north93.booksy.calendar.event;

import java.util.List;

import pl.north93.booksy.calendar.dto.PlaceDto;

public record PlacesUpdatedEvent(List<PlaceDto> places)
{
}
