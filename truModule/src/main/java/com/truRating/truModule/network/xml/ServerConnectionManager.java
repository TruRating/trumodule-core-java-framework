package com.truRating.truModule.network.xml;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.*;

/**
 * Created by Paul on 08/03/2016.
 */
public class ServerConnectionManager {

    private final int PORT = 9999;
    private final String NODE = "40.76.5.14";
    private final int TIMEOUT = 1 * 5000;
    private final Logger log = Logger.getLogger(ServerConnectionManager.class);

    public Socket connectToServer() {

        String reason = "";
        Socket s = new Socket();

        try {
            s.setReuseAddress(true);
            SocketAddress sa = new InetSocketAddress(NODE, PORT);
            s.connect(sa, TIMEOUT);
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
            if (s != null) {
                if (s.isConnected()) {
                    log.info("Port " + PORT + " on " + NODE + " is reachable!");
                } else {
                    log.info("Port " + PORT + " on " + NODE + " is not reachable; reason: " + reason);
                    return null;
                }

            }
        }
        return s;
    }
}
