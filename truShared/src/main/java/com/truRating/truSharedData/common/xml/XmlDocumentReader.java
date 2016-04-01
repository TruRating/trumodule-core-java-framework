/*
 * @(#)XmlReadDocument.java
 *
 * Copyright (c) 2013 truRating Limited. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of 
 * truRating Limited. ("Confidential Information").  You shall
 * not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with truRating Limited.
 *
 * TRURATING LIMITED MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT 
 * THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS 
 * FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. TRURATING LIMITED
 * SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT 
 * OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

/*
 * VERSION HISTORY:
 *
 * 1.00 4 Sep 2013 Initial Version
 */

package com.truRating.truSharedData.common.xml;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import com.truRating.truSharedData.utilTime.TDate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Reads an XmlDocument and exposes the DOM
 * 
 * @author Peter Salmon
 * 
 */

public class XmlDocumentReader extends XmlDocument {

	Document xml;

	private String xmlText = "";
	private boolean xmlIsOK = true;

	/**
	 * @param response
	 * @throws IOException
	 * @throws SAXException
	 */
	public XmlDocumentReader(byte[] xmlBytes) throws SAXException, IOException,
			ParserConfigurationException {

		this.xmlText = new String(xmlBytes, 0, xmlBytes.length, "UTF-8");
		xmlIsOK = false;
		xml = this.createDocument(xmlBytes);
		xmlIsOK = true;
	}

	/**
	 * Boolean value to indicate that the xml text was parsed successfully
	 * 
	 * @return
	 */
	public boolean isOK() {
		return this.xmlIsOK;
	}

	public String toString() {
		return this.xmlText;
	}

	public NodeList getElements(String tagName) {
		NodeList nodes = xml.getElementsByTagName(tagName);
		return nodes;
	}

	public Element getElement(String tagName) {
		return getElement(tagName, 0);
	}

	public Element getElement(String tagName, int instance) {

		NodeList nodes = getElements(tagName);

		if ((nodes != null) && (nodes.getLength() > instance)
				&& (nodes.item(instance) instanceof Element))
			return (Element) nodes.item(instance);

		return null;
	}

	public String getValue(String tagName) {
		return getValue(tagName, 0);
	}

	public String getValue(String tagName, int instance) {
		Element element = getElement(tagName, instance);
		if (element == null)
			return "";
		return element.getNodeValue();
	}

	public String getAttributeValue(String tagName, String attributeName) {
		return getAttributeValue(tagName, 0, attributeName);
	}

	public String getAttributeValue(String tagName, int instance, String attributeName) {
		Element element = getElement(tagName, instance);
		if (element == null)
			return "";
		return getAttributeValue(element, attributeName, "");
	}

	public String getAttributeValue(Element element, String attributeName, String defaultValue) {
		if (element.getAttributes().getNamedItem(attributeName) != null)
			return element.getAttribute(attributeName);
		return defaultValue;
	}

	public int getAttributeValueInt(Element element, String attributeName, int defaultValue) {
		int rv = defaultValue;
		if (element.getAttributes().getNamedItem(attributeName) != null) {
			String av = element.getAttribute(attributeName);
			rv = Integer.parseInt(av);
		}
		return rv;
	}

	public TDate getAttributeValueDate(Element element, String attributeName, TDate defaultValue) {
		TDate rv = defaultValue;
		if (element.getAttributes().getNamedItem(attributeName) != null) {
			String av = element.getAttribute(attributeName);
			rv = new TDate(Integer.parseInt(av));
		}
		return rv;
	}
}
