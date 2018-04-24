package br.com.andrewribeiro.ribrest.logs;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 *
 * @author Andrew Ribeiro
 */
public class RibrestLogsFormatter extends Formatter{
    
    @Override
    public String format(LogRecord lr) {
        StringBuilder sb = new StringBuilder();
        sb.append("Ribrest ");
        sb.append(lr.getLevel()).append(": ");
        sb.append(lr.getMessage()).append("\n");
        return sb.toString();
    }
}
