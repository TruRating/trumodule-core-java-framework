
package com.trurating.xml.questionResponse;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Languages" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Language" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="DisplayElements">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="Question">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;attribute name="qid" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
 *                                               &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                               &lt;attribute name="questiontimeout" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" />
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="Prize" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;attribute name="prizecode" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                               &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                               &lt;attribute name="prizetimeout" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" />
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="Acknowledgement" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;attribute name="ratedvalue" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                               &lt;attribute name="notratedvalue" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                               &lt;attribute name="acknowledgetimeout" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" />
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="Receipt" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;attribute name="ratedvalue" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                     &lt;attribute name="notratedvalue" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                           &lt;attribute name="languagetype" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="includereceipt" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                           &lt;attribute name="includeacknowledgement" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="uid" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" />
 *       &lt;attribute name="messagetype" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="QuestionRequest"/>
 *             &lt;enumeration value="QuestionResponseJAXB"/>
 *             &lt;enumeration value="RatingDelivery"/>
 *             &lt;enumeration value="RatingResponse"/>
 *             &lt;enumeration value="Error"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="mid" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="tid" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="errortext" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="errorcode" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "languages"
})
@XmlRootElement(name = "ServiceMessage")
public class QuestionResponseJAXB {

    @XmlElement(name = "Languages")
    protected QuestionResponseJAXB.Languages languages;
    @XmlAttribute(name = "uid", required = true)
    @XmlSchemaType(name = "unsignedLong")
    protected BigInteger uid;
    @XmlAttribute(name = "messagetype", required = true)
    protected String messagetype;
    @XmlAttribute(name = "mid", required = true)
    protected String mid;
    @XmlAttribute(name = "tid", required = true)
    protected String tid;
    @XmlAttribute(name = "errortext")
    protected String errortext;
    @XmlAttribute(name = "errorcode")
    protected BigInteger errorcode;

    /**
     * Gets the value of the languages property.
     * 
     * @return
     *     possible object is
     *     {@link QuestionResponseJAXB.Languages }
     *     
     */
    public QuestionResponseJAXB.Languages getLanguages() {
        return languages;
    }

    /**
     * Sets the value of the languages property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuestionResponseJAXB.Languages }
     *     
     */
    public void setLanguages(QuestionResponseJAXB.Languages value) {
        this.languages = value;
    }

    /**
     * Gets the value of the uid property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getUid() {
        return uid;
    }

    /**
     * Sets the value of the uid property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setUid(BigInteger value) {
        this.uid = value;
    }

    /**
     * Gets the value of the messagetype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessagetype() {
        return messagetype;
    }

    /**
     * Sets the value of the messagetype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessagetype(String value) {
        this.messagetype = value;
    }

    /**
     * Gets the value of the mid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMid() {
        return mid;
    }

    /**
     * Sets the value of the mid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMid(String value) {
        this.mid = value;
    }

    /**
     * Gets the value of the tid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTid() {
        return tid;
    }

    /**
     * Sets the value of the tid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTid(String value) {
        this.tid = value;
    }

    /**
     * Gets the value of the errortext property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrortext() {
        return errortext;
    }

    /**
     * Sets the value of the errortext property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrortext(String value) {
        this.errortext = value;
    }

    /**
     * Gets the value of the errorcode property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getErrorcode() {
        return errorcode;
    }

    /**
     * Sets the value of the errorcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setErrorcode(BigInteger value) {
        this.errorcode = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Language" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="DisplayElements">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="Question">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;attribute name="qid" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
     *                                     &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                                     &lt;attribute name="questiontimeout" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" />
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="Prize" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;attribute name="prizecode" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                                     &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                                     &lt;attribute name="prizetimeout" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" />
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="Acknowledgement" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;attribute name="ratedvalue" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                                     &lt;attribute name="notratedvalue" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                                     &lt;attribute name="acknowledgetimeout" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" />
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="Receipt" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;attribute name="ratedvalue" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                           &lt;attribute name="notratedvalue" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *                 &lt;attribute name="languagetype" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="includereceipt" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *                 &lt;attribute name="includeacknowledgement" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "language"
    })
    public static class Languages {

        @XmlElement(name = "Language")
        protected QuestionResponseJAXB.Languages.Language language;

        /**
         * Gets the value of the language property.
         * 
         * @return
         *     possible object is
         *     {@link QuestionResponseJAXB.Languages.Language }
         *     
         */
        public QuestionResponseJAXB.Languages.Language getLanguage() {
            return language;
        }

