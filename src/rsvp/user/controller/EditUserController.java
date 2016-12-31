package rsvp.user.controller;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import rsvp.user.DAO.UserDAO;
import rsvp.user.model.User;
import rsvp.user.view.Alert;

public class EditUserController {
    private int index;
    private User editedUser;
    private UserDAO userDAO;
    @FXML
    public TextField login;
    @FXML
    public TextField firstName;
    @FXML
    public TextField lastName;
    @FXML
    public TextField password;
    @FXML
    public CheckBox admin;
    @FXML
    public Button saveEditedUser;

    void initData(int index, User user, UserDAO userDAO) {
        this.index = index;
        editedUser = user;
        this.userDAO = userDAO;
        if(user != null) {
            login.setText(user.getLogin());
            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());
            password.setText(user.getPassword());
            admin.setSelected(user.isAdmin());
            editUserBinding();
        } else {
            addUserBinding();
        }
    }

    public void saveEditedUser(ActionEvent actionEvent) {
        if(editedUser ==  null) {
            createUser();
        } else {
            editUser();
        }
        editedUser = null;
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    private void createUser() {
        User u;
        if(login.getText().equals("")) {
            if(password.getText().equals("")) {
                u = new User(firstName.getText(), lastName.getText(), admin.isSelected());
            } else {
                u = new User(firstName.getText(), lastName.getText(), password.getText(), admin.isSelected());
            }
        } else {
            u = new User(login.getText(), firstName.getText(), lastName.getText(), password.getText(), admin.isSelected());
        }
        if(userDAO.createUser(u)) {
            Alert alert = new Alert("Created new user successfully!\nUser login: " + u.getLogin(), AlertType.INFORMATION);
            alert.showAndWait();
            UserListManagerSingleton.getInstance().addUser(u);
        } else {
            Alert alert = new Alert("Failed to create new user!", AlertType.ERROR);
            alert.showAndWait();
        }
    }

    private void editUser() {
        editedUser.setLogin(login.getText());
        editedUser.setFirstName(firstName.getText());
        editedUser.setLastName(lastName.getText());
        editedUser.setPassword(password.getText());
        editedUser.setAdmin(admin.isSelected());

        if(userDAO.updateUser(editedUser)) {
            Alert alert = new Alert("User edited successfully!\nUser login: " + editedUser.getLogin(), AlertType.INFORMATION);
            alert.showAndWait();
            UserListManagerSingleton.getInstance().updateUser(index, editedUser);
        } else {
            Alert alert = new Alert("Failed to create new user!", AlertType.ERROR);
            alert.showAndWait();
        }
    }

    private void addUserBinding() {
        saveEditedUser.disableProperty().bind(firstName.textProperty().isEmpty()
                .or(lastName.textProperty().isEmpty())
        );
    }

    private void editUserBinding() {
        BooleanProperty isAdmin = new SimpleBooleanProperty(editedUser.isAdmin());

        BooleanBinding changeUserDataBinding = login.textProperty().isEqualTo(editedUser.getLogin())
                .and(firstName.textProperty().isEqualTo(editedUser.getFirstName()))
                .and(lastName.textProperty().isEqualTo(editedUser.getLastName()))
                .and(password.textProperty().isEqualTo(editedUser.getPassword()))
                .and(admin.selectedProperty().isEqualTo(isAdmin));

        saveEditedUser.disableProperty().bind(changeUserDataBinding);
    }
}
