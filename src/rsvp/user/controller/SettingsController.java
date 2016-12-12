package rsvp.user.controller;

import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import rsvp.user.API.AuthenticationService;
import rsvp.user.DAO.DBUserDAO;
import rsvp.user.model.User;
import rsvp.user.DAO.UserDAO;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable{

    public TextField login;
    public TextField firstName;
    public TextField lastName;


    @FXML
    private TextField oldPassword;

    @FXML
    private TextField newPassword;

    @FXML
    private TextField newPassword2;

    @FXML
    private Button changePasswordButton;

    @FXML
    private Button changeUserDataButton;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        User currentUser = AuthenticationService.getCurrentUser();
        login.setText(currentUser.getLogin());
        firstName.setText(currentUser.getFirstName());
        lastName.setText(currentUser.getLastName());
        BooleanBinding changePasswordBinding = oldPassword.textProperty().isEmpty().or(newPassword.textProperty().isEmpty())
                .or(newPassword2.textProperty().isEmpty());
        changePasswordButton.disableProperty().bind(changePasswordBinding);
        BooleanBinding changeUserDataBinding = login.textProperty().isEqualTo(currentUser.getLogin())
                .and(firstName.textProperty().isEqualTo(currentUser.getFirstName()))
                .and(lastName.textProperty().isEqualTo(currentUser.getLastName()));
        changeUserDataButton.disableProperty().bind(changeUserDataBinding);
    }

    public void changePassword(ActionEvent actionEvent) {
        User currentUser = AuthenticationService.getCurrentUser();

        if(oldPassword.getText().equals(currentUser.getPassword())) {
            if(!newPassword.getText().equals(currentUser.getPassword())) {
                if(newPassword.getText().equals(newPassword2.getText())) {
                    UserDAO userDAO = new DBUserDAO();
                    currentUser.setPassword(newPassword.getText());
                    if(userDAO.updateUser(currentUser)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText("Password changed successfully!");
                        alert.showAndWait();
                        oldPassword.setText("");
                        newPassword.setText("");
                        newPassword2.setText("");
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
                alert.setHeaderText("New password must be different from current!");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Current password is incorrect!");
            alert.showAndWait();
        }
    }

    public void changeLoginAndName(ActionEvent actionEvent) {
        UserDAO userDAO = new DBUserDAO();
        User currentUser = AuthenticationService.getCurrentUser();
        String backupLogin = currentUser.getLogin();
        String backupFirstName = currentUser.getFirstName();
        String backupLastName = currentUser.getLastName();
        currentUser.setLogin(login.getText());
        currentUser.setFirstName(firstName.getText());
        currentUser.setLastName(lastName.getText());

        if(userDAO.updateUser(currentUser)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("User data changed successfully!");
            alert.showAndWait();
        } else {
            currentUser.setLogin(backupLogin);
            currentUser.setFirstName(backupFirstName);
            currentUser.setLastName(backupLastName);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Failed to change user data!");
            alert.showAndWait();
        }
    }
}
