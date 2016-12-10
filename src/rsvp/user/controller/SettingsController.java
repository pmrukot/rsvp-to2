package rsvp.user.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import rsvp.user.API.AuthenticationService;
import rsvp.user.DAO.DBUserDAO;
import rsvp.user.model.User;
import rsvp.user.DAO.UserDAO;

public class SettingsController {

    @FXML
    private TextField oldPassword;

    @FXML
    private TextField newPassword;

    @FXML
    private TextField newPassword2;

    public void changePassword(ActionEvent actionEvent) {
        User currentUser = AuthenticationService.getCurrentUser();
        if(oldPassword.getText().equals(currentUser.getPassword())) {
            if(newPassword.getText().equals(newPassword2.getText())) {
                UserDAO userDAO = new DBUserDAO();
                // todo atm DB instance of current user gets updated but not the applications instance
                // to fix this we should change DAO methods to use User instance and update
                // app's instance User password and then use DAO to update DB.
                // if DAO fails revert password change
                if(userDAO.updatePassword(currentUser.getLogin(), newPassword.getText())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText("Password changed successfully!");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Failed to change password!");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Passwords do not match!");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Current password is incorrect!");
            alert.showAndWait();
        }
    }
}
