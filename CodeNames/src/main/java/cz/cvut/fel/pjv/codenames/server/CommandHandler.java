package cz.cvut.fel.pjv.codenames.server;

import java.util.Arrays;

public class CommandHandler {
    public enum CommandType{
        START_SERVER,
        TERMINATE_SERVER,
        CONNECT_PLAYER,
        DISCONNECT_PLAYER,
        MAKE_MOVE,
        SEND_MESSAGE,
        COMMIT_PROMPT,
        UNKNOWN_COMMAND
    }

    CommandType command;
    String[] arguments;

    public CommandHandler(String command){
        command = command.toLowerCase();
        String[] parts = command.split(";");

        switch(parts[0]){
            case "start":
                this.command = CommandType.START_SERVER;
                break;
            case "terminate":
                this.command = CommandType.TERMINATE_SERVER;
                break;
            case "con":
                this.command = CommandType.CONNECT_PLAYER;
                break;
            case "dc":
                this.command = CommandType.DISCONNECT_PLAYER;
                break;
            case "mm":
                this.command = CommandType.MAKE_MOVE;
                break;
            case "sm":
                this.command = CommandType.SEND_MESSAGE;
                break;
            case "cp":
                this.command = CommandType.COMMIT_PROMPT;
                break;
            default:
                this.command = CommandType.UNKNOWN_COMMAND;
                break;
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
