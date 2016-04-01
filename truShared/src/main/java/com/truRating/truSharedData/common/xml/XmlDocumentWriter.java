/*
 * @(#)XmlDocument.java
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
 * 1.00 30 Aug 2013 Initial Version
 */

package com.truRating.truSharedData.common.xml;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Models the gradual building up of an Xml document The last created element is
 * held as a "selected" element and all operations are done to this by default,
 * unless an alternative element is provided. The selected element can be reset
 * to an alternative existing element.
 * 
 * 
 * @author Peter Salmon
 * 
 */
public class XmlDocumentWriter extends XmlDocument {

	private Document xml = null;
	private Element rootElement = null;

	public XmlDocumentWriter(String rootElementName) throws ParserConfigurationException {

		// Create the Xml document
		xml = this.createDocument();

		// Create the root element
		rootElement = xml.createElement(rootElementName);

		// Add the root element to the xml document
		xml.appendChild(rootElement);
	}

	public Element getRootElement() {
		return rootElement;
	}

	public Element createElement(Element parentElement, String elementType) {
		Element newElement = xml.createElement(elementType);
		parentElement.appendChild(newElement);
		return newElement;
	}

	/**
	 * Create an attribute with the given parameters and add it to the element
	 * provided
	 * 
	 */
	public void addAttribute(Element element, String attrName, String attrValue) {
		Attr attr = xml.createAttribute(attrName);
		attr.setValue(attrValue);
		element.setAttributeNode(attr);
	}

	/**
	 * Create an attribute with the given parameters and add it to the element
	 * provided
	 * 
	 */
	public void addAttribute(Element element, String attrName, long attrValue) {
		Attr attr = xml.createAttribute(attrName);
		attr.setValue(String.valueOf(attrValue));
		element.setAttributeNode(attr);
	}

	/**
	 * Wrap the prepared output as a single String
	 */
	public String toString() {
		/*
		 * try { Transformer transformer = TransformerFactory.newInstance()
		 * .newTransformer(); StreamResult result = new StreamResult(new
		 * StringWriter()); DOMSource source = new DOMSource(xml);
		 * transformer.transform(source, result); return
		 * result.getWriter().toString(); } catch (TransformerException ex) {
		 * Log.error("Xml document transformation failed: " + ex.toString());
		 * ex.printStackTrace(); return ""; }
		 */
		StringWriter stringOut = new StringWriter();
		try {
			OutputFormat outputFormat = new OutputFormat(xml);
			// outputFormat.setIndent(2);
			XMLSerializer serializer = new XMLSerializer(stringOut, outputFormat);
			serializer.serialize(xml);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Display the XML
		String rv = stringOut.toString();
		return rv;
	}

}
