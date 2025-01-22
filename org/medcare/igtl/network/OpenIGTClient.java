/*=========================================================================

  Program:   OpenIGTLink Library
  Module:    $HeadURL: http://osfe.org/OpenIGTLink/Source/org/medcare/igtl/network/SocketClient.java $
  Language:  java
  Date:      $Date: 2010-18-14 10:37:44 +0200 (ven., 13 nov. 2009) $
  Version:   $Revision: 0ab$

  Copyright (c) Absynt Technologies Ltd. All rights reserved.

  This software is distributed WITHOUT ANY WARRANTY; without even
  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
  PURPOSE.  See the above copyright notices for more information.

=========================================================================*/
/*=========================================================================
Modifications (by NAMI-THU / TheRisenPhoenix):
    20.11.2024:
        - Adaptation to newer Java versions
        - Refactoring and cleanup
=========================================================================*/

package org.medcare.igtl.network;

import org.medcare.igtl.messages.OpenIGTMessage;
import org.medcare.igtl.util.ErrorManager;
import org.medcare.igtl.util.Header;
import org.medcare.igtl.util.Status;

import javax.net.SocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 * The class can be used by first connecting to a server sending requests
 * comming from the requestQueue, listening for response and adding response to
 * the responsqueue
 * <p>
 *
 * @author <a href="mailto:andleg@osfe.org">Andre Charles Legendre </a>
 * @version 0.1a (09/06/2010)
 */

public abstract class OpenIGTClient extends Thread {
    static Logger logger = Logger.getLogger(OpenIGTClient.class.getName());
    public ErrorManager errorManager;
    private SocketFactory socketFactory;
    private java.net.Socket socket = null;
    private OutputStream outstr;
    private InputStream instr;
    private ResponseQueueManager responseQueue = null;
    private boolean alive = true;
    private String host;
    private int port;
    private Status status;

    /***************************************************************************
     * Default OpenIGTClient constructor.
     *
     * @param host
     *            to be connected
     *
     * @param port
     *            of the host
     * @param errorManager
     *            main class running this client
     * @throws IOException
     * @throws UnknownHostException
     *
     **************************************************************************/
    public OpenIGTClient(String host, int port, ErrorManager errorManager) throws UnknownHostException, IOException {
        super("KnockKnockClient");
        this.host = host;
        this.port = port;
        this.errorManager = errorManager;
        setSocketFactory(SocketFactory.getDefault());

        // not sure if there is better exception method
        try {
            this.socket = socketFactory.createSocket(host, port);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "OpenIGTClient Exception while creating socket", e);
            e.printStackTrace();
            return;
        }


        this.responseQueue = new ResponseQueueManager(errorManager);
        this.responseQueue.start();
        this.outstr = socket.getOutputStream();
        this.instr = socket.getInputStream();
        this.start();
        logger.log(Level.FINE, "OpenIGTClient Client connected and ready");
    }

    /***************************************************************************
     * Reader thread. Reads messages from the socket and add them to the
     * ResponseQueueManager
     *
     **************************************************************************/
    public void run() {
        logger.log(Level.FINE, "OpenIGTClient Starting client Thread");
        try {
            int ret_read = 0;
            byte[] headerBuff = new byte[Header.LENGTH];
            // Log.debug("JE LIS");
            do {
                ret_read = instr.read(headerBuff);
                logger.log(Level.FINE, "OpenIGTClient Size of Header: " + ret_read);

                if (ret_read > 0) {
                    // System.out.print(new String(buff, 0, ret_read));
                    Header header = new Header(headerBuff);

                    int size = (int) header.getBody_size();
                    byte[] bodyBuf = new byte[size];

                    logger.log(Level.FINE, "Size of Packet ::" + size);
                    int currentPos = 0;
                    while (true) {
                        ret_read = instr.read(bodyBuf, currentPos, size);
                        logger.log(Level.FINE, "OpenIGTClient Size of Body: " + ret_read);
                        size = size - ret_read;
                        currentPos = currentPos + ret_read;
                        if (size == 0)
                            break;
                    }

                    if (ret_read > 0) {
                        responseQueue.addResponse(getResponseHandler(header, bodyBuf));
                    }
                }
            } while (isConnected() && ret_read >= 0);
            logger.log(Level.FINE, "OpenIGTClient Client thread exited! Connected state=" + isConnected() + " ret_read = " + ret_read);
        } catch (UnknownHostException e) {
            errorManager.error("OpenIGTClient Don't know about host" + host, e, ErrorManager.OPENIGTCLIENT_UNKNOWNHOST_EXCEPTION);
        } catch (IOException e) {
//            errorManager.error("OpenIGTClient Couldn't get I/O for the connection to: "
//                    + host + " port " + port, e, ErrorManager.OPENIGTCLIENT_IO_EXCEPTION);
        } finally {
            try {
                outstr.close();
            } catch (IOException e) {
                errorManager.error("OpenIGTClient PB outstr.close", e, ErrorManager.OPENIGTCLIENT_IO_EXCEPTION);
            }
            try {
                instr.close();
            } catch (IOException e) {
                errorManager.error("OpenIGTClient PB instr.close", e, ErrorManager.OPENIGTCLIENT_IO_EXCEPTION);
            }
            try {
                socket.close();
            } catch (IOException e) {
                errorManager.error("OpenIGTClient PB socket.close", e, ErrorManager.OPENIGTCLIENT_IO_EXCEPTION);
            }
            this.interrupt();
        }
    }

    public void setSocketFactory(SocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }

    public void sendMessage(OpenIGTMessage message) throws Exception {
        // TODO Auto-generated method stub
        sendMessage(message.getHeader(), message.getBody());
        //System.out.println("Message: Header=" + message.getHeader().toString() + " Body=" + message.getBody().toString());
    }

    public void sendMessage(Header header, byte[] body) throws Exception {
        sendBytes(header.getBytes());
        sendBytes(body);
        logger.log(Level.FINE, "Client Sending Message: Header="
                + header.toString()
                + " Body=" + Arrays.toString(body));
    }

    /***************************************************************************
     * Sends bytes
     * <p>
     *
     * @throws IOException
     *             - Exception in I/O.
     *             <p>
     * @param bytes
     *            - byte[] array.
     **************************************************************************/
    final public synchronized boolean sendBytes(byte[] bytes)
            throws IOException {
        try {
            outstr.write(bytes);
            outstr.flush();
        } catch (Exception e) {
            if (outstr == null)
                errorManager.error("OpenIGTClient sendBytes PB outstr null", e, ErrorManager.OPENIGTCLIENT_EXCEPTION);
            errorManager.error("OpenIGTClient Exception while sendBytes to socket", e, ErrorManager.OPENIGTCLIENT_EXCEPTION);
            return false;
        }
        return true;
    }

    /***************************************************************************
     * Interrupt this thread
     **************************************************************************/
    public void interrupt() {
        alive = false;
        responseQueue.destroy();    // To stop the background threat
        try {
            socket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * ** To set server status
     * **
     *
     * @return the status status
     */
    public Status getStatus() {
        return this.status;
    }

    /**
     * ** To set server status
     *
     * @param status **
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * ** To get response Handler
     *
     * @param header  header of the message received
     * @param bodyBuf byte array of the body of the message received
     *                **
     * @return the response Handler
     */
    public abstract ResponseHandler getResponseHandler(Header header, byte[] bodyBuf);

    public boolean isConnected() {
        return alive && !socket.isClosed();
    }
}
