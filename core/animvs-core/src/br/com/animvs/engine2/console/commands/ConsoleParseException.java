package br.com.animvs.engine2.console.commands;

/**
 * Created by DALDEGAN on 28/02/2015.
 */
final class ConsoleParseException extends RuntimeException {

    public ConsoleParseException(Command command, Throwable cause, String commandLine, int line) {
        super(prepareMessage(command, line, commandLine, null, cause), cause);
    }

    public ConsoleParseException(Command command, String message, Throwable cause, String commandLine, int line) {
        super(prepareMessage(command, line, commandLine, message, cause), cause);
    }

    public ConsoleParseException(Command command, String message, String commandLine, int line) {
        super(prepareMessage(command, line, commandLine, message, null));
    }

    private static String prepareMessage(Command command, int line, String commandLine, String message, Throwable cause) {
        String preparedMessage = null;
        preparedMessage = "\n\n --------------------- ERROR EXECUTING COMMAND ---------------------\n";
        preparedMessage += "exec   : " + commandLine + "\n";

        if (message != null)
            preparedMessage += "message: " + message + "\n";

        if (cause != null && cause.getMessage() != null)
            preparedMessage += "cause  : " + cause.getMessage() + "\n";

        if (line > 0)
            preparedMessage += "line: " + String.valueOf(line + 1) + "\n";

        preparedMessage += "\n------------------------------------------------------------------\n";

        if (command != null)
            preparedMessage += command.getInfo();

        return preparedMessage;
    }
}
