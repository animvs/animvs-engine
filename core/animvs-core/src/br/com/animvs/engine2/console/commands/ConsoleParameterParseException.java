package br.com.animvs.engine2.console.commands;

/**
 * Created by DALDEGAN on 01/03/2015.
 */
public final class ConsoleParameterParseException extends RuntimeException {
    public ConsoleParameterParseException(Command command, ConsoleParameter parameter, String wrongValue, int parameterIndex, int line) {
        this(null, command, parameter, wrongValue, parameterIndex, line);
    }

    public ConsoleParameterParseException(Throwable cause, Command command, ConsoleParameter parameter, String wrongValue, int parameterIndex, int line) {
        super(prepareMessage(command, parameter, wrongValue, parameterIndex, line), cause);
    }

    private static String prepareMessage(Command command, ConsoleParameter parameter, String wrongValue, int parameterIndex, int line) {
        String message = null;
        message = "\n\n --------------------- Error parsing parameter '" + parameterIndex + "' ---------------------\n";
        message += "command name  : " + command.getName() + "\n";
        message += "wrong value   : " + wrongValue + "\n";
        message += "parameter name: " + parameter.getName() + "\n";
        message += "required type : " + parameter.getParameterType();

        if (line > 0)
            message += "\nline          : " + line;

        message += "\n------------------------------------------------------------------------\n";
        message += command.getInfo();


        return message;
    }
}
