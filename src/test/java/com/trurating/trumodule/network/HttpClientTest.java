
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

package com.trurating.trumodule.network;

import com.trurating.service.v210.xml.*;
import com.trurating.trumodule.PosParams;
import com.trurating.trumodule.properties.ITruModuleProperties;
import com.trurating.trumodule.properties.TruModulePropertiesSimple;
import mockit.*;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static mockit.Deencapsulation.setField;

@SuppressWarnings("Duplicates")
@RunWith(JMockit.class)
public class HttpClientTest {

    @Tested
    HttpClient httpClient;
    @Injectable
    private Logger logger;
    @Mocked
    HttpURLConnection httpURLConnectionMock;
    //@Injectable
    private IMarshaller marshaller;

    private ITruModuleProperties properties;
    private PosParams posParams;
    private Request request;

    private String terminalId;
    private String merchantId;
    private String sessionId;


    @Before
    public void setUp() throws MalformedURLException {
        properties = new TruModulePropertiesSimple("UTEST_PID_1", "UTEST_MID_1","UTEST_TID_1"); // Set of test properties
        marshaller = new JAXBMarshallSerialiser();
        httpClient = new HttpClient(properties,marshaller);

        setField(httpClient, "log", logger);

        terminalId = "UTEST_TID_1";
        merchantId = "UTEST_MID_1";
        sessionId = "001";

        request = generateRequest();
    }

