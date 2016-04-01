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
 * 1.00 4 Sep 2013 Initial Version
 */

package com.truRating.truSharedData.common.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * A wrapper for a DOM document
 * 
 * @author Peter Salmon
 * 
 */
public abstract class XmlDocument {

	// DocBuilder (static) instances
	private static DocumentBuilder docBuilder = null;

	public XmlDocument() throws ParserConfigurationException {

		if (docBuilder == null) {
			javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory
					.newInstance();

			dbf.setNamespaceAware(true);
			// dbf.setValidating(dtdValidate || xsdValidate);

			docBuilder = dbf.newDocumentBuilder();
		}
	}

	public Document createDocument() {
		return docBuilder.newDocument();
	}

	public Document createDocument(byte[] xml) throws SAXException, IOException {

		ByteArrayInputStream xmlStream = new ByteArrayInputStream(xml);

		return docBuilder.parse(xmlStream);
	}
}
