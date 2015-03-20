package br.com.animvs.engine2.console.commands;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by DALDEGAN on 19/03/2015.
 */
public final class ConsoleParameterColor extends ConsoleParameter {

    public ConsoleParameterColor(String name, String description) {
        super(name, description);
    }

    @Override
    protected String getParameterType() {
        return "COLOR";
    }

    @Override
    protected Color eventParse(String parameter) throws ConsoleParseException {
        String[] split = parameter.split(":");

        if (split == null)
            throw new IllegalArgumentException("CONSOLE - Error parsing Color parameter: required parameters are NULL");
        else if (split.length < 4)
            throw new IllegalArgumentException("CONSOLE - Error parsing Color parameter: four FLOAT parameters are required while ONLY '" + split.length + "' where specified. Usage ex: '1:1:1:1'");

        return new Color(Float.parseFloat(split[0]), Float.parseFloat(split[1]), Float.parseFloat(split[2]), Float.parseFloat(split[3]));
    }
}
