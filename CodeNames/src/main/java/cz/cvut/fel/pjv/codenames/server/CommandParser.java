package cz.cvut.fel.pjv.codenames.server;

import java.util.Arrays;

public class CommandParser {
    public enum CommandType{
        EMPTY,
        START_SERVER,
        TERMINATE_SERVER,
        CONNECT,
        DISCONNECT_PLAYER,
        MAKE_MOVE,
        SEND_MESSAGE,
        COMMIT_PROMPT,
        CREATE_SESSION,
        GET_HOST_ID,
        GET_LOBBY_IDS,
        GET_PLAYER_COUNT,
        CHOOSE_ROLE,
        CHOOSE_TEAM,
        GET_SESSIONS,
        GET_PLAYER_ROLES,
        LISTEN,
        START_GAME,
        GET_GAME_DATA,
        LISTEN_CHAT,
        LISTEN_GAME,
        LOAD_GAME,
        UNKNOWN_COMMAND
    }

    private final CommandType command;
    private final String[] arguments;

    public CommandParser(){
        command = CommandType.EMPTY;
        arguments = null;
    }

    public CommandParser(String command){
        command = command;
        String[] parts = command.split(";");

        System.out.println(command);
        //System.out.println(parts);

        switch (parts[0].toLowerCase()) {
            case "startserver" -> this.command = CommandType.START_SERVER;
            case "terminateserver" -> this.command = CommandType.TERMINATE_SERVER;
            case "connect" -> this.command = CommandType.CONNECT;
            case "disconnect" -> this.command = CommandType.DISCONNECT_PLAYER;
            case "makemove" -> this.command = CommandType.MAKE_MOVE;
            case "sendmessage" -> this.command = CommandType.SEND_MESSAGE;
            case "commitprompt" -> this.command = CommandType.COMMIT_PROMPT;
            case "createsession" -> this.command = CommandType.CREATE_SESSION;
            case "gethostid" -> this.command = CommandType.GET_HOST_ID;
            case "getlobbyids" -> this.command = CommandType.GET_LOBBY_IDS;
            case "chooserole" -> this.command = CommandType.CHOOSE_ROLE;
            case "chooseteam" -> this.command = CommandType.CHOOSE_TEAM;
            case "getsessions" -> this.command = CommandType.GET_SESSIONS;
            case "getplayercount" -> this.command = CommandType.GET_PLAYER_COUNT;
            case "getplayerroles" -> this.command = CommandType.GET_PLAYER_ROLES;
            case "listen" -> this.command = CommandType.LISTEN;
            case "startgame" -> this.command = CommandType.START_GAME;
            case "getgamedata" -> this.command = CommandType.GET_GAME_DATA;
            case "listenchat" -> this.command = CommandType.LISTEN_CHAT;
            case "message" -> this.command = CommandType.SEND_MESSAGE;
            case "listengame" -> this.command = CommandType.LISTEN_GAME;
            case "loadgame" -> this.command = CommandType.LOAD_GAME;
            default -> this.command = CommandType.UNKNOWN_COMMAND;
        }

        this.arguments = Arrays.copyOfRange(parts, 1, parts.length);
    }

    public CommandType getCommand() {
        return command;
    }

    public String[] getArguments() {
        return arguments;
    }
}
