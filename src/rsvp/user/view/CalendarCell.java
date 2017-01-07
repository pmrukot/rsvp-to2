package rsvp.user.view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import rsvp.booking.model.Booking;
import rsvp.resources.model.TimeSlot;

public class CalendarCell{
    private SimpleObjectProperty<TimeSlot> timeSlot;
    private SimpleObjectProperty<Booking> booking;
    private SimpleObjectProperty<Color> color;
    private SimpleStringProperty cellContent;
    private SimpleObjectProperty<Tooltip> tooltip;
    private SimpleIntegerProperty number;

    CalendarCell(TimeSlot timeSlot) {
        this.timeSlot = new SimpleObjectProperty<>(timeSlot);
        this.cellContent = new SimpleStringProperty();
        this.booking = new SimpleObjectProperty<>();
        this.color = new SimpleObjectProperty<>();
        this.number = new SimpleIntegerProperty();
    }

    CalendarCell(TimeSlot timeSlot, String content) {
        this.timeSlot = new SimpleObjectProperty<>(timeSlot);
        this.cellContent = new SimpleStringProperty(content);
        this.booking = new SimpleObjectProperty<>();
        this.color = new SimpleObjectProperty<>();
        this.number = new SimpleIntegerProperty();
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot.set(timeSlot);
    }

    public void setBooking(Booking booking) {
        this.booking.set(booking);
    }

    public void setColor(Color color) {
        this.color.set(color);
    }

    public void setTooltip(Tooltip tooltip) {
        this.tooltip.set(tooltip);
    }

    public int getNumber() {
        return number.get();
    }

    public SimpleIntegerProperty numberProperty() {
        return number;
    }

    public void setNumber(int number) {
        this.number.set(number);
    }

    public String getCellContent() {
        return cellContent.get();
    }

    public Color getColor() {
        return color.get();
    }

    public SimpleObjectProperty<Color> colorProperty() {
        return color;
    }

    public Tooltip getTooltip() {
        return tooltip.get();
    }

    public SimpleObjectProperty<Tooltip> tooltipProperty() {
        return tooltip;
    }

    public SimpleStringProperty cellContentProperty() {
        return cellContent;
    }

    void setCellContent(String cellContent) {
        this.cellContent = new SimpleStringProperty(cellContent);
    }

    void setTooltip(String message) {
        this.tooltip = new SimpleObjectProperty<>(new Tooltip(message));
    }
}
