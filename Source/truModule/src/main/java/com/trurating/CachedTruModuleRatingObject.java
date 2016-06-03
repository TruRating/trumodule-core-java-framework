package com.trurating;

import com.trurating.service.v200.xml.Request;
import com.trurating.service.v200.xml.RequestRating;
import com.trurating.service.v200.xml.Response;

import java.util.Date;

/**
 * Created by Paul on 02/06/2016.
 */
public class CachedTruModuleRatingObject {

    public CachedTruModuleRatingObject() {
        sessionID = Long.toString(new Date().getTime());
        rating = new RequestRating();
        rating.setValue(TruModule.NO_RATING_VALUE);
    }

    public RequestRating rating;
    public String sessionID;
    public String question;
    public String responseNoRating;
    public String responseWithRating;
    public String receiptNoRating;
    public String receiptWithRating;
    public Response response;
    public Request request;
}