package rsvp.resources.view;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.*;
import rsvp.booking.DAO.BookingDAO;
import rsvp.booking.DAO.DBBookingDAO;
import rsvp.booking.model.Booking;
import rsvp.resources.controller.CalendarController;
import rsvp.resources.model.CalendarTableItem;

import java.util.Arrays;

public class CalendarDayColumn extends TableColumn<CalendarTableItem, CalendarTableItem> {
    int dayNumber;
    CalendarController calendarController;

    public CalendarDayColumn() {
        setCellFactory(column -> new TableCell<CalendarTableItem, CalendarTableItem>() {
            @Override
            protected void updateItem(CalendarTableItem item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    if (item.getBookingDescriptionPerDay(dayNumber) != null){
                        setText(item.getBookingDescriptionPerDay(dayNumber));
                    }
                    if (item.getColor(dayNumber) != null) {
                        setBackground(new Background(new BackgroundFill(
                                item.getColor(dayNumber), CornerRadii.EMPTY, Insets.EMPTY)));
                        addContextMenu(this, item);
                    } else {
                        setBackground(null);
                    }
                }
            }
        });
    }

    public void setDayNumber(int dayNumber){
        this.dayNumber = dayNumber;
    }

    public void setCalendarController(CalendarController calendarController){
        this.calendarController = calendarController;
    }

    private void addContextMenu(TableCell<CalendarTableItem, CalendarTableItem> tableCell, CalendarTableItem item) {
        final ContextMenu contextMenu = new ContextMenu();
        final MenuItem removeMenuItem = new MenuItem("Remove");
        final MenuItem editMenuItem = new MenuItem("Edit");
        removeMenuItem.setOnAction(a -> {
            BookingDAO bookingDAO = new DBBookingDAO();
            Booking booking = item.getBooking(dayNumber);
            bookingDAO.deleteBooking(booking);
            tableCell.setBackground(null);
            tableCell.setText(null);
            calendarController.initializeCalendarTableContent();
        });
        editMenuItem.setOnAction(a -> {
//            initEditBookingLayout(item.getBooking());
//            initializeMyCalendarContent(currentStartDate, currentEndDate);
        });

        contextMenu.getItems().addAll(Arrays.asList(editMenuItem, removeMenuItem));

        tableCell.contextMenuProperty().bind(
                Bindings.when(tableCell.emptyProperty())
                        .then((ContextMenu)null)
                        .otherwise(contextMenu));

    }

}
