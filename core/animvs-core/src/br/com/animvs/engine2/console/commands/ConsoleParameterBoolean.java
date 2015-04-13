package br.com.animvs.engine2.console.commands;

/**
 * Created by DALDEGAN on 19/03/2015.
 */
public final class ConsoleParameterBoolean extends ConsoleParameter {

    public ConsoleParameterBoolean(String name, String description) {
        super(name, description);
    }

    @Override
    protected String getParameterType() {
        return "BOOLEAN";
    }

    @Override
    protected Boolean eventParse(String parameter) throws ConsoleParseException {
        return Boolean.parseBoolean(parameter.trim());
    }
}
