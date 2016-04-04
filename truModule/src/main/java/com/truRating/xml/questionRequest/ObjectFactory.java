
package com.trurating.xml.questionRequest;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.trurating.shared.xml.questionRequest package.
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.trurating.shared.xml.questionRequest
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link QuestionRequestJAXB }
     * 
     */
    public QuestionRequestJAXB createServiceMessage() {
        return new QuestionRequestJAXB();
    }

    /**
     * Create an instance of {@link QuestionRequestJAXB.Languages }
     * 
     */
    public QuestionRequestJAXB.Languages createServiceMessageLanguages() {
        return new QuestionRequestJAXB.Languages();
    }

    /**
     * Create an instance of {@link QuestionRequestJAXB.DeviceInfo }
     * 
     */
    public QuestionRequestJAXB.DeviceInfo createServiceMessageDeviceInfo() {
        return new QuestionRequestJAXB.DeviceInfo();
    }

    /**
     * Create an instance of {@link QuestionRequestJAXB.ServerInfo }
     * 
     */
    public QuestionRequestJAXB.ServerInfo createServiceMessageServerInfo() {
        return new QuestionRequestJAXB.ServerInfo();
    }

    /**
     * Create an instance of {@link QuestionRequestJAXB.Languages.Language }
     * 
     */
    public QuestionRequestJAXB.Languages.Language createServiceMessageLanguagesLanguage() {
        return new QuestionRequestJAXB.Languages.Language();
    }

}
