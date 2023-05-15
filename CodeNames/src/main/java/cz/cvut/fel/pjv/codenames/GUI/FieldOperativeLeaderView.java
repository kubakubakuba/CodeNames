package cz.cvut.fel.pjv.codenames.GUI;

import cz.cvut.fel.pjv.codenames.controller.GameController;
import javafx.application.Application;
import javafx.stage.Stage;

public class FieldOperativeLeaderView extends GameView {

    private GameController localControl;
    public FieldOperativeLeaderView(GameController controller, Stage stage) {
        super(controller);
        this.localControl = controller;
        start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage gameStage) {


    }
}
