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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Implementation of the ITruModuleProperties. Also overrides the abstract
 * loadAllPropertiesFromResourcesSystemArg(), from GeneralPropertiesLoader.
 */
public class TruModuleProperties extends GeneralPropertiesLoader implements ITruModuleProperties {

    private String partnerId = "";
    private String merchantId = "";
    private String terminalId = "";
    private String rfc = "en-GB";
    private int socketTimeoutInMilliSeconds;
    private static Logger log = Logger.getLogger(TruModuleProperties.class.getName());
    private URL truServiceURL;
    private String propertiesFilePath;
    private String transportKey;

    /**
     * Instantiates a new Tru module properties.
     */
    public TruModuleProperties(){
        super();
    }

    /**
     * Instantiates a new Tru module properties.
     *
     * @param propertiesFilePath the properties file path
     * @throws MalformedURLException the malformed url exception
     */
    @SuppressWarnings("unused")
    public TruModuleProperties(String propertiesFilePath) throws MalformedURLException{
        super();
        setPropertiesFilePath(propertiesFilePath);
        this.loadAllFromFile();
        setPartnerId(getProperty("partnerId"));
        setMerchantId(getProperty("merchantId"));
        setTerminalId(getProperty("terminalId"));
        setSocketTimeoutInMilliSeconds(getPropertyAsInt("socket_timeout"));
        setTruServiceURL(new URL(getProperty("service_URL")));
        setRFC(getProperty("rfc"));
    }

    /**
     * This method override will check the for a system property of TRResources,
     * if one if found it will full in ALL properties files on that path.
     * If no system property is found, classpath in general is scanned.
     * The general classpath in IDEs under Maven will have the properties file in scope
     * for development processes.
     */
    @Override
    public void loadAllFromFile(){

        int i = this.propertiesFilePath.lastIndexOf('.');
        int p = Math.max(this.propertiesFilePath.lastIndexOf('/'), this.propertiesFilePath.lastIndexOf('\\'));

        if (i > p) {
            String extension = this.propertiesFilePath.substring(i+1);

            if(extension.equals("properties")){
                load(new File(extension));
                return;
            }
            else if(extension.equals("xml")){
                loadFromXML(new File(extension));
                return;
            }
        }
        getLog().severe("No resource file specified");
    }

    /**
     * Gets properties file path.
     *
     * @return the properties file path
     */
    String getPropertiesFilePath() {
        return propertiesFilePath;
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

    @Override
    public String getPartnerId() {
        return partnerId;
    }

    /**
     * Sets partner id.
     *
     * @param partnerId the partner id
     */
    void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    @Override
    public String getMerchantId() {
        return merchantId;
    }

    /**
     * Sets merchant id.
     *
     * @param merchantId the merchant id
     */
    void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    @Override
    public String getTerminalId() {
        return terminalId;
    }

    /**
     * Sets terminal id.
     *
     * @param terminalId the terminal id
     */
    void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    @Override
    public String getTransportKey() {
        return transportKey;
    }

    /**
     * Sets transport key.
     *
     * @param transportKey the transport key
     */
    void setTransportKey(String transportKey) {
        this.transportKey = transportKey;
    }

    @Override
    public int getSocketTimeoutInMilliSeconds() {
        return socketTimeoutInMilliSeconds;
    }


    /**
     * Sets socket timeout in milli seconds.
     *
     * @param socketTimeoutInMilliSeconds the socket timeout in milli seconds
     */
    void setSocketTimeoutInMilliSeconds(int socketTimeoutInMilliSeconds) {
        this.socketTimeoutInMilliSeconds = socketTimeoutInMilliSeconds;
    }

    @Override
    public URL getTruServiceURL() {
        return truServiceURL;
    }

    /**
     * Sets tru service url.
     *
     * @param truServiceURL the tru service url
     */
    void setTruServiceURL(URL truServiceURL) {
        this.truServiceURL = truServiceURL;
    }

    @Override
    public String getRFC() {
        return this.rfc;
    }

    /**
     * Sets rfc.
     *
     * @param rfc the rfc
     */
    @SuppressWarnings("WeakerAcccess")
    void setRFC(String rfc) {
        this.rfc = rfc;
    }
}
