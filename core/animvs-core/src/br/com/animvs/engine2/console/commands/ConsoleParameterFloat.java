package br.com.animvs.engine2.console.commands;

/**
 * Created by DALDEGAN on 01/03/2015.
 */
public final class ConsoleParameterFloat extends ConsoleParameter {

    public ConsoleParameterFloat(String name, String description) {
        super(name, description);
    }

    @Override
    protected String getParameterType() {
        return "FLOAT";
    }

    @Override
    protected Float eventParse(String parameter) throws ConsoleParseException {
        return Float.parseFloat(parameter);
    }
}
