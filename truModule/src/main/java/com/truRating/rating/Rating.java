package com.trurating.rating;



/**
 * Created by Paul on 08/03/2016.
 */
public class Rating {
    private int value = 0;
    private String timeTakeToRate = "" ;
    private String prizeCode = "" ;
    private String language = "" ;
    private String ratingDateTime = "" ;
    private int questionID = 0 ;

    public String getRatingDateTime() {
        return ratingDateTime;
    }

    public void setRatingDateTime(String ratingDateTime) {
        this.ratingDateTime = ratingDateTime;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID=questionID;
    }

    public String getLanguageCode() {
        return language;
    }

    public void setLanguageCode(String languageCode) {
    	language = languageCode;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getTimeTakeToRate() {
        return timeTakeToRate;
    }

    public void setTimeTakeToRate(String ratingTime) {
        this.timeTakeToRate = ratingTime;
    }
    
    public String getPrizeCode() {
        return prizeCode ;
    }

    public void setPrizeCode(String prizeCode) {
    	this.prizeCode = prizeCode ;
    }
}
