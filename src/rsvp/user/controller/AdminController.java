package rsvp.user.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rsvp.user.DAO.DBUserDAO;
import rsvp.user.DAO.UserDAO;
import rsvp.user.command.Command;
import rsvp.user.command.DeleteUserCommand;
import rsvp.user.model.ObservableQueue;
import rsvp.user.model.User;
import rsvp.user.upload.Upload;
import rsvp.user.view.Alert;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AdminController implements ListChangeListener {
    private UserDAO userDAO;
    private FilteredList<User> filteredList;
    private ObservableQueue<Command> executedCommands;

    @FXML
    private Button undoButton;
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
    @SuppressWarnings("unchecked")
    public void initialize() {
        userDAO = new DBUserDAO();
        executedCommands = new ObservableQueue<>();
        UserListManagerSingleton.getInstance().addListener(this);
        filteredList = new FilteredList<>(UserListManagerSingleton.getInstance().getUsers());
        isAdminColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().isAdmin()));
        usersTable.getColumns().addListener( (ListChangeListener) (c -> { // prevent column reordering
            c.next();
            if(c.wasReplaced()) {
                usersTable.getColumns().clear();
                usersTable.getColumns().addAll(loginColumn, firstNameColumn, lastNameColumn, passwordColumn, isAdminColumn);
            }
        }));
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
        undoButton.disableProperty().bind(Bindings.isEmpty(executedCommands));
        editButton.disableProperty().bind(Bindings.isEmpty(usersTable.getSelectionModel().getSelectedItems()));
        deleteButton.disableProperty().bind(Bindings.isEmpty(usersTable.getSelectionModel().getSelectedItems()));
    }

    @Override
    public void onChanged(Change c) {
        // when source list changed re-apply filter
        filteredList.filtered(user -> {
            String lowerCaseFilter = searchField.getText().toLowerCase();
            return user.getLastName().toLowerCase().contains(lowerCaseFilter);
        });
        usersTable.getSelectionModel().clearSelection();
    }

    public void upload() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        chooser.getExtensionFilters().add(extFilter);
        File file = chooser.showOpenDialog(new Stage());
        try {
            if(file != null){
                // todo rewrite upload to support command
                List<User> createdUsers = Upload.createUsersFromCsv(file);
                Alert alert = new Alert(String.format("%s users were created!", createdUsers.size()), AlertType.INFORMATION);
                alert.showAndWait();
                UserListManagerSingleton.getInstance().addAllUsers(createdUsers);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        initEditUserLayout(selectedUser);
    }

    private void initEditUserLayout(User updatedUser) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(rsvp.home.Main.class.getResource("../user/view/EditUserView.fxml"));
            Parent editUserLayout = loader.load();
            Scene scene = new Scene(editUserLayout, 600, 300);
            Stage secondaryStage = new Stage();
            secondaryStage.setScene(scene);
            EditUserController controller = loader.getController();
            controller.initData(updatedUser, userDAO, executedCommands);
            secondaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser() {
        // todo alert decomposition broke this. adding return in showAndWait kind of fixed it.
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(String.format("Are you sure that you want to delete user %s?", selectedUser.getLogin()), AlertType.CONFIRMATION);
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    String login = selectedUser.getLogin();
                    Command c = new DeleteUserCommand(userDAO, selectedUser);
                    if(c.execute()) {
                        executedCommands.add(c);
                        Alert alert2 = new Alert(String.format("Deleting user %s succeeded!", login), AlertType.INFORMATION);
                        alert2.showAndWait();
                    } else {
                        Alert alert2 = new Alert(String.format("Failed to delete user %s!", selectedUser.getLogin()), AlertType.ERROR);
                        alert2.showAndWait();
                    }
                });
    }

    public void addUser() {
        initEditUserLayout(null);
    }

    public void undoCommand() {
        Command c = executedCommands.remove();
        c.undo();
    }
}
