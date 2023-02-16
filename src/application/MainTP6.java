package application;

import vue.ControllerRandom;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainTP6 extends Application {

    @Override
    public void start(Stage primaryStage) {

        try {
            primaryStage.setTitle("ANWAR_GOUPIL_KOUTOUAN");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainTP6.class.getResource("vue/Principal.fxml"));
            BorderPane root = loader.load();
            ControllerRandom rootController = loader.getController();

            FXMLLoader l = new FXMLLoader();
            l.setLocation(MainTP6.class.getResource("vue/VueTP6.fxml"));
            BorderPane grille = l.load();

            rootController.setGrilleController(l.getController());

            root.setCenter(grille);

            Scene scene = new Scene(root);


            final KeyCombination comb = new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN);
            scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
                if (comb.match(event))
                    System.exit(0);
            });

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
