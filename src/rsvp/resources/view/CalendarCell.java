package rsvp.resources.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import rsvp.resources.controller.CalendarController;
import rsvp.resources.model.UniversityRoom;

import java.io.IOException;

public class CalendarCell extends TableCell<UniversityRoom, Boolean> {
    final Button cellButton = new Button("Show");


    public CalendarCell(){
        cellButton.setOnAction(t ->{
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/CalendarView.fxml"));
            HBox root = loader.load();
            CalendarController controller = loader.<CalendarController>getController();
            controller.setContentForUniversityRoom((UniversityRoom)this.getTableRow().getItem());
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }});
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