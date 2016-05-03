package com.trurating.xml.genericServiceMessage;

/**
 * Created by Paul on 15/04/2016.
 */
public class ServiceMessageDTO {

    private Object messageContents; //use this to store the JAXB representation of the message
    private int msgType; //use this to store message type as int ref in const - see Constants.java

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public Object getMessageContents() {
        return messageContents;
    }

    public void setMessageContents(Object messageContents) {
        this.messageContents = messageContents;
    }

}
