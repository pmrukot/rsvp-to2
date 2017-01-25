package rsvp.booking.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import rsvp.booking.DAO.BookingDAO;
import rsvp.booking.DAO.DBBookingDAO;
import rsvp.booking.Main;
import rsvp.booking.model.Booking;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rsvp.resources.DAO.TimeSlotDAO;
import rsvp.resources.DAO.UniversityRoomDAO;
import rsvp.resources.model.TimeSlot;
import rsvp.resources.model.UniversityRoom;
import rsvp.user.API.AuthenticationService;
import rsvp.booking.controller.CyclicBookingEditionController;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalTime;
import java.util.Optional;

public class BookingController {

    private TimeSlotDAO timeSlotDAO = new TimeSlotDAO();

    private BookingDAO dbBookingDao = new DBBookingDAO();

    private UniversityRoomDAO universityRoomDAO = new UniversityRoomDAO();

    private Stage primaryStage;

    public BookingController() {}

    public BookingController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private ObservableList<Booking> bookings = FXCollections.observableArrayList();

    private ObservableList<TimeSlot> timeSlots = FXCollections.observableArrayList();

    private ObservableList<UniversityRoom> universityRooms = FXCollections.observableArrayList();

    @FXML
    private DatePicker reservationDatePicker;

    @FXML
    private Button createButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private Button participantsButton;

    @FXML
    private TableView<Booking> bookingsTable;

    @FXML
    private TableColumn<Booking, Date> reservationDate;

    @FXML
    private TableColumn<Booking, LocalTime> startTime;

    @FXML
    private TableColumn<Booking, LocalTime> endTime;

    @FXML
    private TableColumn<Booking, String> ownerLogin;

    @FXML
    private TableColumn<Booking, String> universityRoomNumber;

    @FXML
    private void initialize() {
        dbBookingDao.getAllBookingsForCurrentUser();
        reservationDate.setCellValueFactory(cellData -> new SimpleObjectProperty<Date>(cellData.getValue().getReservationDate()));
        ownerLogin.setCellValueFactory(cellData -> new SimpleObjectProperty<String>(cellData.getValue().getOwner().getLogin()));
        startTime.setCellValueFactory(cellData -> new SimpleObjectProperty<LocalTime>(cellData.getValue().getStartTime()));
        endTime.setCellValueFactory(cellData -> new SimpleObjectProperty<LocalTime>(cellData.getValue().getEndTime()));

        universityRoomNumber.setCellValueFactory(cellData -> {
            UniversityRoom universityRoom = cellData.getValue().getUniversityRoom();
            if (universityRoom == null) {
                return new SimpleObjectProperty<String>("Undefined");
            }
            return new SimpleObjectProperty<String>(universityRoom.getNumber());
        });
        setData();
    }

    public void initRootLayout() {
        try {
            LocalTime local = LocalTime.MIN;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/BookingOverviewPane.fxml"));
            VBox rootLayout = (VBox) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteBooking() {
        try{
            Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
            if(selectedBooking.getRootId() > 0){
                Alert alert = prepareCyclicBookingsAlert();

                Optional<ButtonType> result = alert.showAndWait();
                String stringResult = result.get().getText();
                switch(stringResult){
                    case "Single":
                        dbBookingDao.deleteBooking(selectedBooking);
                        bookingsTable.getItems().remove(selectedBooking);
                        return;
                    case "All":
                        dbBookingDao.deleteCyclicBookings(selectedBooking.getRootId());
                        this.setData();
                        return;
                    case "Cancel":
                        return;
                }
            } else {
                dbBookingDao.deleteBooking(selectedBooking);
                bookingsTable.getItems().remove(selectedBooking);
            }
        } catch (NullPointerException ignored) {}

    }

    private Alert prepareCyclicBookingsAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("This booking is a part of cyclic bookings");
        alert.setHeaderText("Do you want to delete this specific booking or all in series?");
        alert.setContentText("Choose your option.");

        ButtonType buttonTypeYes = new ButtonType("Single");
        ButtonType buttonTypeNo = new ButtonType("All");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo, buttonTypeCancel);

        return alert;
    }

    @FXML
    public void handleCreateAction() {
        Booking booking = new Booking();
        booking.markAsNewRecord(true);
        booking.setOwner(AuthenticationService.getCurrentUser());
        editBooking(booking);
        booking.markAsNewRecord(false);
    }

    @FXML
    public void handleEditAction() {
        Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        if(selectedBooking.getRootId() > 0) {
            editCyclicBooking(selectedBooking);
        } else {
            editBooking(selectedBooking);
        }
    }

    public void editBooking(Booking booking) {
        try {
            final Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setOnHiding(event -> setData());

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/BookingEditionPane.fxml"));
            VBox bookingEditionLayout = (VBox) loader.load();
            Scene scene = new Scene(bookingEditionLayout, 300, 300);

            BookingEditionController bookingEditionController = loader.getController();
            bookingEditionController.setDialogStage(dialogStage);
            bookingEditionController.setBookingController(this);
            bookingEditionController.setData(booking);

            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException ignored) {}

        bookingsTable.refresh();
    }

    private void editCyclicBooking(Booking booking) {
        try {
            final Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setOnHiding(event -> setData());

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/CyclicBookingEditionPane.fxml"));
            VBox bookingEditionLayout = (VBox) loader.load();
            Scene scene = new Scene(bookingEditionLayout, 300, 200);

            CyclicBookingEditionController cyclicBookingEditionController = loader.getController();
            cyclicBookingEditionController.setDialogStage(dialogStage);
            cyclicBookingEditionController.setBookingController(this);
            cyclicBookingEditionController.setData(booking);

            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException ignored) {
            ignored.printStackTrace();
        }

        bookingsTable.refresh();
    }

    @FXML
    public void handleEditParticipantsAction() {
        Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        editParticipants(selectedBooking);
    }

    private void editParticipants(Booking booking) {
        try {
            final Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(primaryStage);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/BookingParticipantsEditionPane.fxml"));
            VBox bookingParticipantsEditionLayout = (VBox) loader.load();
            Scene scene = new Scene(bookingParticipantsEditionLayout, 300, 300);

            BookingParticipantsEditionController bookingParticipantsEditionController = loader.getController();
            bookingParticipantsEditionController.setDialogStage(dialogStage);
            bookingParticipantsEditionController.setBookingController(this);
            bookingParticipantsEditionController.setData(booking);

            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException ignored) {}

        bookingsTable.refresh();
    }

    public void addBooking(Booking booking){
        bookings.add(booking);
    }

    public ObservableList<TimeSlot> getTimeSlots() {
        return this.timeSlots;
    }

    public ObservableList<UniversityRoom> getUniversityRooms() {
        return this.universityRooms;
    }

    public void setData() {
        bookings.setAll(dbBookingDao.getAllBookings());
        timeSlots.setAll(timeSlotDAO.getAll());
        universityRooms.setAll(universityRoomDAO.getAll());
        bookingsTable.setItems(bookings);
    }

    public ObservableList<Booking> getBookings() {
        return this.bookings;
    }
}
