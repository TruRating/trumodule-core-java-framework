
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

package com.trurating.trumodule.util;

import com.trurating.service.v210.xml.*;
import com.trurating.trumodule.device.IDevice;
import com.trurating.trumodule.device.IPeripheralCapabilities;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class TruRatingMessageFactoryTest {
    @Mocked
    private IDevice iDevice;
    @Mocked
    private IPeripheralCapabilities screenCapabilities;
    @Mocked
    private IPeripheralCapabilities receiptCapabilities;

    private String partnerId;
    private String merchantId;
    private String terminalId;
    private String sessionId;
    private String rfc1766;
    private int responseTimeMs;

    private TruRatingMessageFactory factory;

    @Before
    public void setUp() throws Exception {
        partnerId = "1";
        merchantId = "0987654321";
        terminalId = "0123456789";
        sessionId = "546787654";

        rfc1766 = "en-GB";
        responseTimeMs = 250;

        factory = new TruRatingMessageFactory();
    }

    @Test
    public void assembleRequestTransaction() throws Exception {
        RequestTransaction requestTransaction = new RequestTransaction();
        requestTransaction.setId("12554");
        requestTransaction.setAmount(1234);

        Request request = factory.assembleRequestTransaction(partnerId,merchantId,terminalId,sessionId,requestTransaction);
        Assert.assertNotNull(request);
        Assert.assertEquals(partnerId,request.getPartnerId());
        Assert.assertEquals(merchantId,request.getMerchantId());
        Assert.assertEquals(terminalId,request.getTerminalId());
        Assert.assertEquals(sessionId,request.getSessionId());
        Assert.assertEquals(1234,request.getTransaction().getAmount());
    }

    @Test
    public void assembleRatingsDeliveryRequest() throws Exception {
        Request request = factory.assembleRatingsDeliveryRequest(rfc1766,partnerId,merchantId,terminalId,sessionId,(short)responseTimeMs,responseTimeMs);
        Assert.assertEquals(partnerId,request.getPartnerId());
        Assert.assertEquals(merchantId,request.getMerchantId());
        Assert.assertEquals(terminalId,request.getTerminalId());
        Assert.assertEquals(sessionId,request.getSessionId());
    }

    @Test
    public void assembleQuestionRequest() throws Exception {

        new Expectations() {{
            iDevice.getFirmware();
            returns("7.5.3-b56");
            times = 1;
            iDevice.getName();
            returns("Name1");
            times = 1;

            screenCapabilities.getFont();
            returns(Font.fromValue("MONOSPACED"));
            times = 1;
            screenCapabilities.getHeight();
            returns((short)3);
            times = 1;
            screenCapabilities.getUnit();
            returns(UnitDimension.fromValue("LINE"));
            times = 1;
            screenCapabilities.getSeparator();
            returns("\n");
            times = 1;
            screenCapabilities.getWidth();
            returns((short)20);
            times = 1;

            iDevice.getScreenCapabilities();
            returns(screenCapabilities);
            //times = 5;

            iDevice.getReceiptCapabilities();
            returns(null);
        }};


        Request request = factory.assembleQuestionRequest(rfc1766,iDevice,partnerId,merchantId,terminalId,sessionId,Trigger.fromValue("CARDINSERTION"));

        Assert.assertEquals(partnerId,request.getPartnerId());
        Assert.assertEquals(merchantId,request.getMerchantId());
        Assert.assertEquals(terminalId,request.getTerminalId());
        Assert.assertEquals(sessionId,request.getSessionId());
        Assert.assertEquals(Font.fromValue("MONOSPACED"),request.getQuestion().getDevice().getScreen().getFont());
        Assert.assertEquals(new Short("3"),request.getQuestion().getDevice().getScreen().getHeight());
        Assert.assertEquals(new Short("20"),request.getQuestion().getDevice().getScreen().getWidth());
        Assert.assertNull(request.getQuestion().getDevice().getReceipt());

    }

    @Test
    public void assembleQuestionRequest_withReceiptCapability() throws Exception {

        new Expectations() {{
            iDevice.getFirmware();
            returns("7.5.3-b56");
            times = 1;
            iDevice.getName();
            returns("Name1");
            times = 1;

            screenCapabilities.getFont();
            returns(Font.fromValue("MONOSPACED"));
            times = 1;
            screenCapabilities.getHeight();
            returns((short)3);
            times = 1;
            screenCapabilities.getUnit();
            returns(UnitDimension.fromValue("LINE"));
            times = 1;
            screenCapabilities.getSeparator();
            returns("\n");
            times = 1;
            screenCapabilities.getWidth();
            returns((short)20);
            times = 1;

            iDevice.getScreenCapabilities();
            returns(screenCapabilities);
           // times = 2;

            receiptCapabilities.getFont();
            returns(Font.fromValue("MONOSPACED"));
            times = 1;
            receiptCapabilities.getHeight();
            returns((short)12);
            times = 1;
            receiptCapabilities.getUnit();
            returns(UnitDimension.fromValue("LINE"));
            times = 1;
            receiptCapabilities.getSeparator();
            returns("\n");
            times = 1;
            receiptCapabilities.getWidth();
            returns((short)15);
            times = 1;

            iDevice.getReceiptCapabilities();
            returns(receiptCapabilities);
          //  times = 1;
        }};


        Request request = factory.assembleQuestionRequest(rfc1766,iDevice,partnerId,merchantId,terminalId,sessionId,Trigger.fromValue("CARDINSERTION"));

        Assert.assertEquals(partnerId,request.getPartnerId());
        Assert.assertEquals(merchantId,request.getMerchantId());
        Assert.assertEquals(terminalId,request.getTerminalId());
        Assert.assertEquals(sessionId,request.getSessionId());
        Assert.assertEquals(Font.fromValue("MONOSPACED"),request.getQuestion().getDevice().getScreen().getFont());
        Assert.assertEquals(new Short("3"),request.getQuestion().getDevice().getScreen().getHeight());
        Assert.assertEquals(new Short("20"),request.getQuestion().getDevice().getScreen().getWidth());

        Assert.assertEquals(Font.fromValue("MONOSPACED"),request.getQuestion().getDevice().getReceipt().getFont());
        Assert.assertEquals(new Short("12"),request.getQuestion().getDevice().getReceipt().getHeight());
        Assert.assertEquals(new Short("15"),request.getQuestion().getDevice().getReceipt().getWidth());
    }
}