package rsvp.resources.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import rsvp.resources.DAO.UniversityRoomDAO;
import rsvp.resources.model.UniversityRoom;
import rsvp.resources.view.BooleanPropertyCell;
import rsvp.resources.view.CalendarCell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UniversityRoomController {
    private static final String CAPACITY_ALERT = "You have to provide capacity greater than 0.";
    private static final String NO_ITEM_SELECTED_ALERT = "You have to select some room in order to do modification.";
    private static final String IMPROPER_NUMBER_FORMAT_ALERT = "You have to provide valid number format.";
    private static final String NO_MODYFICATION_ALERT = "You have to provide different values than before.";
    private static final String NOT_ENOUGH_ARGUMENTS_ALERT = "You have to provide all arguments.";

    @FXML
    TableView<UniversityRoom> universityRoomListTableView;
    @FXML
    TableColumn<UniversityRoom, String> numberColumn;
    @FXML
    TableColumn<UniversityRoom, Integer> capacityColumn;
    @FXML
    TableColumn<UniversityRoom, Boolean> calendarColumn;
    @FXML
    TableColumn<UniversityRoom, Boolean> isComputerRoomColumn;

    @FXML
    private TextField numberFieldCreate;
    @FXML
    private TextField capacityFieldCreate;
    @FXML
    private CheckBox isComputerRoomCheckboxCreate;

    @FXML
    private TextField numberFieldUpdate;
    @FXML
    private TextField capacityFieldUpdate;
    @FXML
    private CheckBox isComputerRoomCheckboxUpdate;

    ObservableList<UniversityRoom> items;
    UniversityRoomDAO universityRoomDAO;

    Alert errorAlert;

    private void handleErrorAlert(TextField firstTextField, TextField secondTextField, String alertMessage) {
        if (alertMessage != null) {
            errorAlert.setContentText(alertMessage);
            errorAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            errorAlert.showAndWait();
        }
        if (firstTextField != null)
            firstTextField.clear();
        if (secondTextField != null)
            secondTextField.clear();
    }

    public void createNewCalendarWindow(UniversityRoom universityRoom) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/CalendarView.fxml"));
            HBox root = loader.load();
            CalendarController controller = loader.<CalendarController>getController();
            controller.setContentForUniversityRoom(universityRoom);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error!");
        errorAlert.setHeaderText("Error while modyfying data");

        items = FXCollections.observableArrayList();
        universityRoomDAO = new UniversityRoomDAO();

        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        numberColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        capacityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        calendarColumn.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
        calendarColumn.setCellFactory(p -> new CalendarCell(this));

        isComputerRoomColumn.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue().getIsComputer()));
        isComputerRoomColumn.setCellFactory(p -> new BooleanPropertyCell());

        items.addAll(universityRoomDAO.getAll());
        universityRoomListTableView.setItems(items);
    }

    @FXML
    private void handleCreateButtonAction(ActionEvent event) {
        if (numberFieldCreate.getText().isEmpty() || capacityFieldCreate.getText().isEmpty()) {
            handleErrorAlert(numberFieldCreate, capacityFieldCreate, NOT_ENOUGH_ARGUMENTS_ALERT);
            return;
        }

        String number = numberFieldCreate.getText();
        Integer capacity;
        try {
            capacity = Integer.parseInt(capacityFieldCreate.getText());
        } catch (NumberFormatException e) {
            handleErrorAlert(numberFieldCreate, capacityFieldCreate, IMPROPER_NUMBER_FORMAT_ALERT);
            return;
        }

        if (capacity < 1) {
            handleErrorAlert(numberFieldCreate, capacityFieldCreate, CAPACITY_ALERT);
            return;
        }

        UniversityRoom createdUniversityRoom = new UniversityRoom(number, capacity,
                isComputerRoomCheckboxCreate.isSelected());
        isComputerRoomCheckboxCreate.setSelected(false);
        items.add(createdUniversityRoom);
        universityRoomDAO.create(createdUniversityRoom);
        handleErrorAlert(numberFieldCreate, capacityFieldCreate, null);
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        UniversityRoom chosenUniversityRoom = universityRoomListTableView.getSelectionModel().getSelectedItem();
        if (chosenUniversityRoom == null) {
            handleErrorAlert(null, null, NO_ITEM_SELECTED_ALERT);
            return;
        }

        universityRoomDAO.delete(chosenUniversityRoom);
        items.remove(chosenUniversityRoom);
    }

    @FXML
    private void handleUpdateButtonAction(ActionEvent event) {
        if (numberFieldUpdate.getText().isEmpty() && capacityFieldUpdate.getText().isEmpty()) {
            handleErrorAlert(numberFieldUpdate, capacityFieldUpdate, NOT_ENOUGH_ARGUMENTS_ALERT);
            return;
        }

        String newNumber = numberFieldUpdate.getText();
        Integer newCapacity;
        Boolean newIsComputer = isComputerRoomCheckboxUpdate.isSelected();
        try {
            newCapacity = Integer.parseInt(capacityFieldUpdate.getText());
        } catch (NumberFormatException e) {
            handleErrorAlert(numberFieldUpdate, capacityFieldUpdate, IMPROPER_NUMBER_FORMAT_ALERT);
            return;
        }

        UniversityRoom chosenUniversityRoom = universityRoomListTableView.getSelectionModel().getSelectedItem();
        if (chosenUniversityRoom == null) {
            handleErrorAlert(numberFieldUpdate, capacityFieldUpdate, NO_ITEM_SELECTED_ALERT);
            return;
        }

        if (chosenUniversityRoom.getNumber().equals(newNumber) &&
                chosenUniversityRoom.getCapacity().equals(newCapacity) &&
                chosenUniversityRoom.getIsComputer() == newIsComputer) {
            handleErrorAlert(numberFieldUpdate, capacityFieldUpdate, NO_MODYFICATION_ALERT);
            return;
        }

        if (newCapacity < 1) {
            handleErrorAlert(numberFieldUpdate, capacityFieldUpdate, CAPACITY_ALERT);
            return;
        }

        universityRoomDAO.update(chosenUniversityRoom, newNumber, newCapacity, newIsComputer);
        handleErrorAlert(numberFieldUpdate, capacityFieldUpdate, null);
        isComputerRoomCheckboxUpdate.setSelected(false);
        items.clear();
        items.addAll(universityRoomDAO.getAll());
        universityRoomListTableView.setItems(items);
    }

    @FXML
    private void handleFileReading(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file for reading");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));
        File file = fileChooser.showOpenDialog(new Stage());
        if(file != null){
            try {
                List<UniversityRoom> newUniversityRooms = new ArrayList<>();
                BufferedReader bufferedFileReader = new BufferedReader(new FileReader(file.getAbsolutePath()));
                String line;
                while ((line = bufferedFileReader.readLine()) != null) {
                    String[] parsedUniversityRoom = line.split(",");
                    UniversityRoom room = new UniversityRoom(parsedUniversityRoom[0].trim(),
                            Integer.valueOf(parsedUniversityRoom[1].trim()),
                            Boolean.valueOf(parsedUniversityRoom[2].trim()));
                    universityRoomDAO.create(room);
                    newUniversityRooms.add(room);
                }
                items.addAll(newUniversityRooms);
                bufferedFileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}