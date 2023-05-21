package cz.cvut.fel.pjv.codenames.server;

import cz.cvut.fel.pjv.codenames.model.Card;
import cz.cvut.fel.pjv.codenames.model.Game;
import cz.cvut.fel.pjv.codenames.model.Key;
import cz.cvut.fel.pjv.codenames.model.Player;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
public class ServerThread extends Thread {

    private final Socket socket;

    private final Server server;
    private final Logger LOGGER = Logger.getLogger(ServerThread.class.getName());
    CommandParser parser = new CommandParser();
    private boolean running = true;

    public ServerThread(Socket socket, Server server)  {
        this.server = server;
        this.socket = socket;
        this.running = true;
    }

    private void sendUpdates(String session){
        for(Socket s : server.getActiveSessions().get(session).getListeners().values()){
            PrintWriter playerWriter = null;
            try {
                playerWriter = new PrintWriter(s.getOutputStream(), true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            playerWriter.println("update;");
        }
        LOGGER.log(Level.INFO, "Sent lobby updates to all players in session " + session);
    }

    private void gameUpdates(String session){
        for(Socket s : server.getActiveSessions().get(session).getGameListeners().values()){
            PrintWriter playerWriter = null;
            try {
                playerWriter = new PrintWriter(s.getOutputStream(), true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            playerWriter.println("update;");
        }
        LOGGER.log(Level.INFO, "Sent game updates to all players in session " + session);
    }

    private void sendMessages(String session, String message){
        for(Socket s : server.getActiveSessions().get(session).getChatListeners().values()){
            PrintWriter playerWriter = null;
            try {
                playerWriter = new PrintWriter(s.getOutputStream(), true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            playerWriter.println("message;" + message.replace(";", ","));
        }
    }

    private void endGame(String session){
        for(Socket s : server.getActiveSessions().get(session).getGameListeners().values()){
            PrintWriter playerWriter = null;
            try {
                playerWriter = new PrintWriter(s.getOutputStream(), true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            playerWriter.println("end;");
        }
        LOGGER.log(Level.INFO, "Sent game end to all players in session " + session);
    }

    private boolean checkRoleAvailable(String playerId, String sessionId, Player.PlayerRole role){
        Session session = server.getActiveSessions().get(sessionId);
        HashMap<String, Player> players = session.getLobby().getListOfPlayers();

        if(players.get(playerId).getTeam() == Player.PlayerTeam.NONE){
            LOGGER.log(Level.INFO, "Player " + playerId + " tried to select role " + role + " but they are not on a team");
            return false;
        }

        if(role == Player.PlayerRole.FIELD_OPERATIVE || role == Player.PlayerRole.NONE){
            return true;
        }

        for(Map.Entry<String, Player> set : players.entrySet()){
            if(set.getValue().getID().equals(playerId)){
                continue; //skip self
            }
            if(set.getValue().getRole() == role && set.getValue().getTeam() == players.get(playerId).getTeam()){
                LOGGER.log(Level.INFO, "Player " + playerId + " tried to select role " + role + " but it was already taken by player " + set.getValue().getID());
                return false;
            }
        }

        return true;
    }

    public String serialize(Serializable object) {
        try {
            // Create a ByteArrayOutputStream to hold the serialized object
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // Create an ObjectOutputStream to write the object into the ByteArrayOutputStream
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            // Write the object to the ObjectOutputStream
            oos.writeObject(object);

            // Close the streams
            oos.close();

            // Convert the ByteArrayOutputStream to a base64 string
            byte[] bytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(bytes);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean compareTeamCard(Key.KeyType key, Player.PlayerTeam team){
        if(key == Key.KeyType.RED && team == Player.PlayerTeam.RED){
            return true;
        }
        return key == Key.KeyType.BLUE && team == Player.PlayerTeam.BLUE;
    }

    public void run(){
        try{
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            String text;
            //do{
                text = reader.readLine();
                if(text == null){
                    return;
                    //continue;
                }
                parser = new CommandParser(text);
                LOGGER.log(Level.INFO, "Received command: " + parser.getCommand() + " with arguments " + Arrays.toString(parser.getArguments()));

                if(parser.getCommand() == CommandParser.CommandType.UNKNOWN_COMMAND){
                    LOGGER.severe("Unknown command!");
                    writer.println("1arg;false");
                }

                if(parser.getCommand() == CommandParser.CommandType.LISTEN){
                    String session = parser.getArguments()[0];
                    String id = parser.getArguments()[1];
                    Session s = server.getActiveSessions().get(session);
                    LOGGER.log(Level.INFO, "Player " + parser.getArguments()[1] + " is listening to session " + session);

                    if(s.getLobby().getListOfPlayers().containsKey(id)){
                        s.addListener(socket, id);
                        writer.println("1arg;true"); //accept the listener
                    }
                    else{
                        writer.println("1arg;false"); //reject the listener
                    }
                }

                if(parser.getCommand() == CommandParser.CommandType.LISTEN_CHAT){
                    String session = parser.getArguments()[0];
                    String id = parser.getArguments()[1];
                    Session s = server.getActiveSessions().get(session);
                    LOGGER.log(Level.INFO, "Player " + parser.getArguments()[1] + " is listening to all chat messages in " + session);

                    if(s.getLobby().getListOfPlayers().containsKey(id)){
                        s.addChatListener(socket, id);
                        writer.println("1arg;true"); //accept the listener
                    }
                    else{
                        writer.println("1arg;false"); //reject the listener
                    }
                }

                if(parser.getCommand() == CommandParser.CommandType.LISTEN_GAME){
                    String session = parser.getArguments()[0];
                    String id = parser.getArguments()[1];
                    Session s = server.getActiveSessions().get(session);
                    LOGGER.log(Level.INFO, "Player " + parser.getArguments()[1] + " is listening to game updates in " + session);

                    if(s.getLobby().getListOfPlayers().containsKey(id)){
                        s.addGameListener(socket, id);
                        writer.println("1arg;true"); //accept the listener
                    }
                    else{
                        writer.println("1arg;false"); //accept the listener
                    }
                }

                if(parser.getCommand() == CommandParser.CommandType.SEND_MESSAGE){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];
                    String message = parser.getArguments()[2];

                    LOGGER.log(Level.INFO, "Player " + idSelf + " sent message: " + message + " to session " + idSession);

                    if(!(server.getActiveSessions().containsKey(idSession) && server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().containsKey(idSelf))){
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to send message to session " + idSession + " but they are not in that session");
                        writer.println("1arg;false");
                        return;
                    }

                    Session s = server.getActiveSessions().get(idSession);

                    for(Player p : s.getLobby().getListOfPlayers().values()){
                        if(p.getTeam() == s.getLobby().getListOfPlayers().get(idSelf).getTeam()){
                            Socket playerSocket = p.getSocket();
                            PrintWriter playerWriter = new PrintWriter(playerSocket.getOutputStream(), true);
                            playerWriter.println("message;" + message.replace(";", ","));
                        }
                    }

                    sendMessages(idSession, message);
                    writer.println("1arg;true");
                }

                if(parser.getCommand() == CommandParser.CommandType.MAKE_MOVE){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];
                    int yCoord = Integer.parseInt(parser.getArguments()[2]);
                    int xCoord = Integer.parseInt(parser.getArguments()[3]);

                    LOGGER.log(Level.INFO, "Field operative " + idSelf + " clicked on X: " + xCoord + " Y: " + yCoord + " at session: "+ idSession);

                    if(!(server.getActiveSessions().containsKey(idSession) && server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().containsKey(idSelf))){
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to make move in session " + idSession + " but they are not in that session");
                        writer.println("1arg;false");
                        return;
                    }

                    if(xCoord < 0 || xCoord > 4 || yCoord < 0 || yCoord > 4){
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to make move in session " + idSession + " but the coordinates were out of bounds");
                        writer.println("1arg;false");
                        return;
                    }

                    Session s = server.getActiveSessions().get(idSession);

                    if(s.getGame().getGameData().getLastPromptCardCount() <= 0){
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to make move in session " + idSession + " but there are no more moves left");
                        writer.println("1arg;false");
                        return;
                    }

                    if(!(s.getGame().getGameData().getCurrentTurnTeam() == s.getLobby().getListOfPlayers().get(idSelf).getTeam())){
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to make move in session " + idSession + " but it is not their turn");
                        writer.println("1arg;false");
                        return;
                    }

                    if(!(s.getGame().getGameData().getCurrentTurnRole() == Player.PlayerRole.FIELD_OPERATIVE || s.getGame().getGameData().getCurrentTurnRole() == Player.PlayerRole.FIELD_OPERATIVE_LEADER)){
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to make move in session " + idSession + " but they are not a field operative");
                        writer.println("1arg;false");
                        return;
                    }

                    ArrayList<ArrayList<Key.KeyType>> discovered = s.getGame().getGameData().getRevealedCardsBoard();

                    if(discovered.get(yCoord).get(xCoord) != Key.KeyType.EMPTY){
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to make move in session " + idSession + " but the card was already revealed");
                        writer.println("1arg;false");
                        return;
                    }

                    if (s.getGame().getGameData().hasGameEnded()){
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to make move in session " + idSession + " but the game has ended");
                        writer.println("1arg;false");
                        return;
                    }

                    Key.KeyType correct = s.getGame().getGameData().getBoard().getKey().getSolution().get(yCoord).get(xCoord);
                    s.getGame().getGameData().revealCardInRevealedCards(yCoord,xCoord,correct);

                    if(correct == Key.KeyType.ASSASSIN){
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to make move in session " + idSession + " but they revealed the assassin");
                        s.getGame().getGameData().setLastPromptCardCount(0);
                        sendMessages(idSession, "_____You found the assassin! The game ends!_____");
                        s.getGame().getGameData().setGameEnded(true);
                        s.getGame().getGameData().setWinner(s.getGame().getGameData().getCurrentTurnTeam() == Player.PlayerTeam.RED ? Player.PlayerTeam.BLUE : Player.PlayerTeam.RED);
                        endGame(idSession);
                        writer.println("1arg;true");
                    }

                    if(compareTeamCard(correct, s.getGame().getGameData().getCurrentTurnTeam())){
                        if(s.getGame().getGameData().getLastPromptCardCount() == 1){
                            s.getGame().getGameData().setLastPromptCardCount(0);
                            s.getGame().getGameData().setCurrentTurnTeam(s.getGame().getGameData().getCurrentTurnTeam() == Player.PlayerTeam.RED ? Player.PlayerTeam.BLUE : Player.PlayerTeam.RED);
                            s.getGame().getGameData().setCurrentTurnRole(Player.PlayerRole.SPY_MASTER);
                            sendMessages(idSession, "_____Correct guess! Other team plays now._____");
                        }
                        else{
                            s.getGame().getGameData().setLastPromptCardCount(s.getGame().getGameData().getLastPromptCardCount() - 1);
                            s.getGame().getGameData().setCurrentTurnRole(Player.PlayerRole.FIELD_OPERATIVE_LEADER);
                            sendMessages(idSession, "_____Correct guess!_____");
                        }

                        if(s.getGame().getColorCardsLeft(Key.KeyType.RED) == 0 || s.getGame().getColorCardsLeft(Key.KeyType.BLUE) == 0){
                            Player.PlayerTeam winner = s.getGame().getColorCardsLeft(Key.KeyType.RED) == 0 ? Player.PlayerTeam.RED : Player.PlayerTeam.BLUE;
                            LOGGER.log(Level.INFO, "Player " + idSelf + " tried to make move in session " + idSession + " and they revealed the last card");
                            s.getGame().getGameData().setLastPromptCardCount(0);
                            sendMessages(idSession, "_____You found the last card! The game ends!_____");
                            sendMessages(idSession, "_____The winner is the " + winner + " team!_____");
                            s.getGame().getGameData().setGameEnded(true);
                            s.getGame().getGameData().setWinner(winner);
                            writer.println("1arg;true");

                            gameUpdates(idSession);
                            endGame(idSession);
                            return;
                        }
                    }
                    else{
                        s.getGame().getGameData().setLastPromptCardCount(0);
                        s.getGame().getGameData().setCurrentTurnTeam(s.getGame().getGameData().getCurrentTurnTeam() == Player.PlayerTeam.RED ? Player.PlayerTeam.BLUE : Player.PlayerTeam.RED);
                        s.getGame().getGameData().setCurrentTurnRole(Player.PlayerRole.SPY_MASTER);
                        sendMessages(idSession, "_____Wrong guess! Other team plays now._____");
                    }

                    writer.println("1arg;true");

                    gameUpdates(idSession);

                    LOGGER.log(Level.INFO, "Player " + idSelf + " made move in session " + idSession + " and it was successfully processed");
                }

                if (parser.getCommand() == CommandParser.CommandType.COMMIT_PROMPT){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];
                    String prompt = parser.getArguments()[2];
                    int count = Integer.parseInt(parser.getArguments()[3]);

                    if(!(server.getActiveSessions().containsKey(idSession) && server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().containsKey(idSelf))){
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to commit prompt in session " + idSession + " but the session or player does not exist");
                        writer.println("1arg;false");
                        return;
                    }

                    Session s = server.getActiveSessions().get(idSession);

                    LOGGER.log(Level.INFO,  s.getGame().getListOfPlayers().get(idSelf).getTeam() +  " prompted: " + prompt + " with count: " + count + " at session: "+ idSession);

                    if(count < 0 || count > 9){
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to commit prompt in session " + idSession + " but the count is invalid");
                        writer.println("1arg;false");
                        return;
                    }

                    if(s.getGame().getListOfPlayers().get(idSelf).getRole() != Player.PlayerRole.SPY_MASTER){
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to commit prompt in session " + idSession + " but they are not a spy master");
                        writer.println("1arg;false");
                        return;
                    }

                    if(s.getGame().getListOfPlayers().get(idSelf).getTeam() != s.getGame().getGameData().getCurrentTurnTeam()){
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to commit prompt in session " + idSession + " but they are not on the current turn team");
                        writer.println("1arg;false");
                        return;
                    }

                    if(s.getGame().getGameData().getCurrentTurnRole() != Player.PlayerRole.SPY_MASTER){
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to commit prompt in session " + idSession + " but it is not their turn");
                        writer.println("1arg;false");
                        return;
                    }

                    s.getGame().getGameData().setLastPromptCardCount(count);
                    s.getGame().getGameData().setLastPromptText(prompt);
                    s.getGame().getGameData().setCurrentTurnRole(Player.PlayerRole.FIELD_OPERATIVE);

                    writer.println("1arg;true;");

                    sendMessages(idSession, "_____Leader prompt: " + prompt + " with count: " + count + "_____");

                    gameUpdates(idSession);

                    LOGGER.log(Level.INFO, "Player " + idSelf + " committed prompt in session " + idSession + " and it was successfully processed");
                }

                if(parser.getCommand() == CommandParser.CommandType.CREATE_SESSION){
                    Session session = new Session(parser.getArguments()[0]);
                    server.addSession(session);

                    LOGGER.log(Level.INFO, "Session " + session.getSessionId() + " created!");

                    writer.println("1arg;" + session.getSessionId());
                }

                if(parser.getCommand() == CommandParser.CommandType.CONNECT){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];

                    String response = "1arg;";
                    if(!server.getActiveSessions().containsKey(idSession)) {
                        response = "1arg;sessioninvalid;";
                        writer.println(response);
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to connect to session " + idSession + " but the session does not exist");
                        return;
                    }

                    Session s = server.getActiveSessions().get(idSession);

                    if(s.getLobby().getListOfPlayers().containsKey(idSelf)){
                        response = "1arg;nametaken;";
                        writer.println(response);
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to connect to session " + idSession + " but the player already exists");
                        return;
                    }

                    s.getLobby().getListOfIds().add(idSelf);
                    s.getLobby().getListOfPlayers().put(idSelf, new Player(idSelf, this.socket));
                    response = "1arg;" + idSession;

                    writer.println(response);

                    sendUpdates(idSession);

                    LOGGER.log(Level.INFO, "Player " + idSelf + " connected to session " + idSession);
                }

                if(parser.getCommand() == CommandParser.CommandType.GET_HOST_ID){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];

                    LOGGER.log(Level.INFO, "Player " + idSelf + " requested host id of session " + idSession);

                    if(!(server.getActiveSessions().containsKey(idSession) && server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().containsKey(idSelf))){
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to get host id of session " + idSession + " but the session or player does not exist");
                        writer.println("1arg;false");
                        return;
                    }

                    Session s = server.getActiveSessions().get(idSession);
                    String response = "1arg;" + s.getHostId();

                    writer.println(response);

                    LOGGER.log(Level.INFO, "Player " + idSelf + " got host id of session " + idSession);
                }


                if(parser.getCommand() == CommandParser.CommandType.CHOOSE_ROLE){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];

                    if(!(server.getActiveSessions().containsKey(idSession) && server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().containsKey(idSelf))){
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to choose role in session " + idSession + " but the session or player does not exist");
                        writer.println("1arg;false");
                        return;
                    }

                    Player.PlayerRole role;
                    if(parser.getArguments()[2].equals("SPY_MASTER")){
                        role = Player.PlayerRole.SPY_MASTER;
                    }
                    else if(parser.getArguments()[2].equals("FIELD_OPERATIVE")){
                        role = Player.PlayerRole.FIELD_OPERATIVE;
                    }
                    else if(parser.getArguments()[2].equals("FIELD_OPERATIVE_LEADER")){
                        role = Player.PlayerRole.FIELD_OPERATIVE_LEADER;
                    }
                    else{
                        role = Player.PlayerRole.NONE;
                    }

                    boolean role_available = checkRoleAvailable(idSelf, idSession, role);
                    String response = "1arg;false";
                    if(role_available){
                        server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().get(idSelf).setRole(role);
                        response = "1arg;true";
                    }

                    writer.println(response);

                    sendUpdates(idSession);

                    LOGGER.log(Level.INFO, "Player " + idSelf + " requested to choose role " + role + " at session " + idSession + ", granted: " + role_available);
                }

                if(parser.getCommand() == CommandParser.CommandType.CHOOSE_TEAM){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];
                    String team = parser.getArguments()[2];

                    String response = "1arg;false";
                    if(server.getActiveSessions().containsKey(idSession) && server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().containsKey(idSelf)){
                        Session s = server.getActiveSessions().get(idSession);
                        s.getLobby().getListOfPlayers().get(idSelf).setRole(Player.PlayerRole.NONE); //we need to reset the role
                        s.getLobby().getListOfPlayers().get(idSelf).setTeam(Player.PlayerTeam.valueOf(team));
                        response = "1arg;true";
                    }

                    writer.println(response);

                    sendUpdates(idSession);

                    LOGGER.log(Level.INFO, "Player " + idSelf + " requested to choose team " + team + " at session " + idSession + ", granted: " + response.split(";")[1]);
                }


                if(parser.getCommand() == CommandParser.CommandType.GET_SESSIONS){
                    String idSelf = parser.getArguments()[0];

                    StringBuilder result = new StringBuilder("sessionlist;");
                    for(Session s : server.getActiveSessions().values()){
                        result.append(s.getSessionId()).append(";");
                    }

                    writer.println(result);
                    LOGGER.log(Level.INFO, "Player "+ idSelf +" requested session list, result: " + result);
                }

                if(parser.getCommand() == CommandParser.CommandType.GET_PLAYER_COUNT){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];

                    if(!(server.getActiveSessions().containsKey(idSession) && server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().containsKey(idSelf))){
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to get player count of session " + idSession + " but the session or player does not exist");
                        writer.println("1arg;false");
                        return;
                    }
                    Session s = server.getActiveSessions().get(idSession);
                    HashMap<String,Player> players = s.getLobby().getListOfPlayers();

                    int[] RBNplayers = {0,0,0};

                    for (Map.Entry<String, Player> entry : players.entrySet())    {
                        Player p = entry.getValue();
                        if (p.getTeam() == Player.PlayerTeam.NONE)
                            RBNplayers[2]++;
                        if(p.getTeam() == Player.PlayerTeam.BLUE)
                            RBNplayers[1]++;
                        if(p.getTeam() == Player.PlayerTeam.RED)
                            RBNplayers[0]++;
                    }
                    String response = "playercount;"+ RBNplayers[0]+ ";"+RBNplayers[1] +";"+ RBNplayers[2];
                    writer.println(response);
                    LOGGER.log(Level.INFO, "Player "+ idSelf + " requested player count, result: " + response);
                }

                if(parser.getCommand() == CommandParser.CommandType.GET_PLAYER_ROLES){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];

                    if(!(server.getActiveSessions().containsKey(idSession) && server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().containsKey(idSelf))){
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to get player roles of session " + idSession + " but the session or player does not exist");
                        writer.println("1arg;false");
                        return;
                    }

                    Session s = server.getActiveSessions().get(idSession);
                    HashMap<String,Player> players = s.getLobby().getListOfPlayers();

                    int[] pRoles = {0, 0, 0, 0, 0, 0, 0}; //red roles x y z, blue roles x y z, none role 7 total

                    for (Map.Entry<String, Player> entry : players.entrySet())    {
                        Player p = entry.getValue();
                        if(p.getRole() == Player.PlayerRole.SPY_MASTER && p.getTeam() == Player.PlayerTeam.RED){
                            pRoles[0]++;
                        }

                        if(p.getRole() == Player.PlayerRole.FIELD_OPERATIVE && p.getTeam() == Player.PlayerTeam.RED) {
                            pRoles[1]++;
                        }

                        if(p.getRole() == Player.PlayerRole.FIELD_OPERATIVE_LEADER && p.getTeam() == Player.PlayerTeam.RED){
                            pRoles[2]++;
                        }

                        if(p.getRole() == Player.PlayerRole.SPY_MASTER && p.getTeam() == Player.PlayerTeam.BLUE){
                            pRoles[3]++;
                        }

                        if(p.getRole() == Player.PlayerRole.FIELD_OPERATIVE && p.getTeam() == Player.PlayerTeam.BLUE){
                            pRoles[4]++;
                        }

                        if(p.getRole() == Player.PlayerRole.FIELD_OPERATIVE_LEADER && p.getTeam() == Player.PlayerTeam.BLUE){
                            pRoles[5]++;
                        }

                        if (p.getRole() == Player.PlayerRole.NONE) {
                            pRoles[6]++;
                        }

                    }

                    String response = "playercountroles;" + pRoles[0] + ";" + pRoles[1] + ";" + pRoles[2] + ";" + pRoles[3] + ";" + pRoles[4] + ";" + pRoles[5] + ";" + pRoles[6] + ";";
                    writer.println(response);

                    LOGGER.log(Level.INFO, "Player "+ idSelf + " requested player count, result: " + response);
                }

                if (parser.getCommand() == CommandParser.CommandType.GET_LOBBY_IDS) {
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];

                    if(!(server.getActiveSessions().containsKey(idSession) && server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().containsKey(idSelf))){
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to get lobby ids of session " + idSession + " but the session or player does not exist");
                        writer.println("1arg;false");
                        return;
                    }
                    Session s = server.getActiveSessions().get(idSession);
                    HashMap<String,Player> players = s.getLobby().getListOfPlayers();

                    StringBuilder response = new StringBuilder("idlist;");
                    for (Map.Entry<String, Player> entry : players.entrySet())    {
                        String id = entry.getKey();
                        response.append(id).append(";");
                    }
                    writer.println(response);
                    LOGGER.log(Level.INFO, "Player "+ idSelf + " requested lobby ids, result: " + response);
                }

                if(parser.getCommand() == CommandParser.CommandType.DISCONNECT_PLAYER){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];

                    if(!(server.getActiveSessions().containsKey(idSession) &&
                         server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().containsKey(idSelf)))
                    {
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to disconnect from session " +
                                  idSession + " but the session or player does not exist");
                        writer.println("1arg;false");
                        return;
                    }

                    Session s = server.getActiveSessions().get(idSession);
                    s.getLobby().getListOfIds().remove(idSelf);
                    s.getLobby().getListOfPlayers().remove(idSelf);

                    writer.println("1arg;true");

                    OutputStream endOutputStream = s.getListeners().get(idSelf).getOutputStream();
                    PrintWriter endPrintWriter = new PrintWriter(endOutputStream, true);
                    endPrintWriter.println("endlisten;");

                    s.getListeners().remove(idSelf);

                    sendUpdates(idSession);

                    writer.println("1arg;true");

                    if(s.getHostId().equals(idSelf)){
                        LOGGER.log(Level.INFO, "Player " + idSelf + " was the host of session " + idSession + " and disconnected");
                        endGame(idSession);
                        server.getActiveSessions().remove(idSession);
                        LOGGER.log(Level.INFO, "Session wit id " + idSession + " has been terminated");
                        return;
                    }

                    if(s.getLobby().getListOfPlayers().size() < 4 && s.getLobby().getListOfPlayers().size() > 1){
                        LOGGER.log(Level.INFO, "Player " + idSelf + " disconnected from session " + idSession + " and there are not enough players to continue");
                        endGame(idSession);

                        //server.getActiveSessions().remove(idSession);
                        //TODO: handle unexpected disconnects
                        return;
                    }

                    if (s.getLobby().getListOfPlayers().size() == 1) {
                        LOGGER.log(Level.INFO, "Player " + idSelf + " disconnected from session " + idSession + " and there are not enough players to continue, session will be deleted");
                        endGame(idSession);
                        server.getActiveSessions().remove(idSession);
                    }

                    LOGGER.log(Level.INFO, "Player " + idSelf + " requested to disconnect from session " + idSession + " and was successful");
                }

