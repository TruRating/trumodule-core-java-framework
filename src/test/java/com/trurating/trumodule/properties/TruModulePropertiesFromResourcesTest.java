
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

public class TruModulePropertiesFromResourcesTest extends TruModuleUnitTest{
    @Test
    public void TruModulePropertiesFromResources() throws Exception {
        TruModuleProperties obj = new TruModulePropertiesFromResources();
        Assert.assertEquals(1,Integer.parseInt((String) testGetValue(obj,"partnerId")));
        Assert.assertEquals(new URL("http://tru-sand-service-v200.trurating.com/api/servicemessage"),testGetValue(obj,"truServiceURL"));
        Assert.assertEquals(150,testGetValue(obj,"socketTimeoutInMilliSeconds"));
    }

    @Test
    public void loadAllPropertiesFromResourcesSystemArg() throws Exception {
        TruModuleProperties obj = new TruModulePropertiesFromResources("properties/truModule.properties");
        Assert.assertEquals(1,Integer.parseInt((String) testGetValue(obj,"partnerId")));
        Assert.assertEquals(new URL("http://tru-sand-service-v200.trurating.com/api/servicemessage"),testGetValue(obj,"truServiceURL"));
        Assert.assertEquals(150,testGetValue(obj,"socketTimeoutInMilliSeconds"));
    }

}