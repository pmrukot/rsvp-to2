package rsvp.user.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rsvp.user.API.UserProviderSingleton;
import rsvp.user.DAO.*;
import rsvp.user.model.User;
import rsvp.user.upload.Upload;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements ListChangeListener {
    private UserDAO userDAO;
    private UserProviderSingleton instance;
    private FilteredList<User> filteredList;
    @FXML
    private TextField searchField;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
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
        userDAO = new DBUserDAO();
        instance = UserProviderSingleton.getInstance();
        isAdminColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().isAdmin()));
        usersTable.getColumns().addListener( (ListChangeListener) (c -> { // prevent column reordering
            c.next();
            if(c.wasReplaced()) {
                usersTable.getColumns().clear();
                usersTable.getColumns().addAll(loginColumn, firstNameColumn, lastNameColumn, passwordColumn, isAdminColumn);
            }
        }));
        editButton.disableProperty().bind(Bindings.isEmpty(usersTable.getSelectionModel().getSelectedItems()));
        deleteButton.disableProperty().bind(Bindings.isEmpty(usersTable.getSelectionModel().getSelectedItems()));
        filteredList = new FilteredList<>(instance.getUsers());
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

    @Override
    public void onChanged(Change c) {
        filteredList.filtered(user -> {
            String lowerCaseFilter = searchField.getText().toLowerCase();
            return user.getLastName().toLowerCase().contains(lowerCaseFilter);
        });
    }

    public void upload() {
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
                instance.getUsers().addAll(createdUsers);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void editUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        int index = instance.getUsers().indexOf(selectedUser);
        initEditUserLayout(selectedUser, index);
    }

    private void initEditUserLayout(User updatedUser, int index) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(rsvp.home.Main.class.getResource("../user/view/EditUserView.fxml"));

            Parent editUserLayout = loader.load();

            Scene scene = new Scene(editUserLayout, 600, 300);
            Stage secondaryStage = new Stage();
            secondaryStage.setScene(scene);
            EditUserController controller = loader.<EditUserController>getController();
            controller.initData(updatedUser, index);
            secondaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(String.format("Are you sure that you want to delete user %s?", selectedUser.getLogin()));
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response ->  {
                    if(userDAO.deleteUser(selectedUser)) {
                        String login = selectedUser.getLogin();
                        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                        alert2.setTitle("Information Dialog");
                        alert2.setHeaderText(String.format("Deleting user %s succeeded!", login));
                        alert2.showAndWait();
                        instance.getUsers().remove(selectedUser);
                    } else {
                        Alert alert2 = new Alert(Alert.AlertType.ERROR);
                        alert2.setTitle("Error Dialog");
                        alert2.setHeaderText(String.format("Failed to delete user %s!", selectedUser.getLogin()));
                        alert2.showAndWait();
                    }
                });
    }

    public void addUser(ActionEvent actionEvent) {
        initEditUserLayout(null, 0);
    }
}
