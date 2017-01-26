package rsvp.resources.view;


import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import rsvp.resources.model.UniversityRoom;

public class BooleanPropertyCell extends TableCell<UniversityRoom, Boolean> {
    CheckBox checkbox;

    public BooleanPropertyCell() {
        checkbox = new CheckBox();
        this.setStyle("-fx-alignment: CENTER;");
        checkbox.setStyle("-fx-opacity: 1;");
        checkbox.setDisable(true);
    }

    @Override
    protected void updateItem(Boolean t, boolean empty) {
        super.updateItem(t, empty);
        if (!empty) {
            setGraphic(checkbox);
            UniversityRoom universityRoom = (UniversityRoom) this.getTableRow().getItem();
            if (universityRoom != null) {
                boolean shouldBeChecked = universityRoom.getIsComputer();
                checkbox.setSelected(shouldBeChecked);
            }
        } else {
            setGraphic(null);
        }
    }
}
