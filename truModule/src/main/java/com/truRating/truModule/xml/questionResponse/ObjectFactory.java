
package com.truRating.truModule.xml.questionResponse;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.truRating.truSharedData.xml.questionResponse package.
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.truRating.truSharedData.xml.questionResponse
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link QuestionResponseJAXB }
     * 
     */
    public QuestionResponseJAXB createServiceMessage() {
        return new QuestionResponseJAXB();
    }

    /**
     * Create an instance of {@link QuestionResponseJAXB.Languages }
     * 
     */
    public QuestionResponseJAXB.Languages createServiceMessageLanguages() {
        return new QuestionResponseJAXB.Languages();
    }

    /**
     * Create an instance of {@link QuestionResponseJAXB.Languages.Language }
     * 
     */
    public QuestionResponseJAXB.Languages.Language createServiceMessageLanguagesLanguage() {
        return new QuestionResponseJAXB.Languages.Language();
    }

    /**
     * Create an instance of {@link QuestionResponseJAXB.Languages.Language.DisplayElements }
     * 
     */
    public QuestionResponseJAXB.Languages.Language.DisplayElements createServiceMessageLanguagesLanguageDisplayElements() {
        return new QuestionResponseJAXB.Languages.Language.DisplayElements();
    }

    /**
     * Create an instance of {@link QuestionResponseJAXB.Languages.Language.Receipt }
     * 
     */
    public QuestionResponseJAXB.Languages.Language.Receipt createServiceMessageLanguagesLanguageReceipt() {
        return new QuestionResponseJAXB.Languages.Language.Receipt();
    }

    /**
     * Create an instance of {@link QuestionResponseJAXB.Languages.Language.DisplayElements.Question }
     * 
     */
    public QuestionResponseJAXB.Languages.Language.DisplayElements.Question createServiceMessageLanguagesLanguageDisplayElementsQuestion() {
        return new QuestionResponseJAXB.Languages.Language.DisplayElements.Question();
    }

    /**
     * Create an instance of {@link QuestionResponseJAXB.Languages.Language.DisplayElements.Prize }
     * 
     */
    public QuestionResponseJAXB.Languages.Language.DisplayElements.Prize createServiceMessageLanguagesLanguageDisplayElementsPrize() {
        return new QuestionResponseJAXB.Languages.Language.DisplayElements.Prize();
    }

    /**
     * Create an instance of {@link QuestionResponseJAXB.Languages.Language.DisplayElements.Acknowledgement }
     * 
     */
    public QuestionResponseJAXB.Languages.Language.DisplayElements.Acknowledgement createServiceMessageLanguagesLanguageDisplayElementsAcknowledgement() {
        return new QuestionResponseJAXB.Languages.Language.DisplayElements.Acknowledgement();
    }

}
