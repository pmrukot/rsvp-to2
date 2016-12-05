package rsvp.resources.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import rsvp.resources.model.UniversityRoom;

public class CalendarCell extends TableCell<UniversityRoom, Boolean> {
    final Button cellButton = new Button("Show");

    public CalendarCell(){
        cellButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                System.out.println("Show button clicked");
            }
        });
    }

    //Display button if the row is not empty
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