    @Test
    public void sendURLString_testBody() throws Exception {

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

        }};

        URL url = new URL(null, properties.getTruServiceURL().toString(), stubURLStreamHandler);
        httpClient.send(url, request);

        Assert.assertEquals(testRequestData,byteArrayOutputStream.toString());
    }

    @Test
    public void sendURLString_testFail500() throws Exception {

        URLStreamHandler stubURLStreamHandler = new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL u ) throws IOException {
                return httpURLConnectionMock ;
            }
        };
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        new Expectations() {{
            httpURLConnectionMock.getOutputStream();
            returns(byteArrayOutputStream);

            httpURLConnectionMock.getResponseCode();
            returns(HttpURLConnection.HTTP_BAD_GATEWAY);


        }};

        URL url = new URL(null,properties.getTruServiceURL().toString(),stubURLStreamHandler);
        Assert.assertNull(httpClient.send(url, request)); //good test - added some code to fix.
    }

    @Test
    public void sendURLPosParamsString_testHeadersWithTransportKey() throws Exception {

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

        }};

        final List<String> capturedKeys = new ArrayList<String>();
        final List<String> capturedValues = new ArrayList<String>();

        URL url = new URL(null,properties.getTruServiceURL().toString(),stubURLStreamHandler);

        request.setSessionId(posParams.getSessionId());

        httpClient.send(url ,request);

        new Verifications() {{
           httpURLConnectionMock.setRequestProperty(withCapture(capturedKeys),withCapture(capturedValues));
        }};

        List<String> expectedKeys = Arrays.asList("x-tru-api-partner-id", "x-tru-api-merchant-id", "x-tru-api-terminal-id", "x-tru-api-encryption-scheme", "x-tru-api-mac", "Accept", "Accept-Charset", "Content-Type");
        List<String> expectedValues = Arrays.asList("UTEST_PID_1", "UTEST_MID_1", "UTEST_TID_1", "3", "8364AB252A7E44AC3F5BDA0D1AFB13415F9BCEF96CC0CAFD55E0430B0228C6261D01C4CAEE29F400", "application/xml", "UTF-8", "application/xml");

        Assert.assertEquals(expectedKeys,capturedKeys);
        Assert.assertEquals(expectedValues,capturedValues);
    }

    @Test
    public void sendURLRequest_testBody() throws Exception {
        new MockUp<ByteArrayInputStream>(){
            @Mock void $init(byte[] input) {}
        };

        URLStreamHandler stubURLStreamHandler = new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL u ) throws IOException {
                return httpURLConnectionMock ;
            }
        };
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);

        new Expectations() {{
            httpURLConnectionMock.getOutputStream();
            returns(byteArrayOutputStream);

            httpURLConnectionMock.getResponseCode();
            returns(HttpURLConnection.HTTP_OK);

            httpURLConnectionMock.getInputStream();
            returns(byteArrayInputStream);

        }};

        httpClient.send(new URL(null,properties.getTruServiceURL().toString(),stubURLStreamHandler), request);

        Assert.assertEquals(testRequestData,byteArrayOutputStream.toString());
    }

    @Test
    public void sendURLString_testResponse() throws Exception {

        URLStreamHandler stubURLStreamHandler = new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL u ) throws IOException {
                return httpURLConnectionMock ;
            }
        };
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(testResponseData.getBytes("UTF-8"));

        new Expectations() {{
            httpURLConnectionMock.getOutputStream();
            returns(byteArrayOutputStream);

            httpURLConnectionMock.getResponseCode();
            returns(HttpURLConnection.HTTP_OK);

            httpURLConnectionMock.getInputStream();
            returns(byteArrayInputStream);

        }};

        Response response = httpClient.send(new URL(null,properties.getTruServiceURL().toString(),stubURLStreamHandler),request);

        Assert.assertEquals("en-GB",response.getDisplay().getLanguage().get(0).getRfc1766());
        Assert.assertEquals("truRating:Please customerBeginsTransactionAtTillPoint the product range from 0-9 or clear",response.getDisplay().getLanguage().get(0).getQuestion().getValue());
        Assert.assertEquals("Thanks for rating!",response.getDisplay().getLanguage().get(0).getScreen().get(0).getValue());
        Assert.assertEquals("RATED",response.getDisplay().getLanguage().get(0).getScreen().get(0).getWhen().value());
        Assert.assertEquals(3000,response.getDisplay().getLanguage().get(0).getScreen().get(0).getTimeoutMs());
        Assert.assertEquals("Sorry you didn't customerBeginsTransactionAtTillPoint",response.getDisplay().getLanguage().get(0).getScreen().get(1).getValue());
        Assert.assertEquals("NOTRATED",response.getDisplay().getLanguage().get(0).getScreen().get(1).getWhen().value());
        Assert.assertEquals(3000,response.getDisplay().getLanguage().get(0).getScreen().get(1).getTimeoutMs());
        Assert.assertEquals("************************************************   Thanks for rating!     Go to trurating.com          for ratings           you can trust.     ************************************************",response.getDisplay().getLanguage().get(0).getReceipt().get(0).getValue());
        Assert.assertEquals("RATED",response.getDisplay().getLanguage().get(0).getReceipt().get(0).getWhen().value());
        Assert.assertEquals("CUSTOMER",response.getDisplay().getLanguage().get(0).getReceipt().get(0).getType().value());
        Assert.assertEquals("************************************************   Sorry you didn't customerBeginsTransactionAtTillPoint     Go to trurating.com          for ratings           you can trust.     ************************************************",response.getDisplay().getLanguage().get(0).getReceipt().get(1).getValue());
        Assert.assertEquals("NOTRATED",response.getDisplay().getLanguage().get(0).getReceipt().get(1).getWhen().value());
        Assert.assertEquals("CUSTOMER",response.getDisplay().getLanguage().get(0).getReceipt().get(1).getType().value());
    }

    private final String testRequestData = "<Request PartnerId=\"UTEST_PID_1\" MerchantId=\"UTEST_MID_1\" TerminalId=\"UTEST_TID_1\" SessionId=\"001\" xmlns=\"http://docs.trurating.com/schema/truservice/v210.xsd\">\n" +
            "    <Question Trigger=\"DWELLTIME\">\n" +
            "        <Language Rfc1766=\"en-GB\"/>\n" +
            "        <Language Rfc1766=\"en-FR\"/>\n" +
            "        <Device Name=\"MX925\" Firmware=\"FORMAGENT 2.0\">\n" +
            "            <Screen Height=\"4\" Width=\"64\" Unit=\"LINE\" Format=\"RAW\" Separator=\"\n\" Font=\"MONOSPACED\"/>\n" +
            "            <Receipt Height=\"30\" Width=\"255\" Unit=\"LINE\" Format=\"RAW\" Separator=\"\n\" Font=\"MONOSPACED\"/>\n" +
            "        </Device>\n" +
            "        <Server Id=\"14839345987423827P\" Firmware=\"MCM.42\"/>\n" +
            "    </Question>\n" +
            "    <PosEvent>\n" +
            "        <StartTilling/>\n" +
            "    </PosEvent>\n" +
            "</Request>";

    private final String testPosEventRequestData = "<Request PartnerId=\"UTEST_PID_1\" MerchantId=\"UTEST_MID_1\" TerminalId=\"UTEST_TID_1\" SessionId=\"001\" xmlns=\"http://docs.trurating.com/schema/truservice/v210.xsd\">\n" +
            "    <Question Trigger=\"DWELLTIME\">\n" +
            "        <Language Rfc1766=\"en-GB\"/>\n" +
            "        <Language Rfc1766=\"en-FR\"/>\n" +
            "        <Device Name=\"MX925\" Firmware=\"FORMAGENT 2.0\">\n" +
            "            <Screen Height=\"4\" Width=\"64\" Unit=\"LINE\" Format=\"RAW\" Separator=\"\n\" Font=\"MONOSPACED\"/>\n" +
            "            <Receipt Height=\"30\" Width=\"255\" Unit=\"LINE\" Format=\"RAW\" Separator=\"\n\" Font=\"MONOSPACED\"/>\n" +
            "        </Device>\n" +
            "        <Server Id=\"14839345987423827P\" Firmware=\"MCM.42\"/>\n" +
            "    </Question>\n" +
            "    <PosEvent>\n" +
            "        <StartTilling/>\n" +
            "    </PosEvent>\n" +
            "</Request>";

    private final String testResponseData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<Response PartnerId=\"UTEST_PID_1\" MerchantId=\"UTEST_MID_1\" TerminalId=\"UTEST_TID_1\" SessionId=\"2349572348\" xmlns=\"http://docs.trurating.com/schema/truservice/v210.xsd\">\n" +
            "    <Display>\n" +
            "      <Language Rfc1766=\"en-GB\">\n" +
            "        <Question TimeoutMs=\"30000\">truRating:Please customerBeginsTransactionAtTillPoint the product range from 0-9 or clear</Question>\n" +
            "        <Screen TimeoutMs=\"3000\" When=\"RATED\" Priority=\"false\">Thanks for rating!</Screen>\n" +
            "        <Screen TimeoutMs=\"3000\" When=\"NOTRATED\" Priority=\"false\">Sorry you didn't customerBeginsTransactionAtTillPoint</Screen>\n" +
            "        <Receipt When=\"RATED\" Priority=\"false\" Type=\"CUSTOMER\">************************************************   Thanks for rating!     Go to trurating.com          for ratings           you can trust.     ************************************************</Receipt>\n" +
            "        <Receipt When=\"NOTRATED\" Priority=\"false\" Type=\"CUSTOMER\">************************************************   Sorry you didn't customerBeginsTransactionAtTillPoint     Go to trurating.com          for ratings           you can trust.     ************************************************</Receipt>\n" +
            "      </Language>\n" +
            "    </Display>\n" +
            "</Response>";

    private Request generateRequest(){
        posParams = new PosParams(sessionId);
        request = new Request();
        request.setTerminalId(terminalId);
        request.setMerchantId(merchantId);
        request.setPartnerId(properties.getPartnerId());
        request.setSessionId(sessionId);

        RequestQuestion requestQuestion = new RequestQuestion();
        requestQuestion.setTrigger(Trigger.DWELLTIME);
        RequestLanguage language1 = new RequestLanguage();
        language1.setRfc1766("en-GB");
        RequestLanguage language2 = new RequestLanguage();
        language2.setRfc1766("en-FR");
        requestQuestion.getLanguage().add(language1);
        requestQuestion.getLanguage().add(language2);
        RequestDevice device = new RequestDevice();
        device.setName("MX925");
        device.setFirmware("FORMAGENT 2.0");
        RequestPeripheral screen = new RequestPeripheral();
        screen.setHeight(new Short("4"));
        screen.setWidth(new Short("64"));
        screen.setFormat(Format.RAW);
        screen.setFont(Font.MONOSPACED);
        screen.setSeparator("\n");
        screen.setUnit(UnitDimension.LINE);
        device.setScreen(screen);
        RequestPeripheral printer = new RequestPeripheral();
        printer.setHeight(new Short("30"));
        printer.setWidth(new Short("255"));
        printer.setFormat(Format.RAW);
        printer.setFont(Font.MONOSPACED);
        printer.setSeparator("\n");
        printer.setUnit(UnitDimension.LINE);
        device.setReceipt(printer);
        requestQuestion.setDevice(device);
        RequestServer server = new RequestServer();
        server.setId("14839345987423827P");
        server.setFirmware("MCM.42");
        requestQuestion.setServer(server);

        request.setQuestion(requestQuestion);
        RequestPosEvent event = new RequestPosEvent();
        event.setStartTilling(new RequestPosStartTilling());
        request.setPosEvent(event);

        return request;
    }

}