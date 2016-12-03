package rsvp.user.controller;


import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rsvp.user.model.UserUtils;

import java.io.File;

public class AdminController {

    public void upload(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        chooser.getExtensionFilters().add(extFilter);
        File file = chooser.showOpenDialog(new Stage());
        UserUtils.createUsersFromCsv(file);
    }
}
