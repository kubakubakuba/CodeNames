package cz.cvut.fel.pjv.codenames.GUI;

import cz.cvut.fel.pjv.codenames.controller.GameController;
import javafx.application.Application;
import javafx.stage.Stage;

public class FieldOperativeLeaderView extends GameView {

    public FieldOperativeLeaderView(GameController controller) {
        super(controller);
        launch();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

    }
}
