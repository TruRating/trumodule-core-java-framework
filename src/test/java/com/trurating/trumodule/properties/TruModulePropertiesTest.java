
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

package com.trurating.trumodule.properties;

import com.trurating.trumodule.util.TruModuleUnitTest;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;

public class TruModulePropertiesTest extends TruModuleUnitTest {


    @Test
    public void getPartnerId() throws Exception {
        String value = "1234567890";
        TruModuleProperties obj = new TruModuleProperties();
        testSetValue(obj, "partnerId", value);
        Assert.assertEquals(value,obj.getPartnerId());
    }

    @Test
    public void setPartnerId() throws Exception {
        String value = "1234567890";
        TruModuleProperties obj = new TruModuleProperties();
        obj.setPartnerId(value);
        Assert.assertEquals(value,testGetValue(obj,"partnerId"));
    }

    @Test
    public void getTransportKey() throws Exception {
        String value = "123456789012345678901234";
        TruModuleProperties obj = new TruModuleProperties();
        testSetValue(obj, "transportKey", value);
        Assert.assertEquals(value,obj.getTransportKey());
    }

    @Test
    public void setTransportKey() throws Exception {
        String value = "123456789012345678901234";
        TruModuleProperties obj = new TruModuleProperties();
        obj.setTransportKey(value);
        Assert.assertEquals(value,testGetValue(obj,"transportKey"));
    }

    @Test
    public void getSocketTimeoutInMilliSeconds() throws Exception {
        int value = 100;
        TruModuleProperties obj = new TruModuleProperties();
        testSetValue(obj, "socketTimeoutInMilliSeconds", value);
        Assert.assertEquals(value,obj.getSocketTimeoutInMilliSeconds());
    }

    @Test
    public void setSocketTimeoutInMilliSeconds() throws Exception {
        int value = 100;
        TruModuleProperties obj = new TruModuleProperties();
        obj.setSocketTimeoutInMilliSeconds(value);
        Assert.assertEquals(value,testGetValue(obj,"socketTimeoutInMilliSeconds"));
    }

    @Test
    public void getTruServiceURL() throws Exception {
        URL value = new URL("http://tru-sand-service-v200.trurating.com/api");
        TruModuleProperties obj = new TruModuleProperties();
        testSetValue(obj, "truServiceURL", value);
        Assert.assertEquals(value,obj.getTruServiceURL());
    }

    @Test
    public void setTruServiceURL() throws Exception {
        URL value = new URL("http://tru-sand-service-v200.trurating.com/api");
        TruModuleProperties obj = new TruModuleProperties();
        obj.setTruServiceURL(value);
        Assert.assertEquals(value,testGetValue(obj,"truServiceURL"));
    }

}