package rsvp.user.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
    @FXML
    private TextField searchField;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private ObservableList<User> users;
    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, String> loginColumn;
    @FXML
    private TableColumn<User, String> firstNameColumn;
    @FXML
    private TableColumn<User, String> lastNameColumn;
    @FXML
    private TableColumn<User, String> passwordColumn;
    @FXML
    private TableColumn<User, Boolean> isAdminColumn;
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
        isAdminColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().isAdmin())); // todo how to do it in fxml?
        udao = new DBUserDAO();
        users = FXCollections.observableArrayList(udao.findUsersByName(""));
        usersTable.getColumns().addListener( (ListChangeListener) (c -> { // prevent column reordering
            c.next();
            if(c.wasReplaced()) {
                usersTable.getColumns().clear();
                usersTable.getColumns().addAll(loginColumn, firstNameColumn, lastNameColumn, passwordColumn, isAdminColumn);
            }
        }));
        editButton.disableProperty().bind(Bindings.isEmpty(usersTable.getSelectionModel().getSelectedItems()));
        deleteButton.disableProperty().bind(Bindings.isEmpty(usersTable.getSelectionModel().getSelectedItems()));
        addButton.disableProperty().bind(firstNameField.textProperty().isEmpty()
                .or(lastNameField.textProperty().isEmpty())
                .or(isAdminField.textProperty().isEmpty())
        );
        FilteredList<User> filteredList = new FilteredList<>(users);
        searchField.textProperty().addListener( (observable, oldValue, newValue) -> filteredList.setPredicate(user -> {
                if(newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return user.getLastName().toLowerCase().contains(lowerCaseFilter);
            })
        );
        SortedList<User> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(usersTable.comparatorProperty());
        usersTable.setItems(sortedList);
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
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
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
