package rsvp.home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
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

    @FXML
    private VBox userPage;

    @FXML
    private TextField login;

    @FXML
    private TextField password;


    public AppController() {}

    AppController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    void initLoginLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(rsvp.home.Main.class.getResource("LoginView.fxml"));
            GridPane rootLayout = (GridPane) loader.load();

            Scene scene = new Scene(rootLayout, 300, 275);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void login(ActionEvent actionEvent) {
        if(login.getText().equals(password.getText())) {
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
            initRootLayout();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Username or password is invalid.");
            alert.setContentText("Try again!");

            alert.showAndWait();
        }
    }


    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(rsvp.home.Main.class.getResource("Main.fxml"));
            Parent rootLayout = (Parent) loader.load();

            Scene scene = new Scene(rootLayout);
            Stage secondaryStage = new Stage();
            secondaryStage.setScene(scene);
            secondaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
