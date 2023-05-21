package cz.cvut.fel.pjv.codenames.controller;

import cz.cvut.fel.pjv.codenames.GUI.GameView;
import cz.cvut.fel.pjv.codenames.model.*;
import cz.cvut.fel.pjv.codenames.server.AnswerParser;
import cz.cvut.fel.pjv.codenames.server.ServerThread;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameController {

    private Client localClient;

    //for all (tracking revealed cards)
    private ArrayList<ArrayList<Key.KeyType>> revealedCardsBoard;

    private Key gamekey;

    private Deck gameDeck;

    private String currentTurnText;

    private String currentPromptText;
    private int currentPromptCardCount;

    private ImageArray redCards;
    private ImageArray blueCards;
    private ImageArray neutralCards;
    private final static Logger LOGGER = Logger.getLogger(GameController.class.getName());

    public ArrayList<ArrayList<Key.KeyType>> getRevealedCardsBoard() {return revealedCardsBoard;}

    public String getCurrentTurnText() {return currentTurnText;}
    public String getCurrentPromptText() {return currentPromptText;}
    public int getCurrentPromptCardCount() {return currentPromptCardCount;}

    private GameView gameView;
    private String serverIP = "localhost";
    private int serverPort = 1313;

    private ChatController chatController;

    private GameListener gameListen;

    private Player.PlayerRole currentTurnRole;
    private Player.PlayerTeam currentTurnTeam;

    public Client getClient() {
        return localClient;
    }

    public GameController(Client client, ChatController chatController){
        this.localClient = client;
        serverIP = localClient.getServerIP();
        serverPort = localClient.getServerPort();
        this.chatController = chatController;
        this.gameView = new GameView(this);
        this.gameListen = new GameListener(gameView, this);
        loadImageFiles();
    }

    public void displayGameWindow(){
        gameView.start(new Stage());

        System.out.println("starting game listener");
        Thread gameListenerThread = new Thread(gameListen);

        gameListenerThread.start();
    }

    public Key getKey() {return gamekey;}
    public Deck getDeck() {return gameDeck;}

    
    //checks if number of Cards doesn't exceed 9 or is less than 1
    public boolean commitPrompt(String promptText, int cardCount){

        if (cardCount > 9 || cardCount < 1) {
            return false;
        }

        String answer = sendCommand("commitprompt;" + this.getClient().getId()+ ";"
                + this.getClient().getSessionId().toString() + ";"
                + promptText + ";"
                + cardCount + ";");
        AnswerParser answerParser = new AnswerParser(answer);
        return answerParser.getArguments()[0].equals("true");
    }

    public void getGameData(){
        String answer = sendCommand("getgamedata;" + this.getClient().getId()+ ";"
                                                + this.getClient().getSessionId().toString() + ";");
        AnswerParser answerParser = new AnswerParser(answer);
        String dataString = answerParser.getArguments()[0];
        byte[] decodedGameData = Base64.getDecoder().decode(dataString);
        GameData gameData = null;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(decodedGameData);

            ObjectInputStream ois = new ObjectInputStream(bais);

            gameData = (GameData)ois.readObject();

            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (gameData != null) {
            update(gameData);
        }
        else{
            System.out.println("Game data is null");
        }
    }

    private String sendCommand(String command) {
        String serverAnswer = "1arg;null";

        try {
            Socket sock = new Socket(localClient.getServerIP(), localClient.getServerPort());
            OutputStream output = sock.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            writer.println(command);

            InputStream input = sock.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            serverAnswer = reader.readLine();
            return serverAnswer;

        } catch (Exception e) {
            System.out.println(e);
        }

        return serverAnswer;
    }

    //checks if Card hasn't been chosen already
    public boolean makeChoice(int x, int y){
        getGameData();

        if (x < 0 || y < 0 || x > 4 || y > 4) {
            return false;
        }

        String answer = sendCommand("makemove;" + this.getClient().getId()+ ";"
                + this.getClient().getSessionId().toString() + ";" + x + ";" + y + ";");

        AnswerParser answerParser = new AnswerParser(answer);
        return answerParser.getArguments()[0].equals("true");
    }

    public void disconnect() {
        sendCommand("disconnect;" + this.getClient().getId()+ ";"
                + this.getClient().getSessionId().toString() + ";");
    }
    //once someone disconnects it should save the game and close the game for all
    //-> will be implemented in gameListener
    
    public void saveGame(File file){
        String answer = sendCommand("getgamedata;" + this.getClient().getId()+ ";"
                + this.getClient().getSessionId().toString() + ";");
        AnswerParser answerParser = new AnswerParser(answer);
        String dataString = answerParser.getArguments()[0];

        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(dataString);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger("Cannon save file!").log(Level.SEVERE, null, ex);
        }
    }
        //TODO: implement getCurrentTurn loads teamcolor and role of current turn to currentTurnText

    public void update(GameData data) {
        //init variable object from data
        String team = (data.getCurrentTurnTeam() == Player.PlayerTeam.RED) ? "RED" : "BLUE";
        String role = (data.getCurrentTurnRole() == Player.PlayerRole.SPY_MASTER) ? "SPYMASTER" : "FIELD OPERATIVES";
        this.currentTurnText = team + " " + role;
        this.gameDeck = data.getBoard().getDeck();
        this.gamekey = data.getBoard().getKey();
        this.revealedCardsBoard = data.getRevealedCardsBoard();
        this.currentPromptText = data.getLastPromptText();
        this.currentPromptCardCount = data.getLastPromptCardCount();
        this.currentTurnRole = data.getCurrentTurnRole();
        this.currentTurnTeam = data.getCurrentTurnTeam();
    }

    public int[] getChangedTileIdx(ArrayList<ArrayList<Key.KeyType>> old, ArrayList<ArrayList<Key.KeyType>> n) {
        int[] idxs = {-1, -1};
        for (int i = 0; i <5 ; i++){
            for(int j = 0; j < 5; j++){
                if (old.get(i).get(j) != n.get(i).get(j)){
                    idxs[0] = i;
                    idxs[1] = j;
                    return idxs;
                }
            }
        }
        return idxs;
    }

    public ChatController getChatController() {
        return chatController;
    }

    public GameView getGameView() {
        return gameView;
    }

    private void loadImageFiles(){
        this.redCards = new ImageArray("file:src/main/resources/cz/cvut/fel/pjv/codenames/cards/card_red_1.png");
        for(int i = 2; i < 9; i++){
            redCards.addImage("file:src/main/resources/cz/cvut/fel/pjv/codenames/cards/card_red_" + i + ".png");
        }
        this.blueCards = new ImageArray("file:src/main/resources/cz/cvut/fel/pjv/codenames/cards/card_blue_1.png");
        for(int i = 2; i < 10; i++){
            blueCards.addImage("file:src/main/resources/cz/cvut/fel/pjv/codenames/cards/card_blue_" + i + ".png");
        }
        this.neutralCards = new ImageArray("file:src/main/resources/cz/cvut/fel/pjv/codenames/cards/card_civ_1.png");
        for(int i = 2; i < 7; i++){
            neutralCards.addImage("file:src/main/resources/cz/cvut/fel/pjv/codenames/cards/card_civ_" + i + ".png");
        }

        redCards.shuffle();
        blueCards.shuffle();
        neutralCards.shuffle();
    }

    public String getImage(Key.KeyType key){
        String imgpath = "";
        if (key == Key.KeyType.RED) {
            imgpath = redCards.getNext();
        }
        if (key == Key.KeyType.BLUE) {
            imgpath = blueCards.getNext();
        }
        if (key == Key.KeyType.CIVILIAN) {
            imgpath = neutralCards.getNext();

        }
        if (key == Key.KeyType.ASSASSIN) {
            imgpath = "file:src/main/resources/cz/cvut/fel/pjv/codenames/cards/card_black.png";
        }
        return imgpath;
    }
}


