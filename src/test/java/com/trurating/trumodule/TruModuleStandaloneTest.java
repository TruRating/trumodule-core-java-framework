
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

package com.trurating.trumodule;


import com.trurating.service.v210.xml.Request;
import com.trurating.service.v210.xml.RequestTransaction;
import com.trurating.service.v210.xml.Response;
import com.trurating.trumodule.device.IDevice;
import com.trurating.trumodule.network.HttpClient;
import com.trurating.trumodule.properties.ITruModuleProperties;
import com.trurating.trumodule.properties.TruModulePropertiesSimple;
import com.trurating.trumodule.util.TruRatingMessageFactory;
import mockit.*;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import static mockit.Deencapsulation.setField;


/**
 * The type Tru module standalone test.
 */
@SuppressWarnings("Duplicates")
@RunWith(JMockit.class)
public class TruModuleStandaloneTest {

    @Tested
    private TruModuleStandalone truModuleStandalone;
    @Injectable
    private IDevice iDevice;
    @Injectable
    private HttpClient httpClient;
    @Injectable
    private Logger logger;
    @Injectable
    private ITruModuleProperties properties;
    @Injectable
    private TruRatingMessageFactory truRatingMessageFactoryMock;


    /**
     * Sets up.
     *
     * @throws MalformedURLException the malformed url exception
     */
    @Before
    public void setUp() throws MalformedURLException {
        properties = new TruModulePropertiesSimple("UTEST_PID_1", "UTEST_MID_1","UTEST_TID_1"); // Set of test properties
        truModuleStandalone = new TruModuleStandalone(properties, iDevice);
        setField(truModuleStandalone, "httpClient", httpClient);
        setField(truModuleStandalone, "truRatingMessageFactory", truRatingMessageFactoryMock);
        setField(truModuleStandalone, "logger", logger);
    }

    /**
     * This is a sunny day test that will assume that a user rates, that the service is available and that the ped functions
     * The test data is set to use the pass in the same sessionID as is already cached.
     *
     * @throws Exception the exception
     */
    @Test
    public void doRating() throws Exception {
        final XSD2TestFactory testFactory = new XSD2TestFactory(properties);

        new Expectations() {{
            //noinspection ConstantConditions
            httpClient.send((URL) any, (Request) any);
            returns(testFactory.generateResponseForQuestion(), testFactory.generateResponseForQuestion());
            times = 2; // there will be two calls to httpClient
            //noinspection ConstantConditions
            iDevice.display1AQ1KR((String) any, anyInt);
            returns(8); // the user will customerBeginsTransactionAtTillPoint with 8
            times = 1;
            //noinspection ConstantConditions
            iDevice.displayMessage((String) any);
            times = 1;
        }};

        String result = truModuleStandalone.doRating(properties.getMerchantId(),properties.getTerminalId());
        Assert.assertEquals("Thanks for rating", result);
    }

    /**
     * Send transaction.
     *
     * @throws Exception the exception
     */
    @Test
    public void sendTransaction() throws Exception {
        final XSD2TestFactory testFactory = new XSD2TestFactory(properties);
        new Expectations() {{
            truRatingMessageFactoryMock.assembleRequestTransaction(
                    anyString,
                    anyString,
                    anyString,
                    anyString,
                    (RequestTransaction) any);

            returns(testFactory.generateRequestToDeliverTransaction());
            times = 1;
            //noinspection ConstantConditions
            httpClient.send((URL) any, (Request) any);
            returns(testFactory.generateResponseForQuestion());
            times = 1;
        }};

        Response response = truModuleStandalone.sendTransaction("2","3", testFactory.assembleRequestTransaction());

        Assert.assertNotNull(response);
        //this assertion against a question needs to removing and replacing with a transaction response
        Assert.assertEquals(response.getDisplay().getLanguage().get(0).getQuestion().getValue(), "Please customerBeginsTransactionAtTillPoint 0-9");
    }

    /**
     * Cancel rating.
     *
     * @throws Exception the exception
     */
    @Test
    public void cancelRating() throws Exception {
        new Expectations() {{
            iDevice.resetDisplay();
            returns(null); // the user will customerBeginsTransactionAtTillPoint with 8
            times = 1;
        }};


        truModuleStandalone.cancelRating();
        Assert.assertTrue(truModuleStandalone.isCancelled());
    }

}
