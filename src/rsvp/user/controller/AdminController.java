package rsvp.user.controller;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rsvp.user.DAO.*;
import rsvp.user.model.User;
import rsvp.user.upload.Upload;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class AdminController {
    private UserDAO udao;
    private User selectedUser;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private ObservableList<User> users;
    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, String> login;
    @FXML
    private TableColumn<User, String> firstName;
    @FXML
    private TableColumn<User, String> lastName;
    @FXML
    private TableColumn<User, String> password;
    @FXML
    private TableColumn<User, Boolean> isAdmin;
    @FXML
    private TextField loginField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField isAdminField;
    @FXML
    private Button addButton;

    @FXML
    @SuppressWarnings("unchecked")
    public void initialize() {
        isAdmin.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().isAdmin())); // todo how to do it in fxml?
        udao = new DBUserDAO();
        users = FXCollections.observableArrayList(udao.findUsersByName(""));
        usersTable.setItems(users);
        usersTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                selectedUser = newValue; // selectedUser = usersTable.getSelectionModel().getSelectedItem();
            }
        });
        usersTable.getColumns().addListener( (ListChangeListener) (c -> {
            // prevent column reordering
            c.next();
            if(c.wasReplaced()) {
                usersTable.getColumns().clear();
                usersTable.getColumns().addAll(login, firstName, lastName, password, isAdmin);
            }
        }));
        //editButton.disableProperty().bind(Bindings.isNotNull());
        //deleteButton.disableProperty().bind(Bindings.isNotNull();
        BooleanBinding addBinding = firstNameField.textProperty().isEmpty()
                .or(lastNameField.textProperty().isEmpty())
                .or(isAdminField.textProperty().isEmpty());
        addButton.disableProperty().bind(addBinding);
    }

    public void upload(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        chooser.getExtensionFilters().add(extFilter);
        File file = chooser.showOpenDialog(new Stage());
        try {
            if(file != null){
                List<User> createdUsers = Upload.createUsersFromCsv(file);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(String.format("%s users were created!", createdUsers.size()));
                alert.showAndWait();
                users.addAll(createdUsers);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createSingleUser(ActionEvent actionEvent) {
        // todo error checking
        User u;
        if(loginField.getText().equals("")) {
            if(passwordField.getText().equals("")) {
                u = new User(firstNameField.getText(), lastNameField.getText(), Boolean.valueOf(isAdminField.getText()));
            } else {
                u = new User(firstNameField.getText(), lastNameField.getText(), passwordField.getText(), Boolean.valueOf(isAdminField.getText()));
            }
        } else {
            u = new User(loginField.getText(), firstNameField.getText(), lastNameField.getText(), passwordField.getText(), Boolean.valueOf(isAdminField.getText()));
        }
        if(udao.createUser(u)) {
            users.add(u);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Created new user successfully!\nUser login: " + u.getLogin());
            alert.showAndWait();
            loginField.setText("");
            firstNameField.setText("");
            lastNameField.setText("");
            passwordField.setText("");
            isAdminField.setText("");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Failed to create new user!");
            alert.showAndWait();
        }
    }

    public void editUser(ActionEvent actionEvent) {
        // todo implement me (and maybe change to "save changes" depending on implementation)
        System.out.println("editUser()");
    }

    public void deleteUser(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(String.format("Are you sure that you want to delete user %s?", selectedUser.getLogin()));
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response ->  {
                    if(udao.deleteUser(selectedUser)) {
                        String login = selectedUser.getLogin();
                        users.remove(selectedUser);
                        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                        alert2.setTitle("Information Dialog");
                        alert2.setHeaderText(String.format("Deleting user %s succeeded!", login));
                        alert2.showAndWait();
                    } else {
                        Alert alert2 = new Alert(Alert.AlertType.ERROR);
                        alert2.setTitle("Error Dialog");
                        alert2.setHeaderText(String.format("Failed to delete user %s!", selectedUser.getLogin()));
                        alert2.showAndWait();
                    }
                });
    }
}
