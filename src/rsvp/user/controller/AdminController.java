package rsvp.user.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rsvp.user.model.DBUserDAO;
import rsvp.user.model.User;
import rsvp.user.model.UserDAO;
import rsvp.user.model.UserUtils;
import java.io.File;
import java.io.IOException;

public class AdminController {
    @FXML
    private ComboBox<User> userComboBox;

    public void upload(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        chooser.getExtensionFilters().add(extFilter);
        File file = chooser.showOpenDialog(new Stage());
        try {
            if(file != null){
                System.out.println(UserUtils.createUsersFromCsv(file) + " users were added.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createSingleUSer(ActionEvent actionEvent) {
        System.out.println("implement me");
    }

    public void editUser(ActionEvent actionEvent) {
        User selected = userComboBox.getSelectionModel().getSelectedItem();
        System.out.println("Selected user: " + selected.getLogin());
    }

    public void deleteUser(ActionEvent actionEvent) {
        User selected = userComboBox.getSelectionModel().getSelectedItem();
        System.out.println("Selected user: " + selected.getLogin());
    }

    public void loadUsers() {
        userComboBox.getItems().clear();
        UserDAO userDAO = new DBUserDAO();
        userComboBox.getItems().addAll(userDAO.findUsersByName(""));
        //return userDAO.findUsersByName("");
    }
}
