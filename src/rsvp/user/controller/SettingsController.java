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
                currentUser.setPassword(newPassword.getText());
                if(userDAO.updateUser(currentUser)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText("Password changed successfully!");
                    alert.showAndWait();
                } else {
                    currentUser.setPassword(oldPassword.getText());
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
