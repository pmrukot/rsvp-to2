package rsvp.user.controller;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import rsvp.user.API.AuthenticationService;
import rsvp.user.API.UserProviderSingleton;
import rsvp.user.DAO.DBUserDAO;
import rsvp.user.model.User;
import rsvp.user.DAO.UserDAO;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable{
    private UserProviderSingleton instance;
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
        instance = UserProviderSingleton.getInstance();
        User currentUser = AuthenticationService.getCurrentUser();
        login.setText(currentUser.getLogin());
        firstName.setText(currentUser.getFirstName());
        lastName.setText(currentUser.getLastName());
        addChangePasswordBinding();
        addChangeUserDataBinding(currentUser);
    }

    public void changePassword() {
        User currentUser = AuthenticationService.getCurrentUser();
        if(oldPassword.getText().equals(currentUser.getPassword())) {
            if(!newPassword.getText().equals(currentUser.getPassword())) {
                if(newPassword.getText().equals(newPassword2.getText())) {
                    UserDAO userDAO = new DBUserDAO();
                    currentUser.setPassword(newPassword.getText());
                    if(userDAO.updateUser(currentUser)) {
                        showInformationDialog("Password changed successfully!");
                        clearPasswordTextFields();
                        instance.refreshUsers();
                    } else {
                        currentUser.setPassword(oldPassword.getText());
                        showErrorDialog("Failed to change password!");
                    }
                } else {
                    showErrorDialog("Passwords do not match!");
                }
            } else {
                showErrorDialog("New password must be different from current!");
            }
        } else {
            showErrorDialog("Current password is incorrect!");
        }
    }

    public void changeLoginAndName() {
        UserDAO userDAO = new DBUserDAO();
        User currentUser = AuthenticationService.getCurrentUser();
        String backupLogin = currentUser.getLogin();
        String backupFirstName = currentUser.getFirstName();
        String backupLastName = currentUser.getLastName();
        currentUser.setLogin(login.getText());
        currentUser.setFirstName(firstName.getText());
        currentUser.setLastName(lastName.getText());

        if(userDAO.updateUser(currentUser)) {
            showInformationDialog("User data changed successfully!");
            instance.refreshUsers();
            addChangeUserDataBinding(currentUser);
        } else {
            backupUser(backupLogin, backupFirstName, backupLastName, currentUser);
            showErrorDialog("Failed to change user data!");
        }
    }

    private void addChangeUserDataBinding(User currentUser) {
        BooleanBinding changeUserDataBinding = login.textProperty().isEqualTo(currentUser.getLogin())
                .and(firstName.textProperty().isEqualTo(currentUser.getFirstName()))
                .and(lastName.textProperty().isEqualTo(currentUser.getLastName()));
        changeUserDataButton.disableProperty().bind(changeUserDataBinding);
    }

    private void addChangePasswordBinding() {
        BooleanBinding changePasswordBinding = oldPassword.textProperty().isEmpty().or(newPassword.textProperty().isEmpty())
                .or(newPassword2.textProperty().isEmpty());
        changePasswordButton.disableProperty().bind(changePasswordBinding);
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    private void showInformationDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    private void clearPasswordTextFields() {
        oldPassword.setText("");
        newPassword.setText("");
        newPassword2.setText("");
    }

    private void backupUser(String login, String firstName, String lastName, User currentUser) {
        currentUser.setLogin(login);
        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);
    }
}