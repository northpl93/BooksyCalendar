package pl.north93.booksy.calendar.components;

import javafx.scene.control.TextInputDialog;

public class AddPlaceDialog extends TextInputDialog
{
    public AddPlaceDialog()
    {
        this.setTitle("Add a new place");
        this.setHeaderText("Paste place ID here");
        this.setContentText("Place ID");
    }
}
