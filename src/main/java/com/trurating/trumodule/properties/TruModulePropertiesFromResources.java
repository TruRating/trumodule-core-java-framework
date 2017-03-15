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
import java.util.Vector;

/**
 * Implementation of the ITruModuleProperties. Also overrides the abstract
 * loadAllPropertiesFromResourcesSystemArg(), from GeneralPropertiesLoader.
 */
@SuppressWarnings("WeakerAccess")
public class TruModulePropertiesFromResources extends TruModuleProperties {

    /**
     * Instantiates a new Tru module properties from resources.
     *
     * @throws MalformedURLException the malformed url exception
     */
    public TruModulePropertiesFromResources() throws MalformedURLException {
        load("properties/truModule.properties");
    }

    /**
     * Instantiates a new Tru module properties from resources.
     *
     * @param propertiesFilePath the properties file path
     * @throws MalformedURLException the malformed url exception
     */
    public TruModulePropertiesFromResources(String propertiesFilePath) throws MalformedURLException {
        load(propertiesFilePath);
    }

    private void load(String propertiesFilePath) throws MalformedURLException {
        setPropertiesFilePath(propertiesFilePath);
        this.loadAllPropertiesFromResourcesSystemArg();
        setPartnerId(getProperty("partnerId"));
        setMerchantId(getProperty("merchantId"));
        setTerminalId(getProperty("terminalId"));
        setTransportKey(getProperty("transportKey"));
        setSocketTimeoutInMilliSeconds(getPropertyAsInt("socket_timeout"));
        setTruServiceURL(new URL(getProperty("service_URL")));
        setRFC(getProperty("rfc"));
    }

    /**
     * This method override will check the for a system property of TRResources,
     * if one if found it will fill in ALL properties files on that path.
     * If no system property is found, classpath in general is scanned.
     * The general classpath in IDEs under Maven will have the properties file in scope
     * for development processes.
     */
    public void loadAllPropertiesFromResourcesSystemArg() {
        String resources = System.getProperty("TRResources");

        if (resources == null || resources.equals("")) {
            if(this.getPropertiesFilePath() == null){
                getLog().info("No resource file specified");
                return;
            }
            ClassLoader classLoader = getClass().getClassLoader();
            URL url = classLoader.getResource(this.getPropertiesFilePath());
            if (url != null) load(new File(url.getFile()));
        } else {
            getLog().info("Resources=" + resources);
            Vector fileList = super.obtainListOfAllPropertiesFiles(resources);
            for (Object aFileList : fileList) {
                load(new File(aFileList.toString()));
            }
        }
    }

}
