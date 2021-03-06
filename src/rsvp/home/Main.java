package rsvp.home;

import javafx.application.Application;
import javafx.stage.Stage;
import rsvp.common.persistence.HibernateUtils;
import rsvp.user.DAO.DBUserDAO;
import rsvp.user.model.User;

public class Main extends Application{
    private Stage primaryStage;
    private AppController appController;

    public Main() {}

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("RSVP");

        this.appController = new AppController(primaryStage);
        this.appController.initLoginLayout();
    }

    @Override
    public void stop() {
        HibernateUtils.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
