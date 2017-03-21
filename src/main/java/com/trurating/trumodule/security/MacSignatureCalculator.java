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

package com.trurating.trumodule.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

/**
 * The type Mac signature calculator.
 */
public class MacSignatureCalculator {
    private static final int API_ENCRYPTION_SCHEME = 3;
    final private static Logger logger = LoggerFactory.getLogger(MacSignatureCalculator.class);
    final private static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /**
     * Calculate mac byte [ ].
     *
     * @param requestBody  the request body
     * @param transportKey the transport key
     * @return the byte [ ]
     */
    public static byte[] calculateMac(byte[] requestBody, String transportKey) {
        logger.debug("Calculating MAC using transport key: {}",transportKey);
        try {
            DESedeKeySpec keySpec = new DESedeKeySpec(transportKey.getBytes("UTF-8"));
            SecretKey key = SecretKeyFactory.getInstance("DESede").generateSecret(keySpec);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cypherOut = cipher.doFinal(md.digest(requestBody));
            return bytesToHex(cypherOut).getBytes("UTF-8");
        } catch (GeneralSecurityException e) {
            logger.error("Error calculating MAC",e);
        } catch (UnsupportedEncodingException e) {
            logger.error("Error calculating MAC",e);
        }
        return null;
    }

    /**
     * Get api encryption scheme int.
     *
     * @return the int
     */
    public static int getApiEncryptionScheme() {
        return API_ENCRYPTION_SCHEME;
    }

    // java.xml.bind.DatatypeConverter.printHexBinary() is not available in android so here is a new version
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
