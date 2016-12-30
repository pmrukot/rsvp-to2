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
import rsvp.user.API.AuthenticationService;

import java.io.IOException;

public class AppController {
    private Stage primaryStage;

    private AuthenticationService authenticationService;

    @FXML
    private BookingController bookingController;

    @FXML
    private VBox bookingPage;

    @FXML
    private TabPane resourcesPage;

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
            loader.setLocation(rsvp.home.Main.class.getResource("view/LoginView.fxml"));
            GridPane rootLayout = loader.load();

            Scene scene = new Scene(rootLayout, 300, 275);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login(ActionEvent actionEvent) {
        if(AuthenticationService.authenticateUser(login.getText(), password.getText())) {
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
            initRootLayout(AuthenticationService.getCurrentUser().isAdmin());
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Username or password is invalid.");
            alert.setContentText("Try again!");

            alert.showAndWait();
        }
    }

    public void logout(ActionEvent actionEvent) {
        if(AuthenticationService.logout()) {
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
            this.primaryStage = new Stage();
            this.primaryStage.setTitle("RSVP");
            initLoginLayout();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Log out failed."); // this should never happen though
            alert.showAndWait();
        }
    }

    private void initRootLayout(boolean isAdmin) {
        try {
            FXMLLoader loader = new FXMLLoader();
            if(isAdmin) {
                loader.setLocation(rsvp.home.Main.class.getResource("view/AdminHome.fxml"));
            } else {
                loader.setLocation(rsvp.home.Main.class.getResource("view/Home.fxml"));
            }
            Parent rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            Stage secondaryStage = new Stage();
            secondaryStage.setScene(scene);
            secondaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
