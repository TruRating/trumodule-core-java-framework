package com.trurating.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * A general loader of system properties.
 * This particular implementation uses java.util.Properties
 * but can we swapped out for any implementation
 * The ITruModuleProperties will vary across implementations and aims
 * to wrap to underlying properties implementation, so that only a set of getters
 * and setters are visible to partners.
 *
 */

public abstract class AbstractPropertiesLoader {

    private static Logger log = Logger.getLogger(AbstractPropertiesLoader.class);
    private Properties properties = null;
    private File propertiesFile;

    public AbstractPropertiesLoader() {
    } 

    public void load(File propertiesFile) {
        this.propertiesFile = propertiesFile;
        try {
            FileInputStream fs = new FileInputStream(propertiesFile);
            if (properties==null) properties = new Properties();
            properties.load(fs);
            fs.close();
        } catch (IOException e) {
            log.error(e);
        }
        loadAllProperties() ;
    }

    public abstract void loadAllProperties() ;
    
	public String getProperty(String o) {
        return (String) properties.get(o);
    }

    public void setProperty( String prop , String value ) {
        properties.setProperty(prop, value);
        try {
            FileOutputStream output = new FileOutputStream(propertiesFile);
            properties.store(output, null);
            output.close();
        } catch (IOException e) {
            log.error(e);
        }
    }

    public int getPropertyAsInteger(String o) {
        Integer propertyAsInt;
        try {
            propertyAsInt = new Integer(getProperty(o));
        } catch (NumberFormatException e) {
            log.debug("Warn: Unable to find a valid Integer value for a program property " + o);
            return -1;
        }
        return propertyAsInt;
    }

    public boolean getPropertyAsBoolean(String o) {
		boolean rv = false;
		String value = getProperty(o) ;
		if ( value != null && value.length() > 0 ) {
			rv = false;
			value = value.toLowerCase().trim();
			if (value.equals("true") || value.equals("yes")
					|| value.equals("y"))
				rv = true;
		}
		return rv;
    }
   
    public String[] getKeys() {
        Enumeration e = properties.keys();
        String[] keys = new String[properties.size()];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = (String) e.nextElement();
        }
        return keys;
    }

    public void loadAllPropertiesFromResourcesSystemArg() {
        final String resources = System.getProperty("resources");                                                             //static pre Spring config
        if (resources == null || resources.equals("")) {
            log.debug("The base file system must be passed a 'resources' property as a program VM argument");
            System.out.println("The base file system must be passed a 'resources' property as a program VM argument");

            System.exit(1);
        }

        Vector fileList = obtainListOfAllPropertiesFiles(resources);

        for (int i=0; i<fileList.size(); i++) {
            load(new File(fileList.get(i).toString()));
        }
    }

    private Vector obtainListOfAllPropertiesFiles(String resources) {
        File folder = new File(resources+ "properties");

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
