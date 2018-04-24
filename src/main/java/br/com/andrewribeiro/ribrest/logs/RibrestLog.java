package br.com.andrewribeiro.ribrest.logs;

import br.com.andrewribeiro.ribrest.Ribrest;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 *
 * @author Andrew Ribeiro
 */
public class RibrestLog {

    private static Logger logger;

    private static Logger getLogger() {
        if (logger == null) {
            logger = Logger.getLogger(RibrestLog.class.getSimpleName());
        } else {
            return logger;
        }
        LogManager.getLogManager().reset();

        Formatter rlf = new RibrestLogsFormatter();

        ConsoleHandler ch = new ConsoleHandler();

        ch.setFormatter(rlf);
        logger.addHandler(ch);

        return logger;
    }

    public static void log(String message) {
        if (Ribrest.getInstance().isDebug()) {
            getLogger().info(message);
        }
    }
    public static void logForced(String message) {
            getLogger().info(message);
    }

}
