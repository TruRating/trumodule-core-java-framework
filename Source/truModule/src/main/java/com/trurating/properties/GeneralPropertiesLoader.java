package com.trurating.properties;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

/**
 * A general loader of system properties.
 * This particular implementation uses java.util.Properties
 * but can we swapped out for any implementation
 * The ITruModuleProperties will vary across implementations and aims
 * to wrap to underlying properties implementation, so that only a set of getters
 * and setters are visible to partners.
 *
 * REQUIRES
 * Expects a resources argument passed as JVM argument
 *
 */

@SuppressWarnings("Duplicates")
public class GeneralPropertiesLoader {

    private static Logger log = Logger.getLogger(GeneralPropertiesLoader.class);
    private Properties properties = null;

    public String getProperty(String o) {
        return (String) properties.get(o);
    }

    public int getPropertyAsInt(String o) {
        return Integer.parseInt((String)properties.get(o));
    }

    public boolean getPropertyAsBoolean(String b) {
        return Boolean.parseBoolean((String)properties.get(b));
    }

    public GeneralPropertiesLoader() {
        loadAllPropertiesFromResourcesSystemArg();
    }

    public void loadAllPropertiesFromResourcesSystemArg() {   	
        String resources = System.getProperty("resources");                                                             //static pre Spring config

        if (resources == null || resources.equals("")) 
        	resources = "C:\\trurating\\";        
        
        if (resources == null || resources.equals("")) 
        {
            log.error("The base file system must be passed a 'resources' property as a program VM argument");
            System.out.println("The base file system must be passed a 'resources' property as a program VM argument");

            System.exit(1);
        }

        Vector fileList = obtainListOfAllPropertiesFiles(resources);

        for (int i=0; i<fileList.size(); i++) {
            load(new File(fileList.get(i).toString()));
        }
    }

    public void load(File propertiesFile) {
        try {
            FileInputStream fs = new FileInputStream(propertiesFile);
            if (properties==null) properties = new Properties();
            properties.load(fs);
            fs.close();
        } catch (IOException e) {
            log.error(e);
        }
    }

    private Vector obtainListOfAllPropertiesFiles(String resources) {
        File folder = new File(resources+ File.separator + "properties");

        File[] fileList = folder.listFiles();

        Vector filterFileList = new Vector(fileList.length);
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isFile() && fileList[i].getName().contains(".properties")) {
                filterFileList.add(fileList[i]);
            }
        }
        return filterFileList;
    }
}
