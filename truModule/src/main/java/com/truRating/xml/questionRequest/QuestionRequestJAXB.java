
package com.trurating.xml.questionRequest;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="Languages">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Language" maxOccurs="2">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="languagetype" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="includereceipt" type="{http://www.w3.org/2001/XMLSchema}boolean" />
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
 *         &lt;element name="DeviceInfo">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="device" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="firmware" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="nlines" use="required" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *                 &lt;attribute name="cpl" use="required" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *                 &lt;attribute name="format" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;enumeration value="RAW"/>
 *                       &lt;enumeration value="TEXT"/>
 *                       &lt;enumeration value="TEXT_CENTERED"/>
 *                       &lt;enumeration value="HTML"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="fieldseparator" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="fonttype" default="MONOSPACED">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;enumeration value="MONOSPACED"/>
 *                       &lt;enumeration value="PROPORTIONAL"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="receiptwidth" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedByte" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ServerInfo">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="serverid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="ppafirmware" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
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
    "deviceInfo",
    "serverInfo"
})
@XmlRootElement(name = "ServiceMessage")
public class QuestionRequestJAXB {

    @XmlElement(name = "Languages", required = true)
    protected QuestionRequestJAXB.Languages languages;
    @XmlElement(name = "DeviceInfo", required = true)
    protected QuestionRequestJAXB.DeviceInfo deviceInfo;
    @XmlElement(name = "ServerInfo", required = true)
    protected QuestionRequestJAXB.ServerInfo serverInfo;
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
     *     {@link QuestionRequestJAXB.Languages }
     *     
     */
    public QuestionRequestJAXB.Languages getLanguages() {
        return languages;
    }

    /**
     * Sets the value of the languages property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuestionRequestJAXB.Languages }
     *     
     */
    public void setLanguages(QuestionRequestJAXB.Languages value) {
        this.languages = value;
    }

    /**
     * Gets the value of the deviceInfo property.
     * 
     * @return
     *     possible object is
     *     {@link QuestionRequestJAXB.DeviceInfo }
     *     
     */
    public QuestionRequestJAXB.DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    /**
     * Sets the value of the deviceInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuestionRequestJAXB.DeviceInfo }
     *     
     */
    public void setDeviceInfo(QuestionRequestJAXB.DeviceInfo value) {
        this.deviceInfo = value;
    }

    /**
     * Gets the value of the serverInfo property.
     * 
     * @return
     *     possible object is
     *     {@link QuestionRequestJAXB.ServerInfo }
     *     
     */
    public QuestionRequestJAXB.ServerInfo getServerInfo() {
        return serverInfo;
    }

    /**
     * Sets the value of the serverInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuestionRequestJAXB.ServerInfo }
     *     
     */
    public void setServerInfo(QuestionRequestJAXB.ServerInfo value) {
        this.serverInfo = value;
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
     *       &lt;attribute name="device" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="firmware" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="nlines" use="required" type="{http://www.w3.org/2001/XMLSchema}byte" />
     *       &lt;attribute name="cpl" use="required" type="{http://www.w3.org/2001/XMLSchema}byte" />
     *       &lt;attribute name="format" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;enumeration value="RAW"/>
     *             &lt;enumeration value="TEXT"/>
     *             &lt;enumeration value="TEXT_CENTERED"/>
     *             &lt;enumeration value="HTML"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="fieldseparator" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="fonttype" default="MONOSPACED">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;enumeration value="MONOSPACED"/>
     *             &lt;enumeration value="PROPORTIONAL"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="receiptwidth" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedByte" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class DeviceInfo {

        @XmlAttribute(name = "device", required = true)
        protected String device;
        @XmlAttribute(name = "firmware", required = true)
        protected String firmware;
        @XmlAttribute(name = "nlines", required = true)
        protected byte nlines;
        @XmlAttribute(name = "cpl", required = true)
        protected byte cpl;
        @XmlAttribute(name = "format", required = true)
        protected String format;
        @XmlAttribute(name = "fieldseparator")
        protected String fieldseparator;
        @XmlAttribute(name = "fonttype")
        protected String fonttype;
        @XmlAttribute(name = "receiptwidth", required = true)
        @XmlSchemaType(name = "unsignedByte")
        protected short receiptwidth;

        /**
         * Gets the value of the device property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDevice() {
            return device;
        }

        /**
         * Sets the value of the device property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDevice(String value) {
            this.device = value;
        }

        /**
         * Gets the value of the firmware property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFirmware() {
            return firmware;
        }

        /**
         * Sets the value of the firmware property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFirmware(String value) {
            this.firmware = value;
        }

        /**
         * Gets the value of the nlines property.
         * 
         */
        public byte getNlines() {
            return nlines;
        }

        /**
         * Sets the value of the nlines property.
         * 
         */
        public void setNlines(byte value) {
            this.nlines = value;
        }

        /**
         * Gets the value of the cpl property.
         * 
         */
        public byte getCpl() {
            return cpl;
        }

        /**
         * Sets the value of the cpl property.
         * 
         */
        public void setCpl(byte value) {
            this.cpl = value;
        }

        /**
         * Gets the value of the format property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFormat() {
            return format;
        }

        /**
         * Sets the value of the format property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFormat(String value) {
            this.format = value;
        }

        /**
         * Gets the value of the fieldseparator property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFieldseparator() {
            return fieldseparator;
        }

        /**
         * Sets the value of the fieldseparator property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFieldseparator(String value) {
            this.fieldseparator = value;
        }

        /**
         * Gets the value of the fonttype property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFonttype() {
            if (fonttype == null) {
                return "MONOSPACED";
            } else {
                return fonttype;
            }
        }

        /**
         * Sets the value of the fonttype property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFonttype(String value) {
            this.fonttype = value;
        }

        /**
         * Gets the value of the receiptwidth property.
         * 
         */
        public short getReceiptwidth() {
            return receiptwidth;
        }

        /**
         * Sets the value of the receiptwidth property.
         * 
         */
        public void setReceiptwidth(short value) {
            this.receiptwidth = value;
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
     *         &lt;element name="Language" maxOccurs="2">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="languagetype" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="includereceipt" type="{http://www.w3.org/2001/XMLSchema}boolean" />
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

        @XmlElement(name = "Language", required = true)
        protected List<QuestionRequestJAXB.Languages.Language> language;

        /**
         * Gets the value of the language property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the language property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getLanguage().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link QuestionRequestJAXB.Languages.Language }
         * 
         * 
         */
        public List<QuestionRequestJAXB.Languages.Language> getLanguage() {
            if (language == null) {
                language = new ArrayList<QuestionRequestJAXB.Languages.Language>();
            }
            return this.language;
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
         *       &lt;attribute name="includeacknowledgement" type="{http://www.w3.org/2001/XMLSchema}boolean" />
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
            @XmlAttribute(name = "includeacknowledgement")
            protected Boolean includeacknowledgement;

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
     *       &lt;attribute name="serverid" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="ppafirmware" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class ServerInfo {

        @XmlAttribute(name = "serverid")
        protected String serverid;
        @XmlAttribute(name = "ppafirmware", required = true)
        protected String ppafirmware;

        /**
         * Gets the value of the serverid property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getServerid() {
            return serverid;
        }

        /**
         * Sets the value of the serverid property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setServerid(String value) {
            this.serverid = value;
        }

        /**
         * Gets the value of the ppafirmware property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPpafirmware() {
            return ppafirmware;
        }

        /**
         * Sets the value of the ppafirmware property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPpafirmware(String value) {
            this.ppafirmware = value;
        }

    }

}
