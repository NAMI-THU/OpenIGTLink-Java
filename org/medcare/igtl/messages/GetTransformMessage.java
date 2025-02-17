/*=========================================================================

  Program:   OpenIGTLink Library
  Module:    $HeadURL: http://osfe.org/OpenIGTLink/Source/org/medcare/igtl/messages/ImageMessage.java $
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

package org.medcare.igtl.messages;

import org.medcare.igtl.util.Header;

/**
 * ** This class create a GetTransform object from bytes received or help to generate
 * bytes to send from it
 *
 * @author Andre Charles Legendre
 */
public class GetTransformMessage extends OpenIGTMessage {

    /**
     * ** Constructor to be used to create message to send them with this
     * constructor you must use method CreateBody and then
     * getBytes to send them
     * **
     *
     * @param deviceName Device Name
     **/
    public GetTransformMessage(String deviceName) {
        super(deviceName);
    }

    /**
     * ** Constructor to be used to create message from received data
     *
     * @param header
     * @param body
     * @throws Exception
     */
    public GetTransformMessage(Header header, byte[] body) throws Exception {
        super(header, body);
    }

    /**
     * ** To create body to get bytes to send
     * <p>
     * **
     *
     * @return the bytes array containing the body
     **/
    @Override
    public byte[] packBody() {
        setBody(new byte[0]);
        setHeader(new Header(VERSION, "GET_TRANS", deviceName, getBody()));
        return getBytes();
    }

    /**
     * ** To create body from body array
     * <p>
     * **
     *
     * @return true if unpacking is ok
     */
    @Override
    public boolean unpackBody() {
        return true;
    }

    /**
     * ** To get the get Transform String
     * **
     *
     * @return the get Transform String
     */
    @Override
    public String toString() {
        return "GET_TRANSFORM Device Name           : " + getDeviceName();
    }
}

