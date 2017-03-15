
/*
 *  The MIT License
 *
 *  Copyright (c) 2017 TruRating Ltd. https://www.trurating.com
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package com.trurating.trumodule.properties;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * A general loader of system properties.
 * This particular implementation uses java.util.Properties
 * but can we swapped out for any implementation
 * The ITruModuleProperties will vary across implementations and aims
 * to wrap to underlying properties implementation, so that only a set of getters
 * and setters are visible to partners.
 * <p>
 * This class has one abstract method. this is where individual partner props files are specified for loading
 * In specific overloaded examples, in the abstract method, the convention at TruRating has been
 * to pass properties files located outside the IDE in a JVM arguments, but where no JVM -D arg
 * has been found, we have used searched on the classpath for the relevant properties file
 */
@SuppressWarnings("Duplicates")
public abstract class GeneralPropertiesLoader {

    private static Logger log = Logger.getLogger(GeneralPropertiesLoader.class.getName());
    private Properties properties = null;

    /**
     * Load all from file.
     */
//This method will vary by partner.
    public abstract void loadAllFromFile();

    /**
     * Gets property.
     *
     * @param o the o
     * @return the property
     */
    String getProperty(String o) {
        return (String) properties.get(o);
    }

    /**
     * Gets property as int.
     *
     * @param o the o
     * @return the property as int
     */
    @SuppressWarnings("SameParameterValue")
    int getPropertyAsInt(String o) {
        int i = -99;
        try {
            i = Integer.parseInt(this.getProperty(o));
        } catch (NumberFormatException e) {
            log.warning("Parse exception when attempting to parse String " + o + ", into an integer. Okay if this is a testPropertiesOverriden.");
        }
        return i;
    }

    /**
     * Gets property as boolean.
     *
     * @param b the b
     * @return the property as boolean
     */
    @SuppressWarnings("unused")
    public boolean getPropertyAsBoolean(String b) {
        return Boolean.parseBoolean((String) properties.get(b));
    }

    /**
     * Load.
     *
     * @param propertiesFile the properties file
     */
    void load(File propertiesFile) {
        loadProperties(propertiesFile,PropertyFileTypes.PROPERTY);
    }

    /**
     * Load from xml.
     *
     * @param propertiesFile the properties file
     */
    void loadFromXML(File propertiesFile) {
        loadProperties(propertiesFile,PropertyFileTypes.XML);
    }


    /**
     * Obtain list of all properties files vector.
     *
     * @param resources the resources
     * @return the vector
     */
    Vector<File> obtainListOfAllPropertiesFiles(String resources) {
        return obtainListOfAllFiles(resources,"properties");
    }

    /**
     * Obtain list of all xml files vector.
     *
     * @param resources the resources
     * @return the vector
     */
    @SuppressWarnings("unused")
    Vector<File> obtainListOfAllXMLFiles(String resources) {
        return obtainListOfAllFiles(resources,"xml");
    }

    private void loadProperties(File propertiesFile, PropertyFileTypes propertyType) {
        try {
            FileInputStream fs = new FileInputStream(propertiesFile);
            if (properties == null) properties = new Properties();
            switch(propertyType){
                case PROPERTY:
                    properties.load(fs);
                    break;
                case XML:
                    break;
                default:
                    throw new Exception("Unknown property type");
            }
            properties.load(fs);
            fs.close();
        } catch (Exception e) {
            log.warning(e.toString());
        }
    }

    private Vector<File> obtainListOfAllFiles(String resources, String fileExtension) {
        File folder = new File(resources + File.separator + fileExtension);

        File[] fileList = folder.listFiles();

        if(fileList == null) {
            return new Vector<File>(0);
        }

        Vector<File> filterFileList = new Vector<File>(fileList.length);
        for (File aFileList : fileList) {
            if (aFileList.isFile() && aFileList.getName().contains("." + fileExtension)) {
                filterFileList.add(aFileList);
            }
        }
        return filterFileList;
    }

    /**
     * The enum Property file types.
     */
    enum PropertyFileTypes{
        /**
         * Property property file types.
         */
        PROPERTY,
        /**
         * Xml property file types.
         */
        XML
    }
}
