
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

import com.trurating.service.v210.xml.Request;
import com.trurating.service.v210.xml.Response;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.logging.Logger;

public class JAXBMarshallSerialiser implements IMarshaller{

    final private Logger log = Logger.getLogger(HttpClient.class.getName());
    private Marshaller requestMarshaller;
    private Unmarshaller responseUnmarshaller;

    /**
     * Instantiates a new Jaxb marshall serialiser.
     */
    @SuppressWarnings("WeakerAccess")
    public JAXBMarshallSerialiser() {
        try {
            requestMarshaller = JAXBContext.newInstance(Request.class).createMarshaller();
            requestMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            requestMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            responseUnmarshaller = JAXBContext.newInstance(Response.class).createUnmarshaller();
        } catch (JAXBException e) {
            log.severe(e.toString());
        }
    }

    @Override
    public StringWriter marshall(Request request) {
        try {
            StringWriter sw = new StringWriter();
            requestMarshaller.marshal(request, sw);
            return sw;
        } catch (JAXBException e) {
            log.severe("Error marshalling Request");
            log.severe(e.toString());
            return null;
        }
    }

    @Override
    public Response unMarshall(String responseString) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(responseString.getBytes());
        Response response = null;
        try {
            response = (Response) responseUnmarshaller.unmarshal(inputStream);
            inputStream.close();
        } catch (JAXBException e) {
            log.severe("Error unmarshalling response");
            log.severe(e.toString());
            return null;
        } catch (IOException e) {
            log.severe("Error unmarshalling response");
            log.severe(e.toString());
            return null;
        }
        return response;
    }
}
