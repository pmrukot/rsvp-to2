package rsvp.resources.view;

import javafx.geometry.Insets;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import rsvp.resources.model.CalendarTableItem;

public class CalendarDayColumn extends TableColumn<CalendarTableItem, Pair<String, Color>> {

    public CalendarDayColumn() {
        setCellFactory(column -> new TableCell<CalendarTableItem, Pair<String, Color>>() {
            @Override
            protected void updateItem(Pair<String, Color> value, boolean empty) {
                super.updateItem(value, empty);
                if (value != null) {
                    setText(value.getKey());
                    if (value.getValue() != null) {
                        setBackground(new Background(new BackgroundFill(value.getValue(), CornerRadii.EMPTY, Insets.EMPTY)));
                    } else {
                        setBackground(null);
                    }
                }
            }
        });
    }
}
