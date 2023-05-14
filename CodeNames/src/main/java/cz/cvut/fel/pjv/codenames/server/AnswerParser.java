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
        ID_LIST,
        UPDATE,
        END_LISTEN,
        UNKNOWN_COMMAND
    }

    private AnswerType answer;
    private String[] arguments;

    public AnswerParser(){
        answer = AnswerType.EMPTY;
        arguments = null;
    }

    public AnswerParser(String answer){
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
