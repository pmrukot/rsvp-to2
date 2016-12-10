package rsvp.user.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import rsvp.user.API.AuthenticationService;
import rsvp.user.DAO.DBUserDAO;
import rsvp.user.model.User;
import rsvp.user.DAO.UserDAO;

public class SettingsController {

    private AuthenticationService authenticationService;

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
                if(userDAO.updatePassword(currentUser.getLogin(), newPassword.getText())) {
                    System.out.println("Password was successfully changed.");
                }
            }
        }
    }

    public void logout(ActionEvent actionEvent) {
/*        authenticationService.setCurrentUser(null);
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("RSVP");

        this.appController = new AppController(primaryStage);
        this.appController.initLoginLayout();*/

    }
}
