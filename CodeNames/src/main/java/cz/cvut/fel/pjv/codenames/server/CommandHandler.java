package cz.cvut.fel.pjv.codenames.server;

import java.util.Arrays;

public class CommandHandler {
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
        UNKNOWN_COMMAND
    }

    private CommandType command;
    private String[] arguments;

    public CommandHandler(){
        command = CommandType.EMPTY;
        arguments = null;
    }

    public CommandHandler(String command){
        command = command;
        String[] parts = command.split(";");

        System.out.println(command);
        System.out.println(parts);

        switch(parts[0].toLowerCase()){
            case "startserver":
                this.command = CommandType.START_SERVER;
                break;
            case "terminateserver":
                this.command = CommandType.TERMINATE_SERVER;
                break;
            case "connect":
                this.command = CommandType.CONNECT;
                break;
            case "disconnect":
                this.command = CommandType.DISCONNECT_PLAYER;
                break;
            case "makemove":
                this.command = CommandType.MAKE_MOVE;
                break;
            case "sendmessage":
                this.command = CommandType.SEND_MESSAGE;
                break;
            case "commitprompt":
                this.command = CommandType.COMMIT_PROMPT;
                break;
            case "createsession":
                this.command = CommandType.CREATE_SESSION;
                break;
            case "gethostid":
                this.command = CommandType.GET_HOST_ID;
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
