package rsvp.resources.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rsvp.resources.model.UniversityRoom;

import java.io.IOException;

public class CalendarCell extends TableCell<UniversityRoom, Boolean> {
    final Button cellButton = new Button("Show");

    public CalendarCell(){
        cellButton.setOnAction(t ->{
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../view/CalendarView.fxml"));
                VBox root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void updateItem(Boolean t, boolean empty) {
        super.updateItem(t, empty);
        if(!empty){
            setGraphic(cellButton);
        }
        else{
            setGraphic(null);
        }
    }
}