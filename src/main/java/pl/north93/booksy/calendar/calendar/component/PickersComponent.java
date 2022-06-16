package pl.north93.booksy.calendar.calendar.component;

import com.google.common.eventbus.Subscribe;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pl.north93.booksy.calendar.calendar.PickersController;
import pl.north93.booksy.calendar.employee.dto.EmployeeDto;
import pl.north93.booksy.calendar.employee.event.EmployeesDownloadedEvent;
import pl.north93.booksy.calendar.place.PlaceDto;
import pl.north93.booksy.calendar.place.event.PlacesUpdatedEvent;
import pl.north93.booksy.calendar.service.ServicesVariantsDownloadedEvent;
import pl.north93.booksy.calendar.service.dto.ServiceVariantDto;

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

        this.placesChoiceBox.setOnAction(this::handlePlaceSelect);
        this.servicesChoiceBox.setOnAction(this::handleServiceSelect);
        this.employeesChoiceBox.setOnAction(this::handleEmployeeSelect);

        this.getChildren().add(this.buildPlaceSection());
        this.getChildren().add(this.buildServiceSection());
        this.getChildren().add(this.buildEmployeeSection());
    }

    private VBox buildPlaceSection()
    {
        final Button addPlaceButton = new Button("+");
        addPlaceButton.setOnMouseClicked(event -> this.pickersController.requestAddPlaceDialog());

        final VBox placeVBox = new VBox(new Label("Select place: "), new HBox(this.placesChoiceBox, addPlaceButton));
        placeVBox.setId("pickers-place");

        return placeVBox;
    }

    private VBox buildServiceSection()
    {
        final VBox serviceVbox = new VBox(new Label("Select service&variant: "), this.servicesChoiceBox);
        serviceVbox.setId("pickers-service");

        return serviceVbox;
    }

    private VBox buildEmployeeSection()
    {
        final VBox employeeVbox = new VBox(new Label("Select employee: "), this.employeesChoiceBox);
        employeeVbox.setId("pickers-employee");

        return employeeVbox;
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
