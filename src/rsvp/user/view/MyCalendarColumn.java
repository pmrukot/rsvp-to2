package rsvp.user.view;


import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.util.Pair;
import javafx.scene.paint.Color;
import rsvp.booking.model.Booking;

import static javafx.geometry.Insets.EMPTY;

public class MyCalendarColumn extends TableColumn<MyCalendarCell, Pair<String, Color>> {
    public MyCalendarColumn() {
        setCellFactory(column -> new TableCell<MyCalendarCell, Pair<String, Color>>() {
            @Override
            protected void updateItem(Pair<String, Color> value, boolean empty) {
                super.updateItem(value, empty);
                if (value != null) {
                    setText(value.getKey());
                    if (value.getValue() != null) {
                        setBackground(new Background(new BackgroundFill(value.getValue(), CornerRadii.EMPTY, EMPTY)));
                    }
                    else{
                        setBackground(null);
                    }
                }
            }
        });
    }
}
