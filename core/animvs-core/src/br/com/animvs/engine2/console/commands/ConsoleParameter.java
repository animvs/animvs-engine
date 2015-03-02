package br.com.animvs.engine2.console.commands;

import java.util.Objects;

/**
 * Created by DALDEGAN on 01/03/2015.
 */
public abstract class ConsoleParameter {
    private String description;
    private String name;

    private Object parsedValue;

    protected abstract String getParameterType();

    public final String getDescription() {
        return description;
    }

    public final String getName() {
        return name;
    }

    public final Object getParsedValue() {
        return parsedValue;
    }

    protected ConsoleParameter(String name, String description) {
        this.name = name;
        this.description = description;
    }

    final void parse(String parameter) throws ConsoleParseException {
        parsedValue = eventParse(parameter);
    }

    protected abstract Object eventParse(String parameter) throws ConsoleParseException;
}
