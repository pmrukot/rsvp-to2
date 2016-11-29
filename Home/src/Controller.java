import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    public TextField password;
    public TextField login;

    public void login(ActionEvent actionEvent) throws IOException {
        if(login.getText().equals(password.getText())) {

            Stage homeStage = new Stage();
            Group root = new Group();
            homeStage.setTitle("Booking rooms");
            Scene scene = new Scene(root, 600, 500);

            TabPane tabPane = new TabPane();

            BorderPane borderPane = new BorderPane();
            for (int i = 0; i < 5; i++) {
                Tab tab = new Tab();
                tab.setText("Tab" + i);
                HBox hbox = new HBox();
                hbox.getChildren().add(new Label("Tab" + i));
                hbox.setAlignment(Pos.CENTER);
                tab.setContent(hbox);
                tabPane.getTabs().add(tab);
            }
            // bind to take available space
            borderPane.prefHeightProperty().bind(scene.heightProperty());
            borderPane.prefWidthProperty().bind(scene.widthProperty());

            borderPane.setCenter(tabPane);
            root.getChildren().add(borderPane);
            homeStage.setScene(scene);



            homeStage.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Username or password is invalid.");
            alert.setContentText("Try again!");

            alert.showAndWait();
        }
    }
}
