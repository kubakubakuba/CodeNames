package cz.cvut.fel.pjv.codenames.server;

import java.util.Arrays;

public class AnswerParser {
    public enum AnswerType{
        EMPTY,
        SESSION_CREATION,
        CONNECT,
        ONE_ARG,
        SESSION_LIST,
        PLAYER_COUNT,
        PLAYER_COUNT_ROLES,
        ID_LIST,
        UPDATE,
        END_LISTEN,
        START_GAME,
        GAME_DATA,
        MESSAGE,
        CHAT_ENABLE,
        CHAT_DISABLE,
        END,
        UNKNOWN_COMMAND
    }

    private AnswerType answer;
    private String[] arguments;

    public AnswerParser(){
        answer = AnswerType.EMPTY;
        arguments = null;
    }

    /**
     * An answer parser that parses an answer string into an AnswerType and an array of arguments for that answer
     * @param answer - answer string to parse, split by semicolons
     */
    public AnswerParser(String answer){
        if(answer == null){
            this.answer = AnswerType.EMPTY;
            this.arguments = null;
            return;
        }

        if(answer.startsWith("gamedata;")){
            this.answer = AnswerType.GAME_DATA;
            this.arguments = new String[]{answer.substring(9)};
            return;
        }
        String[] parts = answer.split(";");

        switch (parts[0].toLowerCase()) {
            case "servercreation" -> this.answer = AnswerType.SESSION_CREATION;
            case "connection" -> this.answer = AnswerType.CONNECT;
            case "1arg" -> this.answer = AnswerType.ONE_ARG;
            case "sessionlist" -> this.answer = AnswerType.SESSION_LIST;
            case "playercount" -> this.answer = AnswerType.PLAYER_COUNT;
            case "update" -> this.answer = AnswerType.UPDATE;
            case "endlisten" -> this.answer = AnswerType.END_LISTEN;
            case "idlist" -> this.answer = AnswerType.ID_LIST;
            case "playercountroles" -> this.answer = AnswerType.PLAYER_COUNT_ROLES;
            case "startgame" -> this.answer = AnswerType.START_GAME;
            case "gamedata" -> this.answer = AnswerType.GAME_DATA;
            case "message" -> this.answer = AnswerType.MESSAGE;
            case "chatenable" -> this.answer = AnswerType.CHAT_ENABLE;
            case "chatdisable" -> this.answer = AnswerType.CHAT_DISABLE;
            case "end" -> this.answer = AnswerType.END;
            default -> this.answer = AnswerType.UNKNOWN_COMMAND;
        }

        this.arguments = Arrays.copyOfRange(parts, 1, parts.length);
    }

    public AnswerType getAnswer() {
        return answer;
    }

    public String[] getArguments() {
        return arguments;
    }
}
