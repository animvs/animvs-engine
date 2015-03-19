package br.com.animvs.engine2.console.commands;

/**
 * Created by DALDEGAN on 19/03/2015.
 */
public interface CommandSender {
    void executeEventBatch(String eventName, String commandBatchStr);
}
