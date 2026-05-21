package dk.sdu.mmmi.cbse.main;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        Game game = context.getBean(Game.class);
        game.start(primaryStage);
        game.render();
    }

    public static void main(String[] args) {
        launch(args);
    }
}