package com.smartbear.readyapi4j.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;

public class PortFinder {
    private static final int PORT_MAX = 65535;
    private static Logger logger = LoggerFactory.getLogger(PortFinder.class);

    public static int portFinder(int firstPort) {
        int port = firstPort;
        ServerSocket serverSocket = null;
        while (port <= PORT_MAX) {
            try {
                serverSocket = new ServerSocket(port);
                return port;
            } catch (IOException ex) {
                port++;
            } finally {
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        // Should never happen
                        logger.error("Could not close serversocket while finding free ports");
                    }
                }
            }
        }
        throw new IllegalStateException("Could not find any free ports for local server");
    }

}
