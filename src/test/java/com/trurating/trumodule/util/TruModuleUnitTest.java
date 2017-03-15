
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

import java.lang.reflect.Field;
import java.net.MalformedURLException;

/**
 * Created by andrewdouglas on 23/01/2017.
 */
public class TruModuleUnitTest {
    protected void testSetValue(Object obj, String fieldName, Object value) throws MalformedURLException, NoSuchFieldException, IllegalAccessException {
        final Field field = fetchField(obj.getClass(),fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    protected Object testGetValue(Object obj, String fieldName) throws MalformedURLException, NoSuchFieldException, IllegalAccessException {
        final Field field = fetchField(obj.getClass(),fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    private Field fetchField(Class clas, String fieldName){
        Field field = null;
        try {
            field = clas.getDeclaredField(fieldName);
        }
        catch(NoSuchFieldException e){
            while(clas.getSuperclass()!=null && field == null){ // we don't want to process Object.class
                // do something with current's fields
                field = fetchField(clas.getSuperclass(), fieldName);
            }
        }
        return field;
    }
}
