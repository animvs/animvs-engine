package br.com.animvs.engine2.console.commands;

/**
 * Created by DALDEGAN on 01/03/2015.
 */
public final class ConsoleParameterString extends ConsoleParameter {

    public ConsoleParameterString(String name, String description) {
        super(name, description);
    }

    @Override
    protected String getParameterType() {
        return "STRING";
    }

    @Override
    protected String eventParse(String parameter) throws ConsoleParseException {
        return parameter;
    }
}
