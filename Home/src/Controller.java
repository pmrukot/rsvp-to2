import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    public TextField password;
    public TextField login;

    public void login(ActionEvent actionEvent) throws IOException {
        if(login.getText().equals(password.getText())) {

            Stage homeStage = new Stage();
            Group root = new Group();
            homeStage.setTitle("Booking rooms");
            Scene scene = new Scene(root, 800, 600);

            TabPane tabPane = new TabPane();

            BorderPane borderPane = new BorderPane();

            Tab myCalendarTab = new Tab();
            myCalendarTab.setText("My calendar");


            Label label = new Label("My Calendar");
            label.setFont(new Font("Arial", 20));


            TableView table = new TableView();
            table.setEditable(false);
            table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            List<TableColumn> columns = new ArrayList();
            for(int i = 1; i<=7 ; i++) {
                columns.add(new TableColumn(DayOfWeek.of(i).name()));
            }

            table.getColumns().addAll(columns);

            VBox vBox = new VBox();
            vBox.setSpacing(5);
            vBox.setPadding(new Insets(0, 10, 10, 10));
            vBox.getChildren().addAll(label, table);

            vBox.setAlignment(Pos.CENTER);

            myCalendarTab.setContent(vBox);
            tabPane.getTabs().add(myCalendarTab);

            for (int i = 0; i < 2; i++) {
                Tab tab = new Tab();
                tab.setText("Tab" + i);
                HBox h = new HBox();
                h.getChildren().add(new Label("Tab" + i));
                h.setAlignment(Pos.CENTER);
                tab.setContent(h);
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
