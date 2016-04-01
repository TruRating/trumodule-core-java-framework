
package com.truRating.truModule.xml.ratingResponse;

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
 *                             &lt;element name="Receipt" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;attribute name="ratedvalue" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                     &lt;attribute name="notratedvalue" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                           &lt;attribute name="languagetype" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="includereceipt" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
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
 *             &lt;enumeration value="QuestionResponse"/>
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
public class RatingResponseJAXB {

    @XmlElement(name = "Languages")
    protected RatingResponseJAXB.Languages languages;
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
     *     {@link RatingResponseJAXB.Languages }
     *     
     */
    public RatingResponseJAXB.Languages getLanguages() {
        return languages;
    }

    /**
     * Sets the value of the languages property.
     * 
     * @param value
     *     allowed object is
     *     {@link RatingResponseJAXB.Languages }
     *     
     */
    public void setLanguages(RatingResponseJAXB.Languages value) {
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
     *                   &lt;element name="Receipt" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;attribute name="ratedvalue" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                           &lt;attribute name="notratedvalue" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *                 &lt;attribute name="languagetype" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="includereceipt" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
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
        protected RatingResponseJAXB.Languages.Language language;

        /**
         * Gets the value of the language property.
         * 
         * @return
         *     possible object is
         *     {@link RatingResponseJAXB.Languages.Language }
         *     
         */
        public RatingResponseJAXB.Languages.Language getLanguage() {
            return language;
        }

        /**
         * Sets the value of the language property.
         * 
         * @param value
         *     allowed object is
         *     {@link RatingResponseJAXB.Languages.Language }
         *     
         */
        public void setLanguage(RatingResponseJAXB.Languages.Language value) {
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
         *         &lt;element name="Receipt" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;attribute name="ratedvalue" type="{http://www.w3.org/2001/XMLSchema}string" />
         *                 &lt;attribute name="notratedvalue" type="{http://www.w3.org/2001/XMLSchema}string" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *       &lt;attribute name="languagetype" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="includereceipt" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "receipt"
        })
        public static class Language {

            @XmlElement(name = "Receipt")
            protected RatingResponseJAXB.Languages.Language.Receipt receipt;
            @XmlAttribute(name = "languagetype", required = true)
            protected String languagetype;
            @XmlAttribute(name = "includereceipt", required = true)
            protected boolean includereceipt;

            /**
             * Gets the value of the receipt property.
             * 
             * @return
             *     possible object is
             *     {@link RatingResponseJAXB.Languages.Language.Receipt }
             *     
             */
            public RatingResponseJAXB.Languages.Language.Receipt getReceipt() {
                return receipt;
            }

            /**
             * Sets the value of the receipt property.
             * 
             * @param value
             *     allowed object is
             *     {@link RatingResponseJAXB.Languages.Language.Receipt }
             *     
             */
            public void setReceipt(RatingResponseJAXB.Languages.Language.Receipt value) {
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

                @XmlAttribute(name = "ratedvalue")
                protected String ratedvalue;
                @XmlAttribute(name = "notratedvalue")
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
