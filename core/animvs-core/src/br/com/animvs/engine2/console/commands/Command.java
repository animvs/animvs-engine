package br.com.animvs.engine2.console.commands;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;

/**
 * Created by DALDEGAN on 01/03/2015.
 */
public abstract class Command {
    private Array<ConsoleParameter> requiredParameters;

    public abstract String getName();

    public abstract String getDescription();

    final Array<ConsoleParameter> getRequiredParameters() {
        return requiredParameters;
    }

    protected abstract Array<ConsoleParameter> createRequiredParameters();

    /*final void execute(String[] parametersStr) {
        for (int i = 0; i < getRequiredParameters().size; i++)
            getRequiredParameters().get(i).<Float>parse(parametersStr[i]);
    }*/

    protected Command() {
        requiredParameters = createRequiredParameters();
    }

    protected abstract void eventExecution(Array<ConsoleParameter> parameters);

    public final String getInfo() {

        StringBuilder usage = new StringBuilder();
        usage.append("---------------------- Command '" + getName() + "' Info --------------------\n");
        usage.append("Command Usage      : ");
        usage.append(getName());
        usage.append("(");

        boolean firstTime = true;
        for (int i = 0; i < getRequiredParameters().size; i++) {
            if (!firstTime)
                usage.append(", ");
            firstTime = false;

            usage.append(getRequiredParameters().get(i).getName() + ": " + getRequiredParameters().get(i).getParameterType());
        }

        usage.append(")\n");
        usage.append("Command Description: ").append(getDescription()).append("\n");
        //usage.append("---------------------- Command Parameters Description ------------------\n");
        usage.append("\nParameters Description:\n");

        firstTime = true;
        for (int i = 0; i < getRequiredParameters().size; i++) {
            if (!firstTime)
                usage.append("\n");
            firstTime = false;

            usage.append("\t").append(getRequiredParameters().get(i).getName()).append(": ");
            usage.append(getRequiredParameters().get(i).getDescription());
        }

        usage.append("\n------------------------------------------------------------------------");

        return usage.toString();
    }
}
