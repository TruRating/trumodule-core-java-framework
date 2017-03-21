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

import com.trurating.service.v220.xml.*;
import com.trurating.trumodule.messages.TruModuleMessageFactory;
import com.trurating.trumodule.properties.ITruModuleProperties;
import mockit.*;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import static org.junit.Assert.*;

@RunWith(JMockit.class)
public class HttpClientTest {
    @Tested
    private
    HttpClient httpClient;

    @Mocked
    private
    TruServiceHttpClient truServiceHttpClient;
    @Mocked
    private
    ITruModuleProperties properties;
    @Mocked
    private
    ISerializer serializer;
    @Mocked
    private
    HttpURLConnection httpURLConnectionMock;

    private String partnerId ="1";
    private String merchantId = "UNITTEST_MERCH_1";
    private String terminalId = "UNITTEST_TERM_1";
    private String sessionId = "UNITTEST_SESSION_1";

    @Before
    public void setUp() {
        httpClient = new HttpClient(properties,serializer);
        Deencapsulation.setField(httpClient, "truServiceHttpClient", truServiceHttpClient);
    }

    @Test
    public void send() throws Exception {
        RequestPosEvent event = new RequestPosEvent();
        RequestPosStartTilling posStartTilling = new RequestPosStartTilling();
        event.setStartTilling(posStartTilling);

        final Request request = TruModuleMessageFactory.assembleRequestPosEvent(partnerId,merchantId,terminalId,sessionId,event);

        final String testRequestData = "<Request PartnerId=\"1\" MerchantId=\"UNITTEST_MERCH_1\" TerminalId=\"UNITTEST_TERM_1\" SessionId=\"UNITTEST_SESSION_1\" xmlns=\"http://docs.trurating.com/schema/truservice/v220.xsd\">\n" +
                "    <PosEvent>\n" +
                "        <StartTilling/>\n" +
                "    </PosEvent>\n" +
                "</Request>";
        final StringWriter testRequestSW = new StringWriter();
        testRequestSW.write(testRequestData);

        final String testResponseData = "<Response PartnerId=\"1\" MerchantId=\"UNITTEST_MERCH_1\" TerminalId=\"UNITTEST_TERM_1\" SessionId=\"UNITTEST_SESSION_1\" xmlns=\"http://docs.trurating.com/schema/truservice/v220.xsd\">\n" +
                "  <Event>\n" +
                "    <Clear />\n" +
                "  </Event>\n" +
                "</Response>";

        ResponseEventClear responseEventClear = new ResponseEventClear();
        ResponseEvent responseEvent = new ResponseEvent();
        responseEvent.setClear(responseEventClear);
        final Response expectedResponse = new Response();
        expectedResponse.setPartnerId(partnerId);
        expectedResponse.setMerchantId(merchantId);
        expectedResponse.setTerminalId(terminalId);
        expectedResponse.setSessionId(sessionId);
        expectedResponse.setEvent(responseEvent);

        URLStreamHandler stubURLStreamHandler = new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL u ) throws IOException {
                return httpURLConnectionMock ;
            }
        };
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ByteArrayInputStream byteArrayInputStream= new ByteArrayInputStream(testResponseData.getBytes());
        new Expectations() {{
            httpURLConnectionMock.getOutputStream();
            returns(byteArrayOutputStream);

            httpURLConnectionMock.getResponseCode();
            returns(HttpURLConnection.HTTP_OK);

            httpURLConnectionMock.getInputStream();
            returns(byteArrayInputStream);

            truServiceHttpClient.serializeRequest((Request)any);
            returns(testRequestSW);

            truServiceHttpClient.unSerializeResponse(anyString);
            returns(expectedResponse);
        }};

        URL url = new URL(null, "https://tru-sand-service-v2xx.trurating.com/api/servicemessage", stubURLStreamHandler);
        final Response response = httpClient.send(url, request);

        new Verifications() {{
            HttpURLConnection captureConn;
            Request captureReq;
            truServiceHttpClient.addHeaderInformation(captureConn = withCapture(),captureReq = withCapture());
            assertEquals(httpURLConnectionMock,captureConn);
            assertEquals(request,captureReq);
            String captureResponse;
            truServiceHttpClient.unSerializeResponse(captureResponse = withCapture());
            assertEquals(testResponseData,captureResponse);
        }};

        Assert.assertEquals(testRequestData,byteArrayOutputStream.toString());
        Assert.assertEquals(expectedResponse,response);
    }

    @Test
    public void send_fail() throws Exception {
        RequestPosEvent event = new RequestPosEvent();
        RequestPosStartTilling posStartTilling = new RequestPosStartTilling();
        event.setStartTilling(posStartTilling);

        final Request request = TruModuleMessageFactory.assembleRequestPosEvent(partnerId,merchantId,terminalId,sessionId,event);

        final String testRequestData = "<Request PartnerId=\"1\" MerchantId=\"UNITTEST_MERCH_1\" TerminalId=\"UNITTEST_TERM_1\" SessionId=\"UNITTEST_SESSION_1\" xmlns=\"http://docs.trurating.com/schema/truservice/v220.xsd\">\n" +
                "    <PosEvent>\n" +
                "        <StartTilling/>\n" +
                "    </PosEvent>\n" +
                "</Request>";
        final StringWriter testRequestSW = new StringWriter();
        testRequestSW.write(testRequestData);

        URLStreamHandler stubURLStreamHandler = new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL u ) throws IOException {
                return httpURLConnectionMock ;
            }
        };
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ByteArrayInputStream byteArrayInputStream= new ByteArrayInputStream(new byte[0]);
        new Expectations() {{
            httpURLConnectionMock.getOutputStream();
            returns(byteArrayOutputStream);

            httpURLConnectionMock.getResponseCode();
            returns(HttpURLConnection.HTTP_INTERNAL_ERROR);

            httpURLConnectionMock.getInputStream();
            returns(byteArrayInputStream);

            truServiceHttpClient.serializeRequest((Request)any);
            returns(testRequestSW);

        }};

        URL url = new URL(null, "https://tru-sand-service-v2xx.trurating.com/api/servicemessage", stubURLStreamHandler);
        final Response response = httpClient.send(url, request);

        new Verifications() {{
            HttpURLConnection captureConn;
            Request captureReq;
            truServiceHttpClient.addHeaderInformation(captureConn = withCapture(),captureReq = withCapture());
            assertEquals(httpURLConnectionMock,captureConn);
            assertEquals(request,captureReq);
        }};

        Assert.assertNull(response);
    }

}