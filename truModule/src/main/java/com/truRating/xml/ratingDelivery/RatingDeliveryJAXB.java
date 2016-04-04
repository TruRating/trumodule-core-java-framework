
package com.trurating.xml.ratingDelivery;

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
 *                           &lt;attribute name="languagetype" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="includereceipt" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Rating" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}short" />
 *                 &lt;attribute name="responsetimemilliseconds" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
 *                 &lt;attribute name="qid" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
 *                 &lt;attribute name="prizecode" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="ratinglanguage" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Transaction" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="txnid" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *                 &lt;attribute name="datetime" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="amount" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                 &lt;attribute name="gratuity" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                 &lt;attribute name="currency" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedByte" />
 *                 &lt;attribute name="cardtype" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="entrymode" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="tendertype" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="result" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;enumeration value="Approved"/>
 *                       &lt;enumeration value="Declined"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="operator" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="CardHash">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="cardhashdatatype">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;enumeration value="CDH1"/>
 *                       &lt;enumeration value="CDH2"/>
 *                       &lt;enumeration value="CDH3"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="cardhashdata" type="{http://www.w3.org/2001/XMLSchema}string" />
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
    "languages",
    "rating",
    "transaction",
    "cardHash"
})
@XmlRootElement(name = "ServiceMessage")
public class RatingDeliveryJAXB {

    @XmlElement(name = "Languages")
    protected RatingDeliveryJAXB.Languages languages;
    @XmlElement(name = "Rating")
    protected RatingDeliveryJAXB.Rating rating;
    @XmlElement(name = "Transaction")
    protected RatingDeliveryJAXB.Transaction transaction;
    @XmlElement(name = "CardHash", required = true)
    protected RatingDeliveryJAXB.CardHash cardHash;
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
     *     {@link RatingDeliveryJAXB.Languages }
     *     
     */
    public RatingDeliveryJAXB.Languages getLanguages() {
        return languages;
    }

    /**
     * Sets the value of the languages property.
     * 
     * @param value
     *     allowed object is
     *     {@link RatingDeliveryJAXB.Languages }
     *     
     */
    public void setLanguages(RatingDeliveryJAXB.Languages value) {
        this.languages = value;
    }

    /**
     * Gets the value of the rating property.
     * 
     * @return
     *     possible object is
     *     {@link RatingDeliveryJAXB.Rating }
     *     
     */
    public RatingDeliveryJAXB.Rating getRating() {
        return rating;
    }

    /**
     * Sets the value of the rating property.
     * 
     * @param value
     *     allowed object is
     *     {@link RatingDeliveryJAXB.Rating }
     *     
     */
    public void setRating(RatingDeliveryJAXB.Rating value) {
        this.rating = value;
    }

    /**
     * Gets the value of the transaction property.
     * 
     * @return
     *     possible object is
     *     {@link RatingDeliveryJAXB.Transaction }
     *     
     */
    public RatingDeliveryJAXB.Transaction getTransaction() {
        return transaction;
    }

    /**
     * Sets the value of the transaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link RatingDeliveryJAXB.Transaction }
     *     
     */
    public void setTransaction(RatingDeliveryJAXB.Transaction value) {
        this.transaction = value;
    }

    /**
     * Gets the value of the cardHash property.
     * 
     * @return
     *     possible object is
     *     {@link RatingDeliveryJAXB.CardHash }
     *     
     */
    public RatingDeliveryJAXB.CardHash getCardHash() {
        return cardHash;
    }

