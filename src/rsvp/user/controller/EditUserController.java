package rsvp.user.controller;


import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import rsvp.user.API.UserProviderSingleton;
import rsvp.user.DAO.DBUserDAO;
import rsvp.user.DAO.UserDAO;
import rsvp.user.model.User;
import rsvp.user.view.Alert;

import java.net.URL;
import java.util.ResourceBundle;

public class EditUserController implements Initializable{
    private UserDAO userDAO;
    private UserProviderSingleton instance;
    public CheckBox admin;
    public Button saveEditedUser;
    public TextField firstName;
    public TextField lastName;
    public TextField login;
    public TextField password;
    private User editedUser;
    private int index;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userDAO = new DBUserDAO();
        instance = UserProviderSingleton.getInstance();
    }

    public void initData(User user, int index) {
        editedUser = user;
        this.index = index;
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


    public void createUser() {
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
            instance.getUsers().add(u);
        } else {
            Alert alert = new Alert("Failed to create new user!", AlertType.ERROR);
            alert.showAndWait();
        }
    }


    public void editUser() {
        editedUser.setLogin(login.getText());
        editedUser.setFirstName(firstName.getText());
        editedUser.setLastName(lastName.getText());
        editedUser.setPassword(password.getText());
        editedUser.setAdmin(admin.isSelected());

        if(userDAO.updateUser(editedUser)) {
            Alert alert = new Alert("User edited successfully!\nUser login: " + editedUser.getLogin(), AlertType.INFORMATION);
            alert.showAndWait();
            instance.getUsers().set(index, editedUser);
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
