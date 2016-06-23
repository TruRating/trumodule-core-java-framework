package com.trurating.trumodule.testharness;

import org.apache.log4j.*;

import java.util.Enumeration;

public class SetUpLog4JDelegate {
    public SetUpLog4JDelegate() {
    }

    public void setupLog4J() {
        if (log4JIsConfigured()==false) {
        ConsoleAppender console = new ConsoleAppender(); //create appender
        String PATTERN = "%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n";
        console.setLayout(new PatternLayout(PATTERN));
        console.setThreshold(Level.INFO);
        console.activateOptions();
        Logger.getRootLogger().addAppender(console);
        }
    }

    private static boolean log4JIsConfigured() {
        Enumeration appenders = Logger.getRootLogger().getAllAppenders();
        if (appenders.hasMoreElements()) {
            return true;
        } else {
            Enumeration loggers = LogManager.getCurrentLoggers();
            while (loggers.hasMoreElements()) {
                Logger c = (Logger) loggers.nextElement();
                if (c.getAllAppenders().hasMoreElements())
                    return true;
            }
        }
        return false;
    }
}