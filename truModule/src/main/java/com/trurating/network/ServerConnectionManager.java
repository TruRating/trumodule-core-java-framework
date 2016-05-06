/*
 * @(#)ServerConnectionManager.java
 *
 * Copyright (c) 2016 trurating Limited. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of 
 * trurating Limited. ("Confidential Information").  You shall
 * not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with trurating Limited.
 *
 * TRURATING LIMITED MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT 
 * THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS 
 * FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. TRURATING LIMITED
 * SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT 
 * OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

package com.trurating.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.trurating.properties.ITruModuleProperties;

/**
 * Created by Paul on 08/03/2016.
 */
public class ServerConnectionManager {

    private Socket socket = null;
    private int PORT = 9999;
    private String NODE = "127.0.0.1"; //"40.76.5.14";
    private int TIMEOUT = 1 * 5000;
    private final Logger log = Logger.getLogger(ServerConnectionManager.class);

    public Socket connectToServer(ITruModuleProperties properties) {

        String reason = "";

        // Configure the socket
        if (properties != null) {
        	if (properties.getTruServiceIPAddress().length() > 0)
        		NODE = properties.getTruServiceIPAddress() ;

        	if (properties.getTruServiceSocketPortNumber() > 0)
        		PORT = properties.getTruServiceSocketPortNumber();

        	if (properties.getTruServiceSocketTimeoutInMilliSeconds() > 0)
        		TIMEOUT = properties.getTruServiceSocketTimeoutInMilliSeconds() ;
        }
        
    	if (socket != null) {
    		log.warn("Socket still open from previous transaction");
    		close() ;
    	}
    		
        socket = new Socket();

        try {
            socket.setReuseAddress(true);
            SocketAddress sa = new InetSocketAddress(NODE, PORT);
            socket.connect(sa, TIMEOUT);
            log.info("Connected to truService!");

        } catch (IOException e) {
            if (e.getMessage().equals("Connection refused")) {
                reason = "PORT " + PORT + " on " + NODE + " is closed.";
                return null;
            }
            if (e instanceof UnknownHostException) {
                reason = "NODE " + NODE + " is unresolved.";
                return null;
            }
            if (e instanceof SocketTimeoutException) {
                reason = "timeout while attempting to reach NODE " + NODE + " on PORT " + PORT;
                return null;
            }

        } finally {
            if (socket != null) {
                if (socket.isConnected()) {
                    log.info("Port " + PORT + " on " + NODE + " is reachable!");
                } else {
                    log.info("Port " + PORT + " on " + NODE + " is not reachable; reason: " + reason);
                    return null;
                }
            }
            else
            	log.error ("Failed to create socket") ;
        }
        return socket;
    }
    
    public OutputStream getOutputStream() {
    	try {
			return socket.getOutputStream() ;
		} catch (IOException e) {
			log.error("Exception occurred getting socket output stream: ", e);
		}
    	return null ;
    }

    public InputStream getInputStream() {
        try {
            return socket.getInputStream();
        } catch (IOException e) {
            log.error("Exception occurred getting socket input stream: ", e);
        }
        return  null;
    }
    
    public byte[] readInput() {
        byte[] buffer = null;
        try {
            buffer = new byte[1024 * 2];
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int len = socket.getInputStream().read(buffer);
            os.write(buffer, 0, len);
            os.flush();
            byte[] toReturn = os.toByteArray();
            os.close();
            return toReturn;
        } catch (Exception e) {
            log.error(e);
        }
        return buffer;
    }    
    
    public void close() {    	
        if ((socket != null) && socket.isConnected()) {
        	try {
				socket.close();
	            log.info("ServerConnectionManager closed");
			} catch (Exception e) {
	            log.warn("Exception raised calling to ServerConnectionManager close: ", e);
			}
        } else {
            log.warn("Call to ServerConnectionManager close when the socket is already closed");
        }
        socket = null ;
    }
}
