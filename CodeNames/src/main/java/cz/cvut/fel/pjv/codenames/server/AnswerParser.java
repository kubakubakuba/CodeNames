package cz.cvut.fel.pjv.codenames.server;

import java.util.Arrays;

public class AnswerParser {
    public enum AnswerType{
        EMPTY,
        SESSION_CREATION,
        CONNECT,
        GENERIC_ONE_ARG,
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
            case "1arg" -> this.answer = AnswerType.GENERIC_ONE_ARG;
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
