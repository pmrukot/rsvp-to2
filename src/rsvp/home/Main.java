package rsvp.home;


import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application{
    private Stage primaryStage;
    private AppController appController;

    public Main() {}

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("RSVP");

        this.appController = new AppController(primaryStage);
        this.appController.initRootLayout();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
