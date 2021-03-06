package rsvp.booking.controller;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import rsvp.booking.DAO.DBBookingDAO;
import rsvp.booking.model.Booking;
import rsvp.common.persistence.HibernateUtils;
import rsvp.user.DAO.DBUserDAO;
import rsvp.user.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.*;
import java.util.stream.Collectors;


public class BookingParticipantsEditionController {
    private BookingController bookingController;
    private Stage dialogStage;
    private Booking booking;

    private DBUserDAO dbUserDao = new DBUserDAO();
    private DBBookingDAO dbBookingDao = new DBBookingDAO();

    @FXML
    private ListView<User> userList;

    @FXML
    private Button saveButton;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setData(Booking booking) {
        this.booking = booking;

        List<User> allUsers = this.dbUserDao.findUsersByName("");
        List<User> checkedUsers = new ArrayList<>(booking.getParticipants());

        this.userList.getItems().addAll(allUsers);
        this.userList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        List<String> allUsersToString = allUsers.stream()
                .map(object -> Objects.toString(object, null))
                .collect(Collectors.toList());

        List<String> checkedUsersToString = checkedUsers.stream()
                .map(object -> Objects.toString(object, null))
                .collect(Collectors.toList());

        for (String user: checkedUsersToString) {
            this.userList.getSelectionModel().select(allUsersToString.indexOf(user));
        }
    }

    @FXML
    private void updateBooking() {
        Set<User> users = new HashSet<User>(this.userList.getSelectionModel().getSelectedItems());
        booking.setParticipants(users);
        dbBookingDao.updateBooking(booking);
        dialogStage.close();
    }

    public void setBookingController(BookingController bookingController) {
        this.bookingController = bookingController;
    }
}
