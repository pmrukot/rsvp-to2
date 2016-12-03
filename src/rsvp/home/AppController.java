package rsvp.home;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rsvp.booking.controller.BookingController;

import java.io.IOException;

public class AppController {
    private Stage primaryStage;

    @FXML
    private BookingController bookingController;

    @FXML
    private VBox bookingPage;

    @FXML
    private VBox resourcesPage;

    public AppController() {}

    public AppController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(rsvp.home.Main.class.getResource("Main.fxml"));
            TabPane rootLayout = (TabPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
