/*
 *
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
 *
 */
package com.trurating.trumodule.mocks;

import com.trurating.service.v230.xml.Font;
import com.trurating.service.v230.xml.Format;
import com.trurating.service.v230.xml.RequestPeripheral;
import com.trurating.service.v230.xml.UnitDimension;
import com.trurating.trumodule.device.IReceiptManager;

public class MockReceiptManger implements IReceiptManager {

    @Override
    public RequestPeripheral getReceiptCapabilities() {
        RequestPeripheral requestPeripheral = new RequestPeripheral();
        requestPeripheral.setFont(Font.MONOSPACED);
        requestPeripheral.setFormat(Format.RAW);
        requestPeripheral.setHeight((short)20);
        requestPeripheral.setWidth((short)16);
        requestPeripheral.setSeparator("|");
        requestPeripheral.setUnit(UnitDimension.LINE);
        return requestPeripheral;
    }

    @Override
    public void appendReceipt(String value) {
        System.out.println("IRECEIPTMANAGER: " + value);
    }
}
