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

import com.trurating.service.v210.xml.*;
import com.trurating.trumodule.device.IDevice;
import com.trurating.trumodule.network.IMarshaller;
import com.trurating.trumodule.properties.ITruModuleProperties;

import java.util.HashMap;

public class TruModuleIntegrated extends TruModule implements ITruModuleIntegrated {
    private HashMap<POSEvent,Trigger> triggerMappings;
    private Trigger trigger;

    /**
     * Instantiates a new Tru module integrated.
     *
     * @param truModuleProperties the tru module properties
     */
    @SuppressWarnings("WeakerAccess")
    public TruModuleIntegrated(ITruModuleProperties truModuleProperties) {
        this(truModuleProperties, null);
    }

    /**
     * Instantiates a new Tru module integrated.
     *
     * @param truModuleProperties the tru module properties
     * @param device              the device
     */
    public TruModuleIntegrated(ITruModuleProperties truModuleProperties, IDevice device) {
        this(truModuleProperties, device, null);
    }

    /**
     * Instantiates a new Tru module integrated.
     *
     * @param truModuleProperties the tru module properties
     * @param device              the device
     * @param marshaller          the marshaller
     */
    @SuppressWarnings("WeakerAccess")
    public TruModuleIntegrated(ITruModuleProperties truModuleProperties, IDevice device, IMarshaller marshaller) {
        super(truModuleProperties, device, marshaller);
    }

    public Response sendPosEvent(PosParams params, RequestPosEvent event) {
        POSEvent posEvent = this.convertRequestPosEventToPOSEvent(event);
        if(posEvent != null){
            Trigger triggerEvent = this.getQuestionTriggerEvent(posEvent);
            if(triggerEvent != null && triggerEvent == this.trigger){
                // This event is set as a trigger (ie. payment request)
                this.fetchQuestionAndDoRating(params,triggerEvent);
            }
        }
        Response response = super.sendPosEvent(params,event);
        return this.processResponse(params,response);
    }

    @Override
    public Response sendPosEventList(PosParams params, RequestPosEventList eventList) {
        Response response = super.sendPosEventList(params, eventList);
        return this.processResponse(params,response);
    }

    @Override
    public Response sendTransaction(PosParams params, RequestTransaction requestTransaction) {
        return super.sendTransaction(this.getTruModuleProperties().getMerchantId(),this.getTruModuleProperties().getTerminalId(),requestTransaction);
    }

    private POSEvent convertRequestPosEventToPOSEvent(RequestPosEvent event){
        if(event.getStartTransaction() != null){
            return POSEvent.REQUESTPOSSTARTTRANSACTION;
        }
        if(event.getItemOrDiscount() != null){
            if (event.getItemOrDiscount().size()>0) {
                if (event.getItemOrDiscount().get(0) != null) {
                    if (event.getItemOrDiscount().get(0) instanceof RequestPosItem) {
                        return POSEvent.REQUESTPOSITEM;
                    }
                    if (event.getItemOrDiscount().get(0) instanceof RequestPosItemDiscount) {
                        return POSEvent.REQUESTPOSDISCOUNT;
                    }
                }
            }
            return null;
        }
        if(event.getCustomer() != null){
            return POSEvent.REQUESTCUSTOMER;
        }
        if(event.getEndTilling() != null){
            return POSEvent.REQUESTPOSENDTILLING;
        }
        if(event.getEndTransaction() != null){
            return POSEvent.REQUESTPOSENDTRANSACTION;
        }
        if(event.getReceiptData() != null){
            return POSEvent.REQUESTPOSRECEIPTDATA;
        }
        return null;
    }

    private void fetchQuestionAndDoRating(PosParams params, Trigger trigger){
        Request questionRequest = super.truRatingMessageFactory.assembleQuestionRequest(
                super.getIDevice().getRfc1766LanguageCode(),
                super.getIDevice(),
                super.getTruModuleProperties().getPartnerId(),
                this.getTruModuleProperties().getMerchantId(),
                this.getTruModuleProperties().getTerminalId(),
                super.getSessionId(params),
                trigger);

        Response eventResponse = super.getQuestion(questionRequest);
        if (eventResponse == null) {
            super.getLogger().severe("Response came back as null from truService - exiting");
            return;
        }

        ResponseLanguage responseLanguage = super.filterResponseLanguage(eventResponse, "en-GB"); //filter the correct language type
        if (responseLanguage == null){
            super.getLogger().severe("Response came back as without required language from truService - exiting");
            return;
        }

        super.setReceiptTextCache(super.doRating(questionRequest));
    }

    @SuppressWarnings("WeakerAccess")
    public void setQuestionTriggerEvent(POSEvent event, Trigger trigger){
        if(this.triggerMappings == null){
            this.triggerMappings = new HashMap<POSEvent, Trigger>();
        }
        this.triggerMappings.put(event,trigger);
    }

    private Trigger getQuestionTriggerEvent(POSEvent event){
        if(this.triggerMappings == null){
            return null;
        }
        return this.triggerMappings.get(event);
    }

    private Response processResponse(PosParams params, Response response){
        if ((response != null) && (response.getEvent() != null) && (response.getEvent().getQuestion() != null) && (response.getEvent().getQuestion().getTrigger() != null)) {
            this.trigger = response.getEvent().getQuestion().getTrigger();
            if(this.trigger == Trigger.DWELLTIME || this.trigger == Trigger.DWELLTIMEEXTEND){
                // Request question now
                this.fetchQuestionAndDoRating(params,trigger);
            }
        }
        return response;
    }
}