                if(parser.getCommand() == CommandParser.CommandType.START_GAME){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];
                    String deckPath = parser.getArguments()[2];

                    if(!(server.getActiveSessions().containsKey(idSession) && server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().containsKey(idSelf))) {
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to start game in session " + idSession + " but the session or player does not exist");
                        writer.println("1arg;false");
                        return;
                    }

                    Session s = server.getActiveSessions().get(idSession);
                    if(!s.getHostId().equals(idSelf)){
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to start game in session " + idSession + " but is not the host");
                        writer.println("1arg;false");
                        return;
                    }

                    if(s.getLobby().getListOfPlayers().size() < 4){
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to start game in session " + idSession + " but there are not enough players");
                        writer.println("1arg;false");
                        return;
                    }

                    for(Player p : s.getLobby().getListOfPlayers().values()){
                        if(p.getRole() == Player.PlayerRole.NONE){
                            LOGGER.log(Level.INFO, "Player " + idSelf + " tried to start game in session " + idSession + " but there are players without roles");
                            writer.println("1arg;false");
                            return;
                        }
                    }

                    //TODO: pass the deck path from host selected file
                    s.startNewGame(deckPath);

                    for(Socket ss : s.getListeners().values()){
                        PrintWriter playerWriter = null;
                        try {
                            playerWriter = new PrintWriter(ss.getOutputStream(), true);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        playerWriter.println("startgame;");
                    }

                    writer.println("1arg;true");

                    sendMessages(idSession, "_____THE GAME HAS BEEN STARTED!_____");

                    LOGGER.log(Level.INFO, "Player " + idSelf + " started game in session " + idSession);
                }

