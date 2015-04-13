package br.com.animvs.engine2.console.commands;

/**
 * Created by DALDEGAN on 01/03/2015.
 */
public final class ConsoleParameterInteger extends ConsoleParameter {

    public ConsoleParameterInteger(String name, String description) {
        super(name, description);
    }

    @Override
    protected String getParameterType() {
        return "INTEGER";
    }

    @Override
    protected Integer eventParse(String parameter) throws ConsoleParseException {
        return Integer.parseInt(parameter.trim());
    }
}
