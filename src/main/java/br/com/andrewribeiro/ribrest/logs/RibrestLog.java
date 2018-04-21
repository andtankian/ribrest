package br.com.andrewribeiro.ribrest.logs;

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

    public static Logger getLogger() {
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

}
