package cz.cvut.fel.pjv.codenames.GUI;

import cz.cvut.fel.pjv.codenames.controller.GameController;
import cz.cvut.fel.pjv.codenames.controller.GameListener;
import cz.cvut.fel.pjv.codenames.controller.LobbyListener;
import cz.cvut.fel.pjv.codenames.model.*;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GameView extends Application {

    private Player.PlayerTeam team;
    private Player.PlayerRole role;
    private GameController localControl= null;
    private SpymasterView viewS =null;
    private FieldOperativeView viewF = null ;
    private  FieldOperativeLeaderView viewL = null;

    public SpymasterView getViewS() {return viewS;}
    public FieldOperativeView getViewF() {return viewF;}
    public FieldOperativeLeaderView getViewL() {return viewL;}

    /**
     * Constructor
     * @param controller the game controller
     */
    public GameView(GameController controller)  {
        this.localControl = controller;
        this.team = localControl.getClient().getPlayer().getTeam();
        this.role = localControl.getClient().getPlayer().getRole();

        //Thread serverListenerThread = new Thread(new GameListener(this ,localControl));
        //serverListenerThread.start();
        determineView(role);
    }

    /**
     * Determines which view to display based on the role of the player
     * @param role role of the player
     */
    public void determineView(Player.PlayerRole role)   {
        if (role == Player.PlayerRole.SPY_MASTER){
            viewS = new SpymasterView(localControl);
        }
        if (role == Player.PlayerRole.FIELD_OPERATIVE){
            viewF = new FieldOperativeView(localControl);
        }
        if (role == Player.PlayerRole.FIELD_OPERATIVE_LEADER){
            viewL = new FieldOperativeLeaderView(localControl);
        }
    };

    /**
     * Updates the view based on the role of the player
     */
    public void update(){
        if (role == Player.PlayerRole.SPY_MASTER){
            viewS.update();
        }
        if (role == Player.PlayerRole.FIELD_OPERATIVE){
            viewF.update();
        }
        if (role == Player.PlayerRole.FIELD_OPERATIVE_LEADER){
            viewL.update();
        }
    }

    /**
     * Starts the view based on the role of the player
     * @param primaryStage stage to be displayed
     */
    @Override
    public void start(Stage primaryStage) {
        if(role == Player.PlayerRole.SPY_MASTER){
            localControl.getChatController().setChatDisable();
            viewS.start(primaryStage);
        }
        if(role == Player.PlayerRole.FIELD_OPERATIVE){
            viewF.start(primaryStage);
        }
        if(role == Player.PlayerRole.FIELD_OPERATIVE_LEADER){
            viewL.start(primaryStage);
        }
    }

    /**
     * Ends the view based on the role of the player
     */
    public void gameEnd(){
        if(role == Player.PlayerRole.SPY_MASTER){
            viewS.gameEnd();
        }
        if(role == Player.PlayerRole.FIELD_OPERATIVE){
            viewF.gameEnd();
        }
        if(role == Player.PlayerRole.FIELD_OPERATIVE_LEADER){
            viewL.gameEnd();
        }
        localControl.getChatController().setChatEnable();
    }

}
