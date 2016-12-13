package rsvp.booking.controller;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import rsvp.booking.model.Booking;
import rsvp.common.persistence.HibernateUtils;
import rsvp.user.DAO.DBUserDAO;
import rsvp.user.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BookingParticipantsEditionController {
    private BookingController bookingController;
    private Stage dialogStage;
    private Booking booking;

    private DBUserDAO dbUserDao = new DBUserDAO();

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
        this.userList.getItems().addAll(allUsers);
        this.userList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    private void updateBooking() {
        Set<User> users = new HashSet<User>(this.userList.getSelectionModel().getSelectedItems());

        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        booking.setParticipants(users);

        session.update(booking);

        transaction.commit();
        session.close();
    }

    public void setBookingController(BookingController bookingController) {
        this.bookingController = bookingController;
    }
}
