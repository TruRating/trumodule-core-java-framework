/*
 *
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
 *
 */
package com.trurating.trumodule.network;

import com.trurating.service.v220.xml.Request;
import com.trurating.service.v220.xml.RequestPosEvent;
import com.trurating.service.v220.xml.RequestPosStartTilling;
import com.trurating.trumodule.messages.TruModuleMessageFactory;
import com.trurating.trumodule.properties.ITruModuleProperties;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(JMockit.class)
public class TruServiceHttpClientTest {
    @Tested
    private
    TruServiceHttpClient truServiceHttpClient;

    @Mocked
    private
    HttpURLConnection httpURLConnection;
    @Mocked
    private
    ITruModuleProperties properties;
    @Mocked
    private
    ISerializer serializer;

    private String partnerId ="1";
    private String merchantId = "UNITTEST_MERCH_1";
    private String terminalId = "UNITTEST_TERM_1";
    private String sessionId = "UNITTEST_SESSION_1";
    private String transportKey = "UNITTESTTRANSPORTKEY1234";

    @Before
    public void setUp() {
        new Expectations() {{
            properties.getTransportKey();
            returns(transportKey);
        }};
        truServiceHttpClient = new TruServiceHttpClient(properties,serializer);
    }

    @Test
    public void serializeRequest() throws Exception {
        RequestPosEvent event = new RequestPosEvent();
        RequestPosStartTilling posStartTilling = new RequestPosStartTilling();
        event.setStartTilling(posStartTilling);

        final Request request = TruModuleMessageFactory.assembleRequestPosEvent(partnerId,merchantId,terminalId,sessionId,event);

        truServiceHttpClient.serializeRequest(request);

        new Verifications() {{
            Request capture;
            serializer.marshall(capture = withCapture());
            assertEquals(request,capture);
        }};
    }

    @Test
    public void unSerializeResponse() throws Exception {
        final String xml = "<Response PartnerId=\"1\" MerchantId=\"UNITTEST_MERCH_1\" TerminalId=\"UNITTEST_TERM_1\" SessionId=\"UNITTEST_SESSION_1\" xmlns=\"http://docs.trurating.com/schema/truservice/v220.xsd\">\n" +
                "  <Display>\n" +
                "    <Language Rfc1766=\"en-GB\">\n" +
                "      <Question TimeoutMs=\"45000\">||||Please rate the menu|from 0-9 or clear</Question>\n" +
                "      <Screen TimeoutMs=\"3000\" When=\"RATED\" Priority=\"false\">                    |                    |                    |                    | Thanks for rating  |                    |                    |                    |                    |                    </Screen>\n" +
                "      <Screen TimeoutMs=\"3000\" When=\"NOTRATED\" Priority=\"false\">                    |                    |                    |                    |  Sorry you didn't  |        rate        |                    |                    |                    |                    </Screen>\n" +
                "      <Receipt When=\"RATED\" Type=\"CUSTOMER\" Priority=\"false\">***************|***************|***************|***************|  Thanks for   | rating! Go to | trurating.com |  for ratings  |you can trust. |***************|***************|***************|***************</Receipt>\n" +
                "      <Receipt When=\"NOTRATED\" Type=\"CUSTOMER\" Priority=\"false\">***************|***************|***************|***************|   Sorry you   | didn't rate!  |     Go to     | trurating.com |  for ratings  |you can trust. |***************|***************|***************|***************</Receipt>\n" +
                "    </Language>\n" +
                "  </Display>\n" +
                "</Response>";



        truServiceHttpClient.unSerializeResponse(xml);

        new Verifications() {{
            String capture;
            serializer.unMarshall(capture = withCapture());
            assertEquals(xml,capture);
        }};
    }

    @Test
    public void addHeaderInformation() throws Exception {
        RequestPosEvent event = new RequestPosEvent();
        RequestPosStartTilling posStartTilling = new RequestPosStartTilling();
        event.setStartTilling(posStartTilling);

        final String requestXml = "<Request PartnerId=\"1\" MerchantId=\"UNITTEST_MERCH_1\" TerminalId=\"UNITTEST_TERM_1\" SessionId=\"UNITTEST_SESSION_1\" xmlns=\"http://docs.trurating.com/schema/truservice/v220.xsd\">\n" +
                "    <PosEvent>\n" +
                "        <StartTilling/>\n" +
                "    </PosEvent>\n" +
                "</Request>";

        final StringWriter requestStringWriter = new StringWriter();
        requestStringWriter.write(requestXml);

        Request request = TruModuleMessageFactory.assembleRequestPosEvent(partnerId,merchantId,terminalId,sessionId,event);

        new Expectations() {{
            serializer.marshall((Request)any);
            returns(requestStringWriter);
        }};

        truServiceHttpClient.addHeaderInformation(httpURLConnection,request);

        final List<String> capturedKeys = new ArrayList<String>();
        final List<String> capturedValues = new ArrayList<String>();

        new Verifications() {{
            //noinspection ConstantConditions
            httpURLConnection.setRequestProperty(withCapture(capturedKeys),withCapture(capturedValues));
            times = 8;
        }};

        List<String> expectedKeys = Arrays.asList("x-tru-api-partner-id", "x-tru-api-merchant-id", "x-tru-api-terminal-id", "x-tru-api-encryption-scheme", "x-tru-api-mac", "Accept", "Accept-Charset", "Content-Type");
        List<String> expectedValues = Arrays.asList(partnerId, merchantId, terminalId, "3", "B5B8275399AF817627AD152DBDFA71835D00C0E543DF8D888F5B5EB4C432D1ED18A6C7B474D74790", "application/xml", "UTF-8", "application/xml");

        Assert.assertEquals(expectedKeys,capturedKeys);
        Assert.assertEquals(expectedValues,capturedValues);
    }

}