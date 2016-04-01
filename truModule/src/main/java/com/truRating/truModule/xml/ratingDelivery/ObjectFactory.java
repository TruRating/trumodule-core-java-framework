
package com.truRating.truModule.xml.ratingDelivery;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.truRating.truSharedData.xml.ratingDelivery package.
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.truRating.truSharedData.xml.ratingDelivery
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RatingDeliveryJAXB }
     * 
     */
    public RatingDeliveryJAXB createServiceMessage() {
        return new RatingDeliveryJAXB();
    }

    /**
     * Create an instance of {@link RatingDeliveryJAXB.Languages }
     * 
     */
    public RatingDeliveryJAXB.Languages createServiceMessageLanguages() {
        return new RatingDeliveryJAXB.Languages();
    }

    /**
     * Create an instance of {@link RatingDeliveryJAXB.Rating }
     * 
     */
    public RatingDeliveryJAXB.Rating createServiceMessageRating() {
        return new RatingDeliveryJAXB.Rating();
    }

    /**
     * Create an instance of {@link RatingDeliveryJAXB.Transaction }
     * 
     */
    public RatingDeliveryJAXB.Transaction createServiceMessageTransaction() {
        return new RatingDeliveryJAXB.Transaction();
    }

    /**
     * Create an instance of {@link RatingDeliveryJAXB.CardHash }
     * 
     */
    public RatingDeliveryJAXB.CardHash createServiceMessageCardHash() {
        return new RatingDeliveryJAXB.CardHash();
    }

    /**
     * Create an instance of {@link RatingDeliveryJAXB.Languages.Language }
     * 
     */
    public RatingDeliveryJAXB.Languages.Language createServiceMessageLanguagesLanguage() {
        return new RatingDeliveryJAXB.Languages.Language();
    }

}
