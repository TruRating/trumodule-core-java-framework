package com.truRating.util;

import com.trurating.ACI.configuration.ProgramProperties;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.File;

/**
 * Created by Paul on 11/03/2016.
 */
public class IntegrationTestStartUp {

    public static void startup() {
        System.setProperty("resources", "c:\\truModule\\");
        File file = new File(ProgramProperties.calculatePropertiesPath("properties", "truModule.properties"));
        ProgramProperties.getInstance().load(file);
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
