package rsvp.home;


import javafx.application.Application;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import rsvp.common.persistence.HibernateUtils;
import rsvp.user.model.User;

import static rsvp.user.model.UserUtils.createUser;


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

    public static void main(String[] args) {
        launch(args);
    }
}
