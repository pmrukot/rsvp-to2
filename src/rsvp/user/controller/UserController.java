package rsvp.user.controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import rsvp.booking.DAO.BookingDAO;
import rsvp.booking.DAO.DBBookingDAO;
import rsvp.booking.controller.BookingController;
import rsvp.booking.model.Booking;
import rsvp.resources.DAO.TimeSlotDAO;
import rsvp.resources.model.TimeSlot;
import rsvp.resources.model.UniversityRoom;
import rsvp.user.model.User;
import rsvp.user.view.CalendarCell;
import rsvp.user.view.ReservationPerWeek;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

import static javafx.geometry.Insets.EMPTY;

public class UserController {
    public Button refreshButton;
    private Date currentStartDate;
    private Date currentEndDate;
    private ObservableList<ReservationPerWeek> calendarTableItems = FXCollections.observableArrayList();

    @FXML
    private Button previousButton;
    @FXML
    private Label dateRangeLabel;
    @FXML
    private Button nextButton;
    @FXML
    private TableView<ReservationPerWeek> myCalendarTable;

    @FXML
    public TableColumn<ReservationPerWeek, CalendarCell> slots;
    @FXML
    private TableColumn<ReservationPerWeek, CalendarCell> monday;
    @FXML
    private TableColumn<ReservationPerWeek, CalendarCell> tuesday;
    @FXML
    private TableColumn<ReservationPerWeek, CalendarCell> wednesday;
    @FXML
    private TableColumn<ReservationPerWeek, CalendarCell> thursday;
    @FXML
    private TableColumn<ReservationPerWeek, CalendarCell> friday;
    @FXML
    private TableColumn<ReservationPerWeek, CalendarCell> saturday;
    @FXML
    private TableColumn<ReservationPerWeek, CalendarCell> sunday;

    @FXML
    private DatePicker reservationDatePicker;

    @FXML
    private ComboBox<UniversityRoom> universityRoom;

    @FXML
    private ComboBox<TimeSlot> firstTimeSlot;

    @FXML
    private ComboBox<TimeSlot> lastTimeSlot;

    @FXML
    private Button updateButton;

    private Booking booking;

    @FXML
    public void initialize() {
        myCalendarTable.setEditable(true);
        slots.setCellValueFactory(new PropertyValueFactory<>("slots"));
        monday.setCellValueFactory(new PropertyValueFactory<>("monday"));
        tuesday.setCellValueFactory(new PropertyValueFactory<>("tuesday"));
        wednesday.setCellValueFactory(new PropertyValueFactory<>("wednesday"));
        thursday.setCellValueFactory(new PropertyValueFactory<>("thursday"));
        friday.setCellValueFactory(new PropertyValueFactory<>("friday"));
        saturday.setCellValueFactory(new PropertyValueFactory<>("saturday"));
        sunday.setCellValueFactory(new PropertyValueFactory<>("sunday"));



        currentStartDate = getDate(DayOfWeek.MONDAY, LocalDate.now());
        currentEndDate = getDate(DayOfWeek.SUNDAY, LocalDate.now());
        setDateRangeLabel();
        setIcons();
        initializeMyCalendarContent(currentStartDate, currentEndDate);

    }


