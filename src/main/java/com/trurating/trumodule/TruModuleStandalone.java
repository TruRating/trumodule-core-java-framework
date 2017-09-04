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

import com.trurating.service.v230.xml.Request;
import com.trurating.service.v230.xml.RequestTransaction;
import com.trurating.service.v230.xml.Trigger;
import com.trurating.trumodule.device.IDevice;
import com.trurating.trumodule.device.IReceiptManager;
import com.trurating.trumodule.messages.TruModuleMessageFactory;
import com.trurating.trumodule.network.ISerializer;
import com.trurating.trumodule.properties.ITruModuleProperties;
import com.trurating.trumodule.util.TruModuleDateUtils;

/**
 * The type TruModule standalone.
 */
public class TruModuleStandalone extends TruModule implements ITruModuleStandalone {
    private final Object sendTransactionLock = new Object();

    /**
     * Instantiates a new Tru module standalone.
     *
     * @param truModuleProperties the tru module properties
     */
    @SuppressWarnings("unused")
    public TruModuleStandalone(ITruModuleProperties truModuleProperties) {
        this(truModuleProperties, null, null);
    }

    /**
     * Instantiates a new Tru module standalone.
     *
     * @param truModuleProperties the tru module properties
     * @param receiptManager      the receipt manager
     * @param device              the device
     */
    @SuppressWarnings("WeakerAccess")
    public TruModuleStandalone(ITruModuleProperties truModuleProperties, IReceiptManager receiptManager, IDevice device) {
        this(truModuleProperties, receiptManager, device, null, false);
    }

    /**
     * Instantiates a new Tru module standalone.
     *
     * @param truModuleProperties  the tru module properties
     * @param receiptManager       the receipt manager
     * @param device               the device
     * @param marshaller           the marshaller
     * @param deferActivationCheck the defer activation check
     */
    @SuppressWarnings({"SameParameterValue", "WeakerAccess"})
    public TruModuleStandalone(ITruModuleProperties truModuleProperties, IReceiptManager receiptManager, IDevice device, ISerializer marshaller, boolean deferActivationCheck) {
        super(truModuleProperties, receiptManager, device, marshaller, deferActivationCheck);
    }

    public void doRating() {
        setSessionId(Long.toString(TruModuleDateUtils.getInstance().timeNowMillis()));
        if (super.isActivated()) {
            Request request = TruModuleMessageFactory.assembleQuestionRequest(
                    getIDevice().getCurrentLanguage(),
                    getIDevice(),
                    getIReceiptManager(),
                    getTruModuleProperties().getPartnerId(),
                    getTruModuleProperties().getMerchantId(),
                    getTruModuleProperties().getTerminalId(),
                    getSessionId(null),
                    Trigger.PAYMENTREQUEST);

            super.doRating(request);
        }
    }

    public void sendTransaction(final RequestTransaction requestTransaction) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendTransactionImpl(requestTransaction);
            }
        }).start();
    }

    /**
     * Send transaction.
     *
     * @param requestTransaction the request transaction
     */
    void sendTransactionImpl(final RequestTransaction requestTransaction) {
        synchronized (sendTransactionLock) {
            String sessionId = super.getSessionId();
            if (super.isActivated()) {
                sendTransaction(sessionId, requestTransaction);
            }
            this.setSessionId(null);
        }
    }
}
