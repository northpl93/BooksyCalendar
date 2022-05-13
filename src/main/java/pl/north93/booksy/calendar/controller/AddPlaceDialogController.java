package pl.north93.booksy.calendar.controller;

import com.google.common.eventbus.Subscribe;

import pl.north93.booksy.calendar.PlacesService;
import pl.north93.booksy.calendar.components.AddPlaceDialog;
import pl.north93.booksy.calendar.event.RequestAddPlaceDialogEvent;

public class AddPlaceDialogController
{
    private final PlacesService placesService;

    public AddPlaceDialogController(final PlacesService placesService)
    {
        this.placesService = placesService;
    }

    @Subscribe
    public void handleRequestAddPlaceDialogEvent(final RequestAddPlaceDialogEvent event)
    {
        final AddPlaceDialog addPlaceDialog = new AddPlaceDialog();
        addPlaceDialog.showAndWait().ifPresent(this.placesService::addPlace);
    }
}
