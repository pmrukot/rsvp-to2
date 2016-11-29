package rsvp.booking;

import rsvp.booking.controller.BookingController;
import javafx.application.Application;
import javafx.stage.Stage;

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
        launch(args);
    }
}
