package rsvp.resources.view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rsvp.booking.DAO.BookingDAO;
import rsvp.booking.DAO.DBBookingDAO;
import rsvp.booking.model.Booking;
import rsvp.resources.controller.CalendarController;
import rsvp.resources.controller.EditBookingController;
import rsvp.resources.model.CalendarTableItem;

import java.io.IOException;
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

    /**
     * Invoked via reflection by {@link rsvp.resources.view.annotations.processors.WeekDayColumnAnnotationProcessor}
     */
    @SuppressWarnings("unused")
    public void setCalendarController(CalendarController calendarController) {
        this.calendarController = calendarController;
    }

    /**
     * Invoked via reflection by {@link rsvp.resources.view.annotations.processors.WeekDayColumnAnnotationProcessor}
     */
    @SuppressWarnings("unused")
    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    private void addContextMenu(TableCell<CalendarTableItem, CalendarTableItem> tableCell, CalendarTableItem item) {
        final ContextMenu contextMenu = new ContextMenu();
        final MenuItem removeMenuItem = new MenuItem("Remove");
        final MenuItem editMenuItem = new MenuItem("Edit");
        removeMenuItem.setOnAction(a -> {
            BookingDAO bookingDAO = new DBBookingDAO();
            Booking booking = item.getBooking(dayNumber);
            bookingDAO.deleteBooking(booking);
            calendarController.initializeCalendarTableContent();
        });
        editMenuItem.setOnAction(a -> {
            initEditBookingLayout(item.getBooking(dayNumber));
            calendarController.initializeCalendarTableContent();
        });

        contextMenu.getItems().addAll(Arrays.asList(editMenuItem, removeMenuItem));

        tableCell.contextMenuProperty().bind(
                Bindings.when(tableCell.emptyProperty())
                        .then((ContextMenu)null)
                        .otherwise(contextMenu));
    }

    private void initEditBookingLayout(Booking booking) {
        try {
            final Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(rsvp.home.Main.class.getResource("../resources/view/BookingEditionPane.fxml"));
            Parent editBookingLayout = loader.load();
            Scene scene = new Scene(editBookingLayout, 300, 300);

            EditBookingController editBookingController = loader.getController();
            editBookingController.setDialogStage(dialogStage);
            editBookingController.setData(booking);

            dialogStage.setScene(scene);
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
