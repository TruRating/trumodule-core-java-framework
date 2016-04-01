package com.truRating.util;

import java.io.File;

import com.truRating.properties.UnitTestProperties;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * Created by Paul on 11/03/2016.
 */
public class IntegrationTestStartUp {

    public static void startup() {
    }

    public static void setupLog4J() {
        ConsoleAppender console = new ConsoleAppender(); //create appender
        String PATTERN = "%d [%p|%c|%C{1}] %m%n";
        console.setLayout(new PatternLayout(PATTERN));
        console.setThreshold(Level.INFO);
        console.activateOptions();
        Logger.getRootLogger().addAppender(console);
    }
}