        /**
         * Sets the value of the language property.
         * 
         * @param value
         *     allowed object is
         *     {@link QuestionResponseJAXB.Languages.Language }
         *     
         */
        public void setLanguage(QuestionResponseJAXB.Languages.Language value) {
            this.language = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="DisplayElements">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="Question">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;attribute name="qid" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
         *                           &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *                           &lt;attribute name="questiontimeout" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" />
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="Prize" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;attribute name="prizecode" type="{http://www.w3.org/2001/XMLSchema}string" />
         *                           &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" />
         *                           &lt;attribute name="prizetimeout" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" />
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="Acknowledgement" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;attribute name="ratedvalue" type="{http://www.w3.org/2001/XMLSchema}string" />
         *                           &lt;attribute name="notratedvalue" type="{http://www.w3.org/2001/XMLSchema}string" />
         *                           &lt;attribute name="acknowledgetimeout" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" />
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="Receipt" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;attribute name="ratedvalue" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *                 &lt;attribute name="notratedvalue" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *       &lt;attribute name="languagetype" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="includereceipt" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
         *       &lt;attribute name="includeacknowledgement" type="{http://www.w3.org/2001/XMLSchema}boolean" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "displayElements",
            "receipt"
        })
        public static class Language {

            @XmlElement(name = "DisplayElements", required = true)
            protected QuestionResponseJAXB.Languages.Language.DisplayElements displayElements;
            @XmlElement(name = "Receipt")
            protected QuestionResponseJAXB.Languages.Language.Receipt receipt;
            @XmlAttribute(name = "languagetype", required = true)
            protected String languagetype;
            @XmlAttribute(name = "includereceipt", required = true)
            protected boolean includereceipt;
            @XmlAttribute(name = "includeacknowledgement")
            protected Boolean includeacknowledgement;

            /**
             * Gets the value of the displayElements property.
             * 
             * @return
             *     possible object is
             *     {@link QuestionResponseJAXB.Languages.Language.DisplayElements }
             *     
             */
            public QuestionResponseJAXB.Languages.Language.DisplayElements getDisplayElements() {
                return displayElements;
            }

            /**
             * Sets the value of the displayElements property.
             * 
             * @param value
             *     allowed object is
             *     {@link QuestionResponseJAXB.Languages.Language.DisplayElements }
             *     
             */
            public void setDisplayElements(QuestionResponseJAXB.Languages.Language.DisplayElements value) {
                this.displayElements = value;
            }

            /**
             * Gets the value of the receipt property.
             * 
             * @return
             *     possible object is
             *     {@link QuestionResponseJAXB.Languages.Language.Receipt }
             *     
             */
            public QuestionResponseJAXB.Languages.Language.Receipt getReceipt() {
                return receipt;
            }

            /**
             * Sets the value of the receipt property.
             * 
             * @param value
             *     allowed object is
             *     {@link QuestionResponseJAXB.Languages.Language.Receipt }
             *     
             */
            public void setReceipt(QuestionResponseJAXB.Languages.Language.Receipt value) {
                this.receipt = value;
            }

            /**
             * Gets the value of the languagetype property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getLanguagetype() {
                return languagetype;
            }

            /**
             * Sets the value of the languagetype property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setLanguagetype(String value) {
                this.languagetype = value;
            }

            /**
             * Gets the value of the includereceipt property.
             * 
             */
            public boolean isIncludereceipt() {
                return includereceipt;
            }

            /**
             * Sets the value of the includereceipt property.
             * 
             */
            public void setIncludereceipt(boolean value) {
                this.includereceipt = value;
            }

            /**
             * Gets the value of the includeacknowledgement property.
             * 
             * @return
             *     possible object is
             *     {@link Boolean }
             *     
             */
            public Boolean isIncludeacknowledgement() {
                return includeacknowledgement;
            }

            /**
             * Sets the value of the includeacknowledgement property.
             * 
             * @param value
             *     allowed object is
             *     {@link Boolean }
             *     
             */
            public void setIncludeacknowledgement(Boolean value) {
                this.includeacknowledgement = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="Question">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;attribute name="qid" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
             *                 &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
             *                 &lt;attribute name="questiontimeout" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" />
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="Prize" minOccurs="0">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;attribute name="prizecode" type="{http://www.w3.org/2001/XMLSchema}string" />
             *                 &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" />
             *                 &lt;attribute name="prizetimeout" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" />
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="Acknowledgement" minOccurs="0">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;attribute name="ratedvalue" type="{http://www.w3.org/2001/XMLSchema}string" />
             *                 &lt;attribute name="notratedvalue" type="{http://www.w3.org/2001/XMLSchema}string" />
             *                 &lt;attribute name="acknowledgetimeout" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" />
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "question",
                "prize",
                "acknowledgement"
            })
            public static class DisplayElements {

                @XmlElement(name = "Question", required = true)
                protected QuestionResponseJAXB.Languages.Language.DisplayElements.Question question;
                @XmlElement(name = "Prize")
                protected QuestionResponseJAXB.Languages.Language.DisplayElements.Prize prize;
                @XmlElement(name = "Acknowledgement")
                protected QuestionResponseJAXB.Languages.Language.DisplayElements.Acknowledgement acknowledgement;

                /**
                 * Gets the value of the question property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link QuestionResponseJAXB.Languages.Language.DisplayElements.Question }
                 *     
                 */
                public QuestionResponseJAXB.Languages.Language.DisplayElements.Question getQuestion() {
                    return question;
                }

                /**
                 * Sets the value of the question property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link QuestionResponseJAXB.Languages.Language.DisplayElements.Question }
                 *     
                 */
                public void setQuestion(QuestionResponseJAXB.Languages.Language.DisplayElements.Question value) {
                    this.question = value;
                }

                /**
                 * Gets the value of the prize property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link QuestionResponseJAXB.Languages.Language.DisplayElements.Prize }
                 *     
                 */
                public QuestionResponseJAXB.Languages.Language.DisplayElements.Prize getPrize() {
                    return prize;
                }

                /**
                 * Sets the value of the prize property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link QuestionResponseJAXB.Languages.Language.DisplayElements.Prize }
                 *     
                 */
                public void setPrize(QuestionResponseJAXB.Languages.Language.DisplayElements.Prize value) {
                    this.prize = value;
                }

                /**
                 * Gets the value of the acknowledgement property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link QuestionResponseJAXB.Languages.Language.DisplayElements.Acknowledgement }
                 *     
                 */
                public QuestionResponseJAXB.Languages.Language.DisplayElements.Acknowledgement getAcknowledgement() {
                    return acknowledgement;
                }

                /**
                 * Sets the value of the acknowledgement property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link QuestionResponseJAXB.Languages.Language.DisplayElements.Acknowledgement }
                 *     
                 */
                public void setAcknowledgement(QuestionResponseJAXB.Languages.Language.DisplayElements.Acknowledgement value) {
                    this.acknowledgement = value;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;attribute name="ratedvalue" type="{http://www.w3.org/2001/XMLSchema}string" />
                 *       &lt;attribute name="notratedvalue" type="{http://www.w3.org/2001/XMLSchema}string" />
                 *       &lt;attribute name="acknowledgetimeout" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" />
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class Acknowledgement {

                    @XmlAttribute(name = "ratedvalue")
                    protected String ratedvalue;
                    @XmlAttribute(name = "notratedvalue")
                    protected String notratedvalue;
                    @XmlAttribute(name = "acknowledgetimeout")
                    @XmlSchemaType(name = "unsignedShort")
                    protected Integer acknowledgetimeout;

                    /**
                     * Gets the value of the ratedvalue property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getRatedvalue() {
                        return ratedvalue;
                    }

                    /**
                     * Sets the value of the ratedvalue property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setRatedvalue(String value) {
                        this.ratedvalue = value;
                    }

                    /**
                     * Gets the value of the notratedvalue property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getNotratedvalue() {
                        return notratedvalue;
                    }

                    /**
                     * Sets the value of the notratedvalue property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setNotratedvalue(String value) {
                        this.notratedvalue = value;
                    }

                    /**
                     * Gets the value of the acknowledgetimeout property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link Integer }
                     *     
                     */
                    public Integer getAcknowledgetimeout() {
                        return acknowledgetimeout;
                    }

                    /**
                     * Sets the value of the acknowledgetimeout property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link Integer }
                     *     
                     */
                    public void setAcknowledgetimeout(Integer value) {
                        this.acknowledgetimeout = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;attribute name="prizecode" type="{http://www.w3.org/2001/XMLSchema}string" />
                 *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" />
                 *       &lt;attribute name="prizetimeout" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" />
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class Prize {

                    @XmlAttribute(name = "prizecode")
                    protected String prizecode;
                    @XmlAttribute(name = "value")
                    protected String value;
                    @XmlAttribute(name = "prizetimeout")
                    @XmlSchemaType(name = "unsignedShort")
                    protected Integer prizetimeout;

                    /**
                     * Gets the value of the prizecode property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getPrizecode() {
                        return prizecode;
                    }

                    /**
                     * Sets the value of the prizecode property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setPrizecode(String value) {
                        this.prizecode = value;
                    }

                    /**
                     * Gets the value of the value property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getValue() {
                        return value;
                    }

                    /**
                     * Sets the value of the value property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setValue(String value) {
                        this.value = value;
                    }

                    /**
                     * Gets the value of the prizetimeout property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link Integer }
                     *     
                     */
                    public Integer getPrizetimeout() {
                        return prizetimeout;
                    }

                    /**
                     * Sets the value of the prizetimeout property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link Integer }
                     *     
                     */
                    public void setPrizetimeout(Integer value) {
                        this.prizetimeout = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;attribute name="qid" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
                 *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
                 *       &lt;attribute name="questiontimeout" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" />
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class Question {

                    @XmlAttribute(name = "qid", required = true)
                    protected long qid;
                    @XmlAttribute(name = "value", required = true)
                    protected String value;
                    @XmlAttribute(name = "questiontimeout")
                    @XmlSchemaType(name = "unsignedShort")
                    protected Integer questiontimeout;

                    /**
                     * Gets the value of the qid property.
                     * 
                     */
                    public long getQid() {
                        return qid;
                    }

                    /**
                     * Sets the value of the qid property.
                     * 
                     */
                    public void setQid(long value) {
                        this.qid = value;
                    }

                    /**
                     * Gets the value of the value property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getValue() {
                        return value;
                    }

                    /**
                     * Sets the value of the value property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setValue(String value) {
                        this.value = value;
                    }

                    /**
                     * Gets the value of the questiontimeout property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link Integer }
                     *     
                     */
                    public Integer getQuestiontimeout() {
                        return questiontimeout;
                    }

                    /**
                     * Sets the value of the questiontimeout property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link Integer }
                     *     
                     */
                    public void setQuestiontimeout(Integer value) {
                        this.questiontimeout = value;
                    }

                }

            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;attribute name="ratedvalue" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
             *       &lt;attribute name="notratedvalue" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Receipt {

                @XmlAttribute(name = "ratedvalue", required = true)
                protected String ratedvalue;
                @XmlAttribute(name = "notratedvalue", required = true)
                protected String notratedvalue;

                /**
                 * Gets the value of the ratedvalue property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getRatedvalue() {
                    return ratedvalue;
                }

                /**
                 * Sets the value of the ratedvalue property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setRatedvalue(String value) {
                    this.ratedvalue = value;
                }

                /**
                 * Gets the value of the notratedvalue property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getNotratedvalue() {
                    return notratedvalue;
                }

                /**
                 * Sets the value of the notratedvalue property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setNotratedvalue(String value) {
                    this.notratedvalue = value;
                }

            }

        }

    }

}