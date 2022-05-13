package pl.north93.booksy.calendar.components;

import com.google.common.eventbus.Subscribe;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pl.north93.booksy.calendar.controller.PickersController;
import pl.north93.booksy.calendar.dto.EmployeeDto;
import pl.north93.booksy.calendar.dto.PlaceDto;
import pl.north93.booksy.calendar.dto.ServiceVariantDto;
import pl.north93.booksy.calendar.event.EmployeesDownloadedEvent;
import pl.north93.booksy.calendar.event.PlacesUpdatedEvent;
import pl.north93.booksy.calendar.event.ServicesVariantsDownloadedEvent;

public class PickersComponent extends HBox
{
    private final PickersController pickersController;

    private final ChoiceBox<PlaceDto> placesChoiceBox = new ChoiceBox<>();
    private final ChoiceBox<ServiceVariantDto> servicesChoiceBox = new ChoiceBox<>();
    private final ChoiceBox<EmployeeDto> employeesChoiceBox = new ChoiceBox<>();

    public PickersComponent(final PickersController pickersController)
    {
        this.pickersController = pickersController;
        this.setId("pickers");

        final Button addPlaceButton = new Button("+");
        addPlaceButton.setOnMouseClicked(event -> this.pickersController.requestAddPlaceDialog());

        final VBox placeVBox = new VBox(new Label("Select place: "), new HBox(this.placesChoiceBox, addPlaceButton));
        placeVBox.setMinWidth(100);

        this.placesChoiceBox.setOnAction(this::handlePlaceSelect);
        this.getChildren().add(placeVBox);


        final VBox serviceVbox = new VBox(new Label("Select service&variant: "), this.servicesChoiceBox);
        serviceVbox.setMinWidth(300);

        this.servicesChoiceBox.setOnAction(this::handleServiceSelect);
        this.getChildren().add(serviceVbox);

        final VBox employeeVbox = new VBox(new Label("Select employee: "), this.employeesChoiceBox);
        employeeVbox.setMinWidth(300);

        this.employeesChoiceBox.setOnAction(this::handleEmployeeSelect);
        this.getChildren().add(employeeVbox);
    }

    private void handlePlaceSelect(final ActionEvent event)
    {
        final PlaceDto selectedPlace = this.placesChoiceBox.getSelectionModel().getSelectedItem();
        if (selectedPlace != null)
        {
            this.pickersController.requestServiceVariantDownload(selectedPlace.id());
        }
    }

    private void handleServiceSelect(final ActionEvent event)
    {
        final ServiceVariantDto selectedService = this.servicesChoiceBox.getSelectionModel().getSelectedItem();
        if (selectedService != null)
        {
            this.pickersController.requestCalendarDownload(selectedService);
        }
    }

    private void handleEmployeeSelect(final ActionEvent event)
    {
        final EmployeeDto selectedEmployee = this.employeesChoiceBox.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null)
        {
            this.pickersController.requestEmployeeSelect(selectedEmployee);
        }
    }

    @Subscribe
    private void handlePlacesUpdated(final PlacesUpdatedEvent event)
    {
        this.placesChoiceBox.getItems().setAll(event.places());
        this.placesChoiceBox.getSelectionModel().selectLast();
    }

    @Subscribe
    private void handleServicesVariantsDownloaded(final ServicesVariantsDownloadedEvent event)
    {
        this.servicesChoiceBox.getItems().setAll(event.serviceVariantDtos());
    }

    @Subscribe
    private void handleEmployeesDownloaded(final EmployeesDownloadedEvent event)
    {
        this.employeesChoiceBox.getItems().setAll(event.employees());
        this.employeesChoiceBox.getSelectionModel().selectFirst();
    }
}
