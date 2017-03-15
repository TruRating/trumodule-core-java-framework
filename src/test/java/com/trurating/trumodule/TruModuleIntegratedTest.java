
/*
 * // The MIT License
 * //
 * // Copyright (c) 2017 TruRating Ltd. https://www.trurating.com
 * //
 * // Permission is hereby granted, free of charge, to any person obtaining a copy
 * // of this software and associated documentation files (the "Software"), to deal
 * // in the Software without restriction, including without limitation the rights
 * // to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * // copies of the Software, and to permit persons to whom the Software is
 * // furnished to do so, subject to the following conditions:
 * //
 * // The above copyright notice and this permission notice shall be included in
 * // all copies or substantial portions of the Software.
 * //
 * // THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * // IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * // FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * // AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * // LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * // OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * // THE SOFTWARE.
 */

package com.trurating.trumodule;

import com.trurating.service.v210.xml.*;
import com.trurating.trumodule.device.IDevice;
import com.trurating.trumodule.network.HttpClient;
import com.trurating.trumodule.properties.ITruModuleProperties;
import com.trurating.trumodule.properties.TruModulePropertiesSimple;
import com.trurating.trumodule.util.TruRatingMessageFactory;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;
import java.util.Date;

import java.util.logging.Logger;

import static mockit.Deencapsulation.setField;

@SuppressWarnings("Duplicates")
@RunWith(JMockit.class)
public class TruModuleIntegratedTest {

    @Tested
    private TruModuleIntegrated truModule;
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

    private String sessionID;

    @Before
    public void setUp() throws Exception {
        properties = new TruModulePropertiesSimple("UTEST_PID_1", "UTEST_MID_1","UTEST_TID_1"); // Set of test properties
        truModule = new TruModuleIntegrated(properties, iDevice);
        setField(truModule, "httpClient", httpClient);
        setField(truModule, "truRatingMessageFactory", truRatingMessageFactoryMock);
        setField(truModule, "logger", logger);
        sessionID = Long.toString(new Date().getTime());
    }

    @Test
    public void sendPosEvent() throws Exception {
        final XSD2TestFactory testFactory = new XSD2TestFactory(properties);
        new Expectations() {{
            //noinspection ConstantConditions
            httpClient.send((URL) any, (Request) any);
            returns(testFactory.generateResponseForPosEvent());
            times = 1;
        }};

        PosParams params = new PosParams(sessionID);
        Response response = truModule.sendPosEvent(params,testFactory.generateRequestPosEvent());

        Assert.assertNotNull(response);
    }

    public void sendPosEventList() throws Exception {

    }

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

        Response response = truModule.sendTransaction("2","3", testFactory.assembleRequestTransaction());

        Assert.assertNotNull(response);
        //this assertion against a question needs to removing and replacing with a transaction response
        Assert.assertEquals(response.getDisplay().getLanguage().get(0).getQuestion().getValue(), "Please customerBeginsTransactionAtTillPoint 0-9");
    }

    @Test
    public void cancelRating() throws Exception {
        new Expectations() {{
            iDevice.resetDisplay();
            returns(null); // the user will customerBeginsTransactionAtTillPoint with 8
            times = 1;
        }};


        truModule.cancelRating();
        Assert.assertTrue(truModule.isCancelled());
    }
}