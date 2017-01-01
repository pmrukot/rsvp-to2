package rsvp.resources.view;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import rsvp.resources.controller.UniversityRoomController;
import rsvp.resources.model.UniversityRoom;

public class CalendarCell extends TableCell<UniversityRoom, Boolean> {
    final Button cellButton = new Button("Show");


    public CalendarCell(UniversityRoomController universityRoomController){
        cellButton.setOnAction(t ->
                universityRoomController.createNewCalendarWindow((UniversityRoom)this.getTableRow().getItem()));
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