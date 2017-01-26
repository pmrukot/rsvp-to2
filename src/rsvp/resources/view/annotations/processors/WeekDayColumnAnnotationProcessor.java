package rsvp.resources.view.annotations.processors;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import rsvp.resources.controller.CalendarController;
import rsvp.resources.model.CalendarTableItem;
import rsvp.resources.view.annotations.WeekDayColumn;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class WeekDayColumnAnnotationProcessor {

    public void process(CalendarController calendarController) {
        Arrays.stream(calendarController.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(WeekDayColumn.class))
                .forEach(field -> {
                    field.setAccessible(true);
                    try {
                        Object column = field.get(calendarController);

                        column.getClass()
                                .getDeclaredMethod("setDayNumber", int.class)
                                .invoke(column, field.getAnnotation(WeekDayColumn.class).dayNumber());

                        column.getClass()
                                .getDeclaredMethod("setCalendarController", CalendarController.class)
                                .invoke(column, calendarController);

                        column.getClass()
                                .getMethod("setCellValueFactory", Callback.class)
                                .invoke(column, new Callback<CellDataFeatures<CalendarTableItem, CalendarTableItem>,
                                        ObservableValue<CalendarTableItem>>() {
                                    @Override
                                    public ObservableValue<CalendarTableItem> call(CellDataFeatures<CalendarTableItem,
                                            CalendarTableItem> param) {
                                        return new SimpleObjectProperty<>(param.getValue());
                                    }
                                });
                    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
    }

}
