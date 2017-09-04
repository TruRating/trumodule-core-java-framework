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

import com.trurating.service.v230.xml.Request;
import com.trurating.service.v230.xml.RequestPosEvent;
import com.trurating.service.v230.xml.RequestPosStartTilling;
import com.trurating.service.v230.xml.Response;
import com.trurating.trumodule.messages.TruModuleMessageFactory;
import mockit.Tested;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JAXBSerializerTest {
    @Tested
    private
    JAXBSerializer serializer;

    private String partnerId ="1";
    private String merchantId = "UNITTEST_MERCH_1";
    private String terminalId = "UNITTEST_TERM_1";
    private String sessionId = "UNITTEST_SESSION_1";

    @Before
    public void setUp() {
        serializer = new JAXBSerializer();
    }

    @Test
    public void marshall() throws Exception {
        RequestPosEvent event = new RequestPosEvent();
        RequestPosStartTilling posStartTilling = new RequestPosStartTilling();
        event.setStartTilling(posStartTilling);

        Request request = TruModuleMessageFactory.assembleRequestPosEvent(partnerId,merchantId,terminalId,sessionId,event);

        String xml = "<Request PartnerId=\"1\" MerchantId=\"UNITTEST_MERCH_1\" TerminalId=\"UNITTEST_TERM_1\" SessionId=\"UNITTEST_SESSION_1\" xmlns=\"http://docs.trurating.com/schema/truservice/v230.xsd\">\n" +
                "    <PosEvent>\n" +
                "        <StartTilling/>\n" +
                "    </PosEvent>\n" +
                "</Request>";

        Assert.assertEquals(xml,serializer.marshall(request).toString());
    }

    @Test
    public void unMarshall() throws Exception {
        String xml = "<Response PartnerId=\"1\" MerchantId=\"UNITTEST_MERCH_1\" TerminalId=\"UNITTEST_TERM_1\" SessionId=\"UNITTEST_SESSION_1\" xmlns=\"http://docs.trurating.com/schema/truservice/v230.xsd\">\n" +
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

        Response response = serializer.unMarshall(xml);

        Assert.assertEquals(partnerId,response.getPartnerId());
        Assert.assertEquals(merchantId,response.getMerchantId());
        Assert.assertEquals(terminalId,response.getTerminalId());
        Assert.assertEquals(sessionId,response.getSessionId());
        Assert.assertEquals("en-GB",response.getDisplay().getLanguage().get(0).getRfc1766());
        Assert.assertEquals("||||Please rate the menu|from 0-9 or clear",response.getDisplay().getLanguage().get(0).getQuestion().getValue());
    }

}