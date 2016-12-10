package rsvp.booking;

import rsvp.booking.controller.BookingController;
import javafx.application.Application;
import javafx.stage.Stage;
import rsvp.user.DAO.DBUserDAO;
import rsvp.user.DAO.UserDAO;

public class Main extends Application{

    private Stage primaryStage;
    private BookingController bookingController;


    public Main() {}

    @Override
    public void start(Stage primaryStage) {



        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Create booking");

        this.bookingController = new BookingController(primaryStage);
        this.bookingController.initRootLayout();
    }

    public static void main(String[] args) {
         UserDAO userDAO = new DBUserDAO();
        userDAO.createUser("A", "B", "pass", true);
        userDAO.createUser("C", "D", "pass2", false);
        launch(args);
    }
}
