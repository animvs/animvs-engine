package br.com.animvs.engine2.console.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

/**
 * Created by DALDEGAN on 01/03/2015.
 */
public final class Console {

    private String batchPath;

    private Array<Command> commands;

    public Console(Array<Command> commands, String batchPath) {
        if (commands == null)
            throw new RuntimeException("The parameter 'commands' must be != NULL");

        if (batchPath == null)
            throw new RuntimeException("The parameter 'batchPath' must be != NULL");

        if (!Gdx.files.internal(batchPath).isDirectory())
            throw new RuntimeException("The specified 'batchPath' isn't a directory: " + batchPath);

        this.commands = commands;
        this.batchPath = batchPath;

        validateRegisteredCommands();
    }

    public void printCommands() {
        Array<String> commandNames = new Array<String>(true, commands.size);
        commandNames.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });

        for (int i = 0; i < commands.size; i++)
            commandNames.add(commands.get(i).getName());

        Gdx.app.log("", "\n----------------------------------- COMMAND LIST -----------------------------------");
        for (int i = 0; i < commandNames.size; i++) {
            Gdx.app.log("", commandNames.get(i));
        }

        Gdx.app.log("", "\n----------------------------------- COMMANDS INFO -----------------------------------");
        for (int i = 0; i < commands.size; i++) {
            Gdx.app.log("", commands.get(i).getInfo());
        }

        Gdx.app.log("", "------------------------------------------------------------------------------------");
    }

    public void executeBatch(CommandSender sender, String filePath) {
        execute(sender, Gdx.files.internal(batchPath + filePath + ".txt").readString());
    }

    public void execute(CommandSender sender, String commandLine) {
        String[] lines = commandLine.split("\n");

        for (int i = 0; i < lines.length; i++) {
            if (checkIsEmptyLine(lines[i]))
                continue;

            if (checkIsCommentLine(lines[i]))
                continue;

            if (!lines[i].contains("("))
                throw new RuntimeException("Invalid command line (missing '(' character): " + lines[i]);
            if (!lines[i].contains(")"))
                throw new RuntimeException("Invalid command line (missing ')' character): " + lines[i]);

            String commandName = lines[i].substring(0, lines[i].indexOf('('));
            String[] parameters = parseParameterStr(i, lines[i]).split(",");

            Command<CommandSender> command = findCommand(commandName);

            int line = lines.length > 1 ? i : 0;
            validateCommand(command, parameters, commandName, commandLine, line);

            int j = 0;
            try {
                for (j = 0; j < command.getRequiredParameters().size; j++)
                    command.getRequiredParameters().get(j).parse(parameters[j]);
            } catch (Exception e) {
                /*if (parameters.length == 0 || parameters[j] == null || parameters[j].trim().length() == 0)
                    throw new ConsoleParameterParseException(command, command.getRequiredParameters().get(j), null, j, line);
                else*/
                throw new ConsoleParameterParseException(command, command.getRequiredParameters().get(j), parameters[j], j, line);
            }

            try {
                command.eventExecution(sender, command.getRequiredParameters());
            } catch (Exception e) {
                throw new ConsoleParseException(command, "Error during command execution: " + command.getName(), e, commandLine, line);
            }
        }
    }

    private void validateRegisteredCommands() {
        for (int i = 0; i < commands.size; i++) {
            for (int j = 0; j < commands.size; j++) {
                if (i == j)
                    continue;

                if (commands.get(i).getClass() == commands.get(j).getClass())
                    throw new RuntimeException("Console command registered more than once: " + commands.get(i).getName());
            }
        }
    }

    private void validateCommand(Command command, String[] parameters, String commandName, String commandLine, int line) {
        if (command == null)
            throw new ConsoleParseException(null, "Unknown command: " + commandName, commandLine, line);

        if (command.getRequiredParameters() == null)
            throw new RuntimeException("Command '" + command.getName() + "' initialized by Console Engine (missing required parameters)");

        if (command.getRequiredParameters().size > parameters.length)
            throw new ConsoleParseException(command, "Parameters are missing - Required: " + command.getRequiredParameters().size + " Found: " + parameters.length, commandLine, line);
    }

    private Command findCommand(String name) {
        for (int i = 0; i < commands.size; i++) {
            if (commands.get(i).getName().equals(name))
                return commands.get(i);
        }

        return null;
    }

    private String parseParameterStr(int line, String commandLine) {
        int startParenthesisCharIndex = -1;
        int endParenthesisCharIndex = -1;

        for (int i = 0; i < commandLine.length(); i++) {
            if (commandLine.charAt(i) == '(') {
                if (startParenthesisCharIndex != -1)
                    throw new ConsoleParseException(null, "COMMAND VALIDATION ERROR: Duplicated character '(', this char must appear ONLY ONCE in each command line", commandLine, line);

                startParenthesisCharIndex = i;
                continue;
            }

            if (commandLine.charAt(i) == ')') {
                if (endParenthesisCharIndex != -1)
                    throw new ConsoleParseException(null, "COMMAND VALIDATION ERROR: Duplicated character ')', this char must appear ONLY ONCE in each command line", commandLine, line);

                endParenthesisCharIndex = i;
                continue;
            }
        }

        if (startParenthesisCharIndex == -1)
            throw new ConsoleParseException(null, "COMMAND VALIDATION ERROR: Missing character '('", commandLine, line);

        if (endParenthesisCharIndex == -1)
            throw new ConsoleParseException(null, "COMMAND VALIDATION ERROR: Missing character ')'", commandLine, line);

        return commandLine.substring(startParenthesisCharIndex + 1, endParenthesisCharIndex);
    }

    private boolean checkIsCommentLine(String line) {
        return line.charAt(0) == '#';
    }

    private boolean checkIsEmptyLine(String line) {
        return line.replace('\r', ' ').replace('n', ' ').trim().length() == 0;
    }
}
