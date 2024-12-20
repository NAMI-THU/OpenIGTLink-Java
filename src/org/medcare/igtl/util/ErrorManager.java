/*=========================================================================

  Program:   OpenIGTLink Library
  Module:    $HeadURL: http://osfe.org/OpenIGTLink/Source/org/medcare/igtl/network/SocketClientFactory.java $
  Language:  java
  Date:      $Date: 2010-08-14 10:37:44 +0200 (ven., 13 nov. 2009) $
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
package org.medcare.igtl.util;

import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public abstract class ErrorManager {

    public final static int RESPONSE_NOT_MANAGED = 0;
    public static final int RESPONSE_CRC_EXCEPTION = 1;
    public static final int RESPONSE_EXCEPTION = 2;
    public static final int REQUEST_EXCEPTION = 3;
    public static final int REQUEST_INTERRUPT_EXCEPTION = 4;
    public static final int RESPONSE_INTERRUPTED_EXCEPTION = 5;
    public static final int MESSAGE_NOT_MANAGED = 6;
    public static final int MESSAGE_CRC_EXCEPTION = 7;
    public static final int MESSAGE_EXCEPTION = 8;
    public static final int MESSAGE_INTERRUPTED_EXCEPTION = 9;
    public static final int SERVERTHREAD_ABNORMAL_ANSWER = 10;
    public static final int OPENIGTCLIENT_UNKNOWNHOST_EXCEPTION = 11;
    public static final int OPENIGTCLIENT_IO_EXCEPTION = 12;
    public static final int OPENIGTCLIENT_EXCEPTION = 13;
    public static final int OPENIGTSERVER_IO_EXCEPTION = 14;
    public static final int REQUEST_RESULT_ERROR = 15;
    public static final int REQUEST_ERROR = 16;
    public static final int APPLICATION_UNKNOWNHOST_EXCEPTION = 17;
    public static final int APPLICATION_IO_EXCEPTION = 18;
    public static final int APPLICATION_EXCEPTION = 19;
    public static final int RESPONSE_PB_RESULT = 20;
    public static final int MESSAGE_ERROR = 21;
    public static final int RESPONSE_ERROR = 22;
    public static final int MESSAGE_PB_RESULT = 23;
    public static final int MESSAGE_HANDLER_ERROR = 24;
    public static final int RESPONSE_HANDLER_ERROR = 25;
    public static final int REQUEST_TYPE_NOT_FOUND = 26;
    public static final int SERVERTHREAD_IO_EXCEPTION = 27;
    public LogManager lm;
    public FileHandler fh;
    public Logger logger;

    /***************************************************************************
     * Default ErrorManager constructor.
     *
     **************************************************************************/
    public ErrorManager() {
        //Default constructor
    }

    /**
     * Method to manage errors
     *
     * @param message   message to log
     * @param exception exception to log
     * @param errorCode errorCode to log
     */
    public abstract void error(String message, Exception exception, int errorCode);

    /**
     * Method to get Logger used by this ErrorManager
     *
     * @return the Logger
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Method to set Logger used by this ErrorManager
     *
     * @param logger the Logger
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