                if(parser.getCommand() == CommandParser.CommandType.LOAD_GAME) {
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];
                    String gameData = parser.getArguments()[2];

                    if(!(server.getActiveSessions().containsKey(idSession) && server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().containsKey(idSelf))) {
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to load game in session " + idSession + " but the session or player does not exist");
                        writer.println("1arg;false");
                        return;
                    }

                    Session s = server.getActiveSessions().get(idSession);
                    if(!s.getHostId().equals(idSelf)){
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to load game in session " + idSession + " but is not the host");
                        writer.println("1arg;false");
                        return;
                    }

                    if(s.getLobby().getListOfPlayers().size() < 4){
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to load game in session " + idSession + " but there are not enough players");
                        writer.println("1arg;false");
                        return;
                    }

                    for(Player p : s.getLobby().getListOfPlayers().values()){
                        if(p.getRole() == Player.PlayerRole.NONE){
                            LOGGER.log(Level.INFO, "Player " + idSelf + " tried to load game in session " + idSession + " but there are players without roles");
                            writer.println("1arg;false");
                            return;
                        }
                    }

                    //TODO: pass the deck path from host selected file
                    s.loadGame(gameData);
                    for(Socket ss : s.getListeners().values()){
                        PrintWriter playerWriter = null;
                        try {
                            playerWriter = new PrintWriter(ss.getOutputStream(), true);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        playerWriter.println("startgame;");
                    }

                    writer.println("1arg;true");

                    sendMessages(idSession, "_____THE GAME HAS BEEN LOADED FROM SAVE FILE!_____");
                    //sendUpdates(idSession);

                    for(Socket ss : s.getGameListeners().values()){
                        PrintWriter playerWriter = null;
                        try {
                            playerWriter = new PrintWriter(ss.getOutputStream(), true);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        playerWriter.println("startgame;");
                    }

                    LOGGER.log(Level.INFO, "Player " + idSelf + " loaded game in session " + idSession);
                }

                if(parser.getCommand() == CommandParser.CommandType.GET_GAME_DATA){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];

                    if(!(server.getActiveSessions().containsKey(idSession) && server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().containsKey(idSelf))) {
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to get game data in session " + idSession + " but the session or player does not exist");
                        writer.println("1arg;false");
                        return;
                    }

                    Session s = server.getActiveSessions().get(idSession);
                    if(s.getGame() == null){
                        LOGGER.log(Level.INFO, "Player " + idSelf + " tried to get game data in session " + idSession + " but the game has not been started");
                        writer.println("1arg;false");
                        return;
                    }

                    String gameData = serialize(s.getGame().getGameData());

                    String response = "gamedata;" + gameData;
                    writer.println(response);

                    LOGGER.log(Level.INFO, "Player " + idSelf + " got game data in session " + idSession);
                }

//            } while(running);
            //LOGGER.log(Level.FINE, "Server thread terminated");
            //writer.println("Stopping the server");
            //socket.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