    /**
     * Sets the value of the cardHash property.
     * 
     * @param value
     *     allowed object is
     *     {@link RatingDeliveryJAXB.CardHash }
     *     
     */
    public void setCardHash(RatingDeliveryJAXB.CardHash value) {
        this.cardHash = value;
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
     *       &lt;attribute name="cardhashdatatype">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;enumeration value="CDH1"/>
     *             &lt;enumeration value="CDH2"/>
     *             &lt;enumeration value="CDH3"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="cardhashdata" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class CardHash {

        @XmlAttribute(name = "cardhashdatatype")
        protected String cardhashdatatype;
        @XmlAttribute(name = "cardhashdata")
        protected String cardhashdata;

        /**
         * Gets the value of the cardhashdatatype property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCardhashdatatype() {
            return cardhashdatatype;
        }

        /**
         * Sets the value of the cardhashdatatype property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCardhashdatatype(String value) {
            this.cardhashdatatype = value;
        }

        /**
         * Gets the value of the cardhashdata property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCardhashdata() {
            return cardhashdata;
        }

        /**
         * Sets the value of the cardhashdata property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCardhashdata(String value) {
            this.cardhashdata = value;
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
     *       &lt;sequence>
     *         &lt;element name="Language" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="languagetype" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="includereceipt" type="{http://www.w3.org/2001/XMLSchema}boolean" />
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
        protected RatingDeliveryJAXB.Languages.Language language;

        /**
         * Gets the value of the language property.
         * 
         * @return
         *     possible object is
         *     {@link RatingDeliveryJAXB.Languages.Language }
         *     
         */
        public RatingDeliveryJAXB.Languages.Language getLanguage() {
            return language;
        }

        /**
         * Sets the value of the language property.
         * 
         * @param value
         *     allowed object is
         *     {@link RatingDeliveryJAXB.Languages.Language }
         *     
         */
        public void setLanguage(RatingDeliveryJAXB.Languages.Language value) {
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
         *       &lt;attribute name="languagetype" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="includereceipt" type="{http://www.w3.org/2001/XMLSchema}boolean" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Language {

            @XmlAttribute(name = "languagetype", required = true)
            protected String languagetype;
            @XmlAttribute(name = "includereceipt")
            protected Boolean includereceipt;

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
             * @return
             *     possible object is
             *     {@link Boolean }
             *     
             */
            public Boolean isIncludereceipt() {
                return includereceipt;
            }

            /**
             * Sets the value of the includereceipt property.
             * 
             * @param value
             *     allowed object is
             *     {@link Boolean }
             *     
             */
            public void setIncludereceipt(Boolean value) {
                this.includereceipt = value;
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
     *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}short" />
     *       &lt;attribute name="responsetimemilliseconds" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
     *       &lt;attribute name="qid" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
     *       &lt;attribute name="prizecode" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="ratinglanguage" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Rating {

        @XmlAttribute(name = "value", required = true)
        protected short value;
        @XmlAttribute(name = "responsetimemilliseconds", required = true)
        protected long responsetimemilliseconds;
        @XmlAttribute(name = "qid", required = true)
        protected long qid;
        @XmlAttribute(name = "prizecode")
        protected String prizecode;
        @XmlAttribute(name = "ratinglanguage", required = true)
        protected String ratinglanguage;

        /**
         * Gets the value of the value property.
         * 
         */
        public short getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         */
        public void setValue(short value) {
            this.value = value;
        }

        /**
         * Gets the value of the responsetimemilliseconds property.
         * 
         */
        public long getResponsetimemilliseconds() {
            return responsetimemilliseconds;
        }

        /**
         * Sets the value of the responsetimemilliseconds property.
         * 
         */
        public void setResponsetimemilliseconds(long value) {
            this.responsetimemilliseconds = value;
        }

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
         * Gets the value of the ratinglanguage property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRatinglanguage() {
            return ratinglanguage;
        }

        /**
         * Sets the value of the ratinglanguage property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRatinglanguage(String value) {
            this.ratinglanguage = value;
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
     *       &lt;attribute name="txnid" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
     *       &lt;attribute name="datetime" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="amount" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
     *       &lt;attribute name="gratuity" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
     *       &lt;attribute name="currency" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedByte" />
     *       &lt;attribute name="cardtype" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="entrymode" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="tendertype" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="result" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;enumeration value="Approved"/>
     *             &lt;enumeration value="Declined"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="operator" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Transaction {

        @XmlAttribute(name = "txnid", required = true)
        @XmlSchemaType(name = "unsignedInt")
        protected long txnid;
        @XmlAttribute(name = "datetime", required = true)
        protected String datetime;
        @XmlAttribute(name = "amount", required = true)
        protected int amount;
        @XmlAttribute(name = "gratuity", required = true)
        protected int gratuity;
        @XmlAttribute(name = "currency", required = true)
        @XmlSchemaType(name = "unsignedByte")
        protected short currency;
        @XmlAttribute(name = "cardtype", required = true)
        protected String cardtype;
        @XmlAttribute(name = "entrymode", required = true)
        protected String entrymode;
        @XmlAttribute(name = "tendertype", required = true)
        protected String tendertype;
        @XmlAttribute(name = "result", required = true)
        protected String result;
        @XmlAttribute(name = "operator")
        protected String operator;

        /**
         * Gets the value of the txnid property.
         * 
         */
        public long getTxnid() {
            return txnid;
        }

        /**
         * Sets the value of the txnid property.
         * 
         */
        public void setTxnid(long value) {
            this.txnid = value;
        }

        /**
         * Gets the value of the datetime property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDatetime() {
            return datetime;
        }

        /**
         * Sets the value of the datetime property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDatetime(String value) {
            this.datetime = value;
        }

        /**
         * Gets the value of the amount property.
         * 
         */
        public int getAmount() {
            return amount;
        }

        /**
         * Sets the value of the amount property.
         * 
         */
        public void setAmount(int value) {
            this.amount = value;
        }

        /**
         * Gets the value of the gratuity property.
         * 
         */
        public int getGratuity() {
            return gratuity;
        }

        /**
         * Sets the value of the gratuity property.
         * 
         */
        public void setGratuity(int value) {
            this.gratuity = value;
        }

        /**
         * Gets the value of the currency property.
         * 
         */
        public short getCurrency() {
            return currency;
        }

        /**
         * Sets the value of the currency property.
         * 
         */
        public void setCurrency(short value) {
            this.currency = value;
        }

        /**
         * Gets the value of the cardtype property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCardtype() {
            return cardtype;
        }

        /**
         * Sets the value of the cardtype property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCardtype(String value) {
            this.cardtype = value;
        }

        /**
         * Gets the value of the entrymode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEntrymode() {
            return entrymode;
        }

        /**
         * Sets the value of the entrymode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEntrymode(String value) {
            this.entrymode = value;
        }

        /**
         * Gets the value of the tendertype property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTendertype() {
            return tendertype;
        }

        /**
         * Sets the value of the tendertype property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTendertype(String value) {
            this.tendertype = value;
        }

        /**
         * Gets the value of the result property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getResult() {
            return result;
        }

        /**
         * Sets the value of the result property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setResult(String value) {
            this.result = value;
        }

        /**
         * Gets the value of the operator property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOperator() {
            return operator;
        }

        /**
         * Sets the value of the operator property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOperator(String value) {
            this.operator = value;
        }

    }

}