    private void init(TableColumn<ReservationPerWeek, CalendarCell> column) {
        column.setCellFactory(new Callback<TableColumn<ReservationPerWeek, CalendarCell>, TableCell<ReservationPerWeek, CalendarCell>>() {
            @Override
            public TableCell<ReservationPerWeek, CalendarCell> call(TableColumn<ReservationPerWeek, CalendarCell> param) {
                TableCell<ReservationPerWeek, CalendarCell> tableCell = new TableCell<ReservationPerWeek, CalendarCell>(){

                    @Override
                    protected void updateItem(CalendarCell item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            if(item.getCellContent() != null) {
                                if(item.getNumber() == 1 || item.getCellContent().equals(item.getTimeSlot().toString())) {
                                    setText(item.getCellContent());
                                }
                            }

                            if (item.getColor() != null) {
                                setBackground(new Background(new BackgroundFill(item.getColor(), CornerRadii.EMPTY, EMPTY)));
                                if(item.getTooltip() != null) {
                                    setTooltip(item.getTooltip());
                                }
                                addContextMenu(item, this);
                            }
                            else{
                                //addContextMenuAdd(item, this);
                                setBackground(null);
                            }
                        }
                    }
                };

                return tableCell;
            }
        });
    }

    private void addContextMenu(CalendarCell item, TableCell<ReservationPerWeek, CalendarCell> tableCell) {
        final ContextMenu contextMenu = new ContextMenu();
        final MenuItem removeMenuItem = new MenuItem("Remove");
        final MenuItem editMenuItem = new MenuItem("Edit");
        removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BookingDAO bookingDAO = new DBBookingDAO();
                bookingDAO.deleteBooking(item.getBooking());
                initializeMyCalendarContent(currentStartDate, currentEndDate);
            }
        });
        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                initEditBookingLayout(item.getBooking());
                initializeMyCalendarContent(currentStartDate, currentEndDate);
            }
        });

        contextMenu.getItems().addAll(Arrays.asList(editMenuItem, removeMenuItem));



        tableCell.contextMenuProperty().bind(
                Bindings.when(tableCell.emptyProperty())
                        .then((ContextMenu)null)
                        .otherwise(contextMenu));

    }

