package cz.cvut.fel.pjv.codenames.GUI;

import cz.cvut.fel.pjv.codenames.controller.GameController;
import cz.cvut.fel.pjv.codenames.model.Game;
import cz.cvut.fel.pjv.codenames.model.Player;
import javafx.application.Application;
import javafx.stage.Stage;

public class GameView extends Application {

    private Player.PlayerTeam team;
    private Player.PlayerRole role;

    private GameController localControl= null;

    public GameView(GameController controller)  {
        this.localControl = controller;
        this.team = localControl.getClient().getPlayer().getTeam();
        this.role = localControl.getClient().getPlayer().getRole();
        determineView(role);
    }


    public void determineView(Player.PlayerRole role)   {
        if (role == Player.PlayerRole.SPY_MASTER){
            SpymasterView view = new SpymasterView(localControl, new Stage());
        }
        if (role == Player.PlayerRole.FIELD_OPERATIVE){
            FieldOperativeView view = new FieldOperativeView(localControl, new Stage());
        }
        if (role == Player.PlayerRole.FIELD_OPERATIVE_LEADER){
            FieldOperativeLeaderView view = new FieldOperativeLeaderView(localControl, new Stage());
        }
    };

    @Override
    public void start(Stage primaryStage) {
        if(role == Player.PlayerRole.SPY_MASTER){
            viewS.start(primaryStage);
        }
        if(role == Player.PlayerRole.FIELD_OPERATIVE){
            viewF.start(primaryStage);
        }
        if(role == Player.PlayerRole.FIELD_OPERATIVE_LEADER){
            viewL.start(primaryStage);
        }
    }
}
