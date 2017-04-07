/*
 * // The MIT License
 * //
 * // Copyright (c) 2017 TruRating Ltd. https://www.trurating.com
 * //
 * // Permission is hereby granted, free of charge, to any person obtaining a copy
 * // of this software and associated documentation files (the "Software"), to deal
 * // in the Software without restriction, including without limitation the rights
 * // to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * // copies of the Software, and to permit persons to whom the Software is
 * // furnished to do so, subject to the following conditions:
 * //
 * // The above copyright notice and this permission notice shall be included in
 * // all copies or substantial portions of the Software.
 * //
 * // THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * // IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * // FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * // AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * // LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * // OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * // THE SOFTWARE.
 */

package com.trurating.trumodule.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * The type Tru module date utils.
 */
public class TruModuleDateUtils {

    private static final Logger logger = LoggerFactory.getLogger(TruModuleDateUtils.class);

    /**
     * Provides the time now.
     * A convenience method that will provide the current time as an XMLGregorianCalendar
     *
     * @return An XMLGregorianCalendar set to the time the method was called
     */
    public static XMLGregorianCalendar timeNow() {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(new Date().getTime());
        XMLGregorianCalendar xmlGregorianCalendar = null;

        try {
            xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        } catch (DatatypeConfigurationException e) {
            logger.error("Error formatting date",e);
        }
        return xmlGregorianCalendar;
    }

    /**
     * Time from millis xml gregorian calendar.
     *
     * @param timeInMillis the time in millis
     * @return the xml gregorian calendar
     */
    public static XMLGregorianCalendar timeFromMillis(@SuppressWarnings("SameParameterValue") long timeInMillis) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(timeInMillis);
        XMLGregorianCalendar xmlGregorianCalendar = null;

        try {
            xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        } catch (DatatypeConfigurationException e) {
            logger.error("Error formatting date",e);
        }
        return xmlGregorianCalendar;
    }

    /**
     * Time now millis long.
     *
     * @return the long
     */
    public static long timeNowMillis() {
        return new Date().getTime();
    }
}
