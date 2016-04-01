package com.truRating.truSharedData.properties;

import com.truRating.truModule.properties.ITruModuleProperties;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

/**
 * Created by Paul on 2016. :)
 */
public class ProgramProperties implements ITruModuleProperties {

    private static Logger log = Logger.getLogger(ProgramProperties.class);
    private Properties properties = null;
    private File propertiesFile;
    private String pathToResources;
    private static ProgramProperties INSTANCE=null;

    private ProgramProperties() {}

    public static synchronized ProgramProperties getInstance() {
        if (INSTANCE==null) {
            return INSTANCE= new ProgramProperties();
        }
        return INSTANCE;
    }

    public static String calculatePropertiesPath(String relativePathAddition, String fileName) {
        final String resources = System.getProperty("resources");
        String fileSeparator = System.getProperty("file.separator");
        String pathToFile = resources + relativePathAddition;
        if (!fileName.equals("")) pathToFile += fileSeparator + fileName;
        log.debug("Path to resource file is : " + pathToFile);
        return pathToFile;
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
    }

    public void update(Properties properties) {
        Set keys = properties.keySet();
        for (Object key : keys) {
            if ( !this.properties.containsKey(key) ) {
                this.properties.put(key, properties.get(key));
            }
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

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
            log.error("Unable to find a valid Integer value for a program property " + o);
            return -1;
        }
        return propertyAsInt;
    }

    public String[] getKeys() {
        Enumeration e = properties.keys();
        String[] keys = new String[properties.size()];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = (String) e.nextElement();
        }
        return keys;
    }

    public Set<String> getKeysAsSet() {
        return properties.stringPropertyNames();
    }

    public int size() {
        return properties.size();
    }

    public String getPathToResources() {
        return pathToResources;
    }

    public void setPathToResources(String pathToResources) {
        this.pathToResources = pathToResources;
    }

    public String getMid() {
        return getProperty("mid");
    }

    public String getTid() {
        return getProperty("tid");
    }

    public String getUid() {
        return getProperty("uid");
    }

    public String getLanguageCode() {
        return getProperty("languageCode");
    }

    public String getIncludeReceipt() {
        return getProperty("includeAcknowledgement");
    }

    public String getIncludeAcknowledgement() {
        return getProperty("includeAcknowledgement");
    }

    public String getDeviceType() {
        return getProperty("device_Type");
    }

    public String getDevicenLines() {
        return getProperty("device_nLines");
    }

    public String getDeviceCpl() {
        return getProperty("device_cpl");
    }

    public String getDeviceFormat() {
        return getProperty("device_format");
    }

    public String getDeviceFirmware() {
        return getProperty("device_firmware");
    }

    public String getDeviceFontType() {
        return getProperty("device_font_type");
    }

    public String getServerId() {
        return getProperty("serverId");
    }

    public String getPpaFirmware() {
        return getProperty("ppaFirmware");
    }

    public String getTruServiceIPAddress() {
        return getProperty("node");
    }

    public int getTruServiceSocketTimeoutInMilliSeconds() {
        return new Integer(getProperty("socket_timeout")).intValue();
    }

    public int getTruServiceSocketPortNumber() {
        return new Integer(getProperty("port")).intValue();
    }

    public Properties initialisePropertiesFromSystemArg() {
        final String resources = System.getProperty("resources");                                                             //static pre Spring config
        if (resources == null || resources.equals("")) {
            log.error("The base file system must be passed a 'resources' property as a program VM argument");
            System.exit(1);
        } else {
            log.info("Using clientResources folder as :" + resources);
            ProgramProperties.getInstance().setPathToResources(resources);
            File file = new File(ProgramProperties.calculatePropertiesPath("properties", "truModule.properties"));
            ProgramProperties.getInstance().load(file);
        }
        return ProgramProperties.getInstance().getProperties();
    }

    public void loadAllPropertiesInDirectory(File file) {

    }

    public void loadAllPropertiesFromResourcesSystemArg() {
        final String resources = System.getProperty("resources");                                                             //static pre Spring config
        if (resources == null || resources.equals("")) {
            log.error("The base file system must be passed a 'resources' property as a program VM argument");
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
