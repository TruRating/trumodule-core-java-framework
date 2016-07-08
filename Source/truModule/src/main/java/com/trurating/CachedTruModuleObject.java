package com.trurating;

import com.trurating.service.v200.xml.Request;
import com.trurating.service.v200.xml.RequestRating;
import com.trurating.service.v200.xml.RequestTransaction;
import com.trurating.service.v200.xml.Response;

import java.util.Date;

/**
 * Created by Paul on 02/06/2016.
 */
public class CachedTruModuleObject {

    /*
    this is a set of caches of various data held over throughout the trumodule scenario.
     */
    public CachedTruModuleObject() {
        sessionID = Long.toString(new Date().getTime());
        rating = new RequestRating();
        rating.setValue(TruModule.NO_VALUE);
    }

    public RequestRating rating; //the actual rating object
    public boolean cancelled = false; //this is used to flag that a cancellation has come in - i.e. something else wants to use the ped. In this case all further actions are skipped in the module.
    public RequestTransaction transaction; //holds the transaction that is started on a call to doRating()
    public String sessionID; //an id that will persist throughout a single cycle of getQuestion, rate and deliver
    public String question; //the question text: isolated here, like the following four fields, for ease of access
    public String responseNoRating; //screen response that is associated with a particular language
    public String responseWithRating;
    public String receiptNoRating; //receipt response that is associated with a particular language
    public String receiptWithRating;
    public Response response; //entire objects as received from the service
    public Request request;
}