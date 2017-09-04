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

import com.trurating.service.v230.xml.*;
import com.trurating.trumodule.device.IDevice;
import com.trurating.trumodule.device.IReceiptManager;
import com.trurating.trumodule.messages.PosParams;
import com.trurating.trumodule.messages.TruModuleMessageFactory;
import com.trurating.trumodule.network.ISerializer;
import com.trurating.trumodule.properties.ITruModuleProperties;


/**
 * The type Tru module integrated.
 */
public class TruModuleIntegrated extends TruModule implements ITruModuleIntegrated {
    private final Object sendPosEventLock = new Object();
    private final Object sendPosEventListLock = new Object();
    private final Object sendTransactionLock = new Object();


    /**
     * Instantiates a new Tru module integrated.
     *
     * @param truModuleProperties the tru module properties
     */
    @SuppressWarnings("unused")
    public TruModuleIntegrated(ITruModuleProperties truModuleProperties) {
        this(truModuleProperties, null, null);
    }

    /**
     * Instantiates a new Tru module integrated.
     *
     * @param truModuleProperties the tru module properties
     * @param receiptManager      the receipt manager
     * @param device              the device
     */
    @SuppressWarnings("WeakerAccess")
    public TruModuleIntegrated(ITruModuleProperties truModuleProperties, IReceiptManager receiptManager, IDevice device) {
        this(truModuleProperties, receiptManager, device, null);
    }

    /**
     * Instantiates a new Tru module integrated.
     *
     * @param truModuleProperties the tru module properties
     * @param receiptManager      the receipt manager
     * @param device              the device
     * @param marshaller          the marshaller
     */
    @SuppressWarnings({"SameParameterValue", "WeakerAccess"})
    public TruModuleIntegrated(ITruModuleProperties truModuleProperties, IReceiptManager receiptManager, IDevice device, ISerializer marshaller) {
        super(truModuleProperties, receiptManager, device, marshaller);
    }

    /**
     * Instantiates a new Tru module integrated.
     *
     * @param truModuleProperties  the tru module properties
     * @param receiptManager       the receipt manager
     * @param device               the device
     * @param marshaller           the marshaller
     * @param deferActivationCheck the defer activation check
     */
    @SuppressWarnings("WeakerAccess")
    public TruModuleIntegrated(ITruModuleProperties truModuleProperties, IReceiptManager receiptManager, IDevice device, ISerializer marshaller, boolean deferActivationCheck) {
        super(truModuleProperties, receiptManager, device, marshaller, deferActivationCheck);
    }

    public void sendPosEvent(final PosParams params, final RequestPosEvent event) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendPosEventImpl(params, event);
            }
        }).start();
    }

    /**
     * Send pos event.
     *
     * @param params the params
     * @param event  the event
     */
    void sendPosEventImpl(final PosParams params, final RequestPosEvent event) {
        synchronized (sendPosEventLock) {
            if (super.isActivated()) {
                Response response = super.sendRequest(TruModuleMessageFactory.assembleRequestPosEvent(super.getTruModuleProperties().getPartnerId(), super.getTruModuleProperties().getMerchantId(), super.getTruModuleProperties().getTerminalId(), super.getSessionId(params), event));
                this.processResponse(params, response);
            }
        }
    }

    public void sendPosEventList(final PosParams params, final RequestPosEventList eventList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendPosEventListImpl(params, eventList);
            }
        }).start();
    }

    /**
     * Send pos event list.
     *
     * @param params    the params
     * @param eventList the event list
     */
    void sendPosEventListImpl(final PosParams params, final RequestPosEventList eventList) {
        synchronized (sendPosEventListLock) {
            if (super.isActivated()) {
                Response response = super.sendRequest(TruModuleMessageFactory.assembleRequestPosEvent(super.getTruModuleProperties().getPartnerId(), super.getTruModuleProperties().getMerchantId(), super.getTruModuleProperties().getTerminalId(), super.getSessionId(params), eventList));
                this.processResponse(params, response);
            }
        }
    }

    public void sendTransaction(final PosParams params, final RequestTransaction requestTransaction) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendTransactionImpl(params, requestTransaction);
            }
        }).start();
    }

    /**
     * Send transaction.
     *
     * @param params             the params
     * @param requestTransaction the request transaction
     */
    void sendTransactionImpl(final PosParams params, final RequestTransaction requestTransaction) {
        synchronized (sendTransactionLock) {
            if (super.isActivated()) {
                this.sendTransaction(this.getSessionId(params), requestTransaction);
            }
        }
    }

    public void initiatePayment(PosParams params) {
        if (super.isActivated()) {
            if (super.getTrigger() == Trigger.PAYMENTREQUEST) {
                this.assembleRequestAndDoRating(params, super.getTrigger());
            } else {
                super.cancelRating();
            }
        }
    }

    private void assembleRequestAndDoRating(PosParams params, Trigger trigger) {
        Request request = TruModuleMessageFactory.assembleQuestionRequest(
                super.getIDevice().getCurrentLanguage(),
                super.getIDevice(),
                super.getIReceiptManager(),
                super.getTruModuleProperties().getPartnerId(),
                super.getTruModuleProperties().getMerchantId(),
                super.getTruModuleProperties().getTerminalId(),
                super.getSessionId(params),
                trigger);

        super.doRating(request);
    }

    private void processResponse(PosParams params, Response response) {
        if ((response != null) && (response.getEvent() != null)) {
            if ((response.getEvent().getQuestion() != null) && (response.getEvent().getQuestion().getTrigger() != null)) {
                super.setTrigger(response.getEvent().getQuestion().getTrigger());
                if (super.getTrigger() == Trigger.DWELLTIME || super.getTrigger() == Trigger.DWELLTIMEEXTEND) {
                    // Request question now
                    this.assembleRequestAndDoRating(params, super.getTrigger());
                }
            } else if (response.getEvent().getClear() != null) {
                super.cancelRating();
            }
        }
    }
}