/*    private void addContextMenuAdd(CalendarCell item, TableCell<ReservationPerWeek, CalendarCell> tableCell) {
        final ContextMenu contextMenuAdd = new ContextMenu();
        final MenuItem addMenuItem = new MenuItem("Add");
        addMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                initEditBookingLayout();
                initializeMyCalendarContent(currentStartDate, currentEndDate);
            }
        });
        contextMenuAdd.getItems().add(addMenuItem);
        tableCell.contextMenuProperty().bind(
                Bindings.when(tableCell.emptyProperty())
                        .then(contextMenuAdd)
                        .otherwise((ContextMenu)null));
    }*/


    private void initEditBookingLayout(Booking booking) {
/*        BookingController bc = new BookingController();
        bc.editBooking(booking);*/
        /*try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(rsvp.home.Main.class.getResource("../booking/view/BookingEditionPane.fxml"));
            Parent editBookingLayout = loader.load();
            Scene scene = new Scene(editBookingLayout, 600, 300);
            Stage secondaryStage = new Stage();
            secondaryStage.setScene(scene);
            //todo : command here
            secondaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void initializeMyCalendarContent(Date start, Date end) {
        calendarTableItems.clear();
        calendarTableItems.addAll(fillTableCells(start, end));

        myCalendarTable.setItems(calendarTableItems);
        myCalendarTable.getColumns().clear();
        myCalendarTable.getColumns().addAll(slots, monday, tuesday, wednesday, thursday, friday, saturday, sunday);
        init(slots);
        init(monday);
        init(tuesday);
        init(wednesday);
        init(thursday);
        init(friday);
        init(saturday);
        init(sunday);
    }

    private List<ReservationPerWeek> fillTableCells(Date start, Date end){
        BookingDAO bookingDAO = new DBBookingDAO();
        List<Booking> bookings = bookingDAO.getAllBookingsForCurrentUser();

        TimeSlotDAO timeSlotDAO = new TimeSlotDAO();
        List<TimeSlot> timeSlots = timeSlotDAO.getAll();
        Collections.sort(timeSlots);

        Map<TimeSlot, ReservationPerWeek> bookingItemsMap = new HashMap<>();
        List<ReservationPerWeek> result = new ArrayList<>();
        for (TimeSlot timeSlot : timeSlots){
            ReservationPerWeek row = new ReservationPerWeek(timeSlot);
            result.add(row);
            bookingItemsMap.put(timeSlot, row);
        }
        int[] i = {1};
        bookings.stream().filter(booking ->
                isInThePeriod(start, end, booking.getReservationDate())).
                forEach(booking -> {
                    Color color = getRandomColor();
                    i[0] = 1;
                    DayOfWeek dayOfWeek = booking.getReservationDate().toLocalDate().getDayOfWeek();
                    timeSlots.stream().filter(timeSlot -> isBetweenTimeSlots(booking.getFirstSlot(),
                            booking.getLastSlot(), timeSlot)).forEach(timeSlot -> {
                        ReservationPerWeek item = bookingItemsMap.get(timeSlot);
                        item.addColor(dayOfWeek, color);
                        item.addBooking(dayOfWeek, booking);
                        item.addNumber(dayOfWeek, i[0]);
                        i[0]++;
                    });
                });

        return result;
    }
    private boolean isInThePeriod(Date start, Date end, Date date){
        return !date.before(start) && !date.after(end);
    }

    private boolean isBetweenTimeSlots(TimeSlot start, TimeSlot end, TimeSlot timeSlot){
        if(start == null && end == null) return true;
        if(start == null) return (end.compareTo(timeSlot) == 1 || end.getId() == timeSlot.getId());
        if(end == null) return (start.compareTo(timeSlot) == -1 || start.getId() == timeSlot.getId());
        if(start.getId() == timeSlot.getId() || end.getId() == timeSlot.getId()) {
            return true;
        } else if (start.compareTo(timeSlot) == -1 && end.compareTo(timeSlot) == 1) {
            return true;
        }
        return false;
    }

    private Date getDate(DayOfWeek dayOfWeek, LocalDate currentDate) {
        DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();
        if(dayOfWeek.equals(currentDayOfWeek)){
            return Date.valueOf(currentDate);
        } else if(dayOfWeek.getValue() < currentDayOfWeek.getValue()) {
            return Date.valueOf(currentDate.minusDays(currentDayOfWeek.getValue() - dayOfWeek.getValue()));
        }
        return Date.valueOf(currentDate.plusDays(dayOfWeek.getValue() - currentDayOfWeek.getValue()));
    }

    public void handleShowPreviousWeek() {
        currentEndDate = Date.valueOf(currentStartDate.toLocalDate().minusDays(1));
        currentStartDate = Date.valueOf(currentStartDate.toLocalDate().minusDays(7));
        initializeMyCalendarContent(currentStartDate, currentEndDate);
        setDateRangeLabel();
    }

    public void handleShowNextWeek() {
        currentStartDate = Date.valueOf(currentEndDate.toLocalDate().plusDays(1));
        currentEndDate = Date.valueOf(currentEndDate.toLocalDate().plusDays(7));
        initializeMyCalendarContent(currentStartDate, currentEndDate);
        setDateRangeLabel();
    }

    private void setIcons() {
        ImageView previousView = new ImageView(new Image(getClass().getResourceAsStream("../view/images/previous.png")));
        previousView.setFitWidth(20);
        previousView.setFitHeight(20);
        previousButton.setGraphic(previousView);
        ImageView nextView = new ImageView(new Image(getClass().getResourceAsStream("../view/images/next.png")));
        nextView.setFitWidth(20);
        nextView.setFitHeight(20);
        nextButton.setGraphic(nextView);
        ImageView refreshView = new ImageView(new Image(getClass().getResourceAsStream("../view/images/reset.png")));
        refreshView.setFitWidth(20);
        refreshView.setFitHeight(20);
        refreshButton.setGraphic(refreshView);
    }

    private void setDateRangeLabel(){
        String formattedStartDate = new SimpleDateFormat("dd/MM").format(this.currentStartDate);
        String formattedEndDate = new SimpleDateFormat("dd/MM/yyyy").format(this.currentEndDate);
        dateRangeLabel.setText(formattedStartDate + " - "+ formattedEndDate);
    }

    private Color getRandomColor(){
        Random rand = new Random();
        double r = rand.nextFloat() / 2f + 0.5;
        double g = rand.nextFloat() / 2f + 0.5;
        double b = rand.nextFloat() / 2f + 0.5;
        return new Color(r, g, b, 1.0);
    }

    public void refresh(ActionEvent actionEvent) {
        initializeMyCalendarContent(currentStartDate, currentEndDate);
    }
}
