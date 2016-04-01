
package com.truRating.truModule.xml.ratingResponse;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.truRating.truSharedData.xml.ratingResponse package.
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.truRating.truSharedData.xml.ratingResponse
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RatingResponseJAXB }
     * 
     */
    public RatingResponseJAXB createServiceMessage() {
        return new RatingResponseJAXB();
    }

    /**
     * Create an instance of {@link RatingResponseJAXB.Languages }
     * 
     */
    public RatingResponseJAXB.Languages createServiceMessageLanguages() {
        return new RatingResponseJAXB.Languages();
    }

    /**
     * Create an instance of {@link RatingResponseJAXB.Languages.Language }
     * 
     */
    public RatingResponseJAXB.Languages.Language createServiceMessageLanguagesLanguage() {
        return new RatingResponseJAXB.Languages.Language();
    }

    /**
     * Create an instance of {@link RatingResponseJAXB.Languages.Language.Receipt }
     * 
     */
    public RatingResponseJAXB.Languages.Language.Receipt createServiceMessageLanguagesLanguageReceipt() {
        return new RatingResponseJAXB.Languages.Language.Receipt();
    }

}
