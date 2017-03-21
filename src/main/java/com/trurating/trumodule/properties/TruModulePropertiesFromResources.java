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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Implementation of the ITruModuleProperties. Also overrides the abstract
 * loadAllPropertiesFromResourcesSystemArg(), from GeneralPropertiesLoader.
 */
@SuppressWarnings("WeakerAccess")
public class TruModulePropertiesFromResources extends TruModuleProperties {
    private static final Logger log = Logger.getLogger(TruModulePropertiesFromResources.class.getName());
    private String propertiesFilePath;

    private Properties properties = null;

    /**
     * Instantiates a new Tru module properties from resources.
     *
     * @throws MalformedURLException the malformed url exception
     */
    public TruModulePropertiesFromResources() throws MalformedURLException {
        this("properties/truModule.properties");
    }

    /**
     * Instantiates a new Tru module properties.
     *
     * @param propertiesFilePath the properties file path
     * @throws MalformedURLException the malformed url exception
     */
    @SuppressWarnings("unused")
    public TruModulePropertiesFromResources(String propertiesFilePath) throws MalformedURLException {
        setPropertiesFilePath(propertiesFilePath);
        this.loadAllFromFile();
        this.populateObject();
    }

    /**
     * This method override will check the for a system property of TRResources,
     * if one if found it will full in ALL properties files on that path.
     * If no system property is found, classpath in general is scanned.
     * The general classpath in IDEs under Maven will have the properties file in scope
     * for development processes.
     */
    public void loadAllFromFile() {

        int i = this.propertiesFilePath.lastIndexOf('.');
        int p = Math.max(this.propertiesFilePath.lastIndexOf('/'), this.propertiesFilePath.lastIndexOf('\\'));

        if (i > p) {
            String extension = this.propertiesFilePath.substring(i + 1);

            if (extension.equals("properties")) {
                load(new File(extension));
                return;
            } else if (extension.equals("xml")) {
                loadFromXML(new File(extension));
                return;
            }
        }
        getLog().severe("No resource file specified");
    }

    /**
     * Sets properties file path.
     *
     * @param propertiesFilePath the properties file path
     */
    void setPropertiesFilePath(String propertiesFilePath) {
        this.propertiesFilePath = propertiesFilePath;
    }

    /**
     * Gets log.
     *
     * @return the log
     */
    public static Logger getLog() {
        return log;
    }

    private void populateObject() throws MalformedURLException {
        setPartnerId(getProperty("partnerId"));
        setMerchantId(getProperty("merchantId"));
        setTerminalId(getProperty("terminalId"));
        setTransportKey(getProperty("transportKey"));
        setSocketTimeoutInMilliSeconds(getPropertyAsInt("socket_timeout"));
        setTruServiceURL(new URL(getProperty("service_URL")));
        setRFC(getProperty("rfc"));
    }

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
            log.warning("Parse exception when attempting to parse String " + o + ", into an integer. Okay if this is a testProperties.");
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
        loadProperties(propertiesFile, PropertyFileTypes.PROPERTY);
    }

    /**
     * Load from xml.
     *
     * @param propertiesFile the properties file
     */
    void loadFromXML(File propertiesFile) {
        loadProperties(propertiesFile, PropertyFileTypes.XML);
    }

    private void loadProperties(File propertiesFile, PropertyFileTypes propertyType) {
        try {
            FileInputStream fs = new FileInputStream(propertiesFile);
            if (properties == null) properties = new Properties();
            switch (propertyType) {
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

    /**
     * The enum Property file types.
     */
    enum PropertyFileTypes {
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
