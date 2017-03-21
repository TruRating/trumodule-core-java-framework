
/*
 *  The MIT License
 *
 *  Copyright (c) 2017 TruRating Ltd. https://www.trurating.com
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package com.trurating.trumodule.network;

import com.trurating.service.v220.xml.Request;
import com.trurating.service.v220.xml.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

/**
 * The type Jaxb marshall serializer.
 */
@SuppressWarnings("SpellCheckingInspection")
public class JAXBSerializer implements ISerializer {
    final private Logger logger = LoggerFactory.getLogger(JAXBSerializer.class);
    private Marshaller requestMarshaller;
    private Unmarshaller responseUnmarshaller;

    /**
     * Instantiates a new Jaxb marshall serializer.
     */
    @SuppressWarnings("WeakerAccess")
    public JAXBSerializer() {
        logger.debug("Loading JAXBSerializer...");
        try {
            requestMarshaller = JAXBContext.newInstance(Request.class).createMarshaller();
            requestMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            requestMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            responseUnmarshaller = JAXBContext.newInstance(Response.class).createUnmarshaller();
        } catch (JAXBException e) {
            logger.error("Error loading JAXB",e);
        }
    }

    public StringWriter marshall(Request request) {
        try {
            StringWriter sw = new StringWriter();
            requestMarshaller.marshal(request, sw);
            return sw;
        } catch (JAXBException e) {
            logger.error("Error marshalling Request",e);
            return null;
        }
    }

    public Response unMarshall(String responseString) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(responseString.getBytes());
        Response response;
        try {
            response = (Response) responseUnmarshaller.unmarshal(inputStream);
            inputStream.close();
        } catch (JAXBException e) {
            logger.error("Error unmarshalling response",e);
            return null;
        } catch (IOException e) {
            logger.error("Error unmarshalling response",e);
            return null;
        }
        return response;
    }
}
