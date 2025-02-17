/*=========================================================================

  Program:   OpenIGTLink Library
  Module:    $HeadURL: http://osfe.org/OpenIGTLink/Source/org/medcare/igtl/util/header.java $
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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ** For reading/writing OpenIGTLink headers
 *
 * @author Andre Charles Legendre
 **/

public class Header {
    private static final Logger logger = Logger.getLogger(Header.class.getName());
    public static int LENGTH = 58;
    private int base = 1000000000; /* 10^9 */
    private long version; // unsigned int 16bits
    private String dataType; // char 12 bits
    private String deviceName; // char 20 bits
    private static long timestamp; // unsigned int 64 bits
    private long body_size; // unsigned int 64 bits
    private long crc; // unsigned int 64 bits
    private BytesArray bytesArray;
    private String type = "";
    private String name = "";

    // ------------------------------------------------------------------------

    /**
     * ** Destination Constructor
     * **
     *
     * @param version   ; // Version number unsigned int 16bits
     * @param _type     ; // Type name of data char 12 bits
     * @param _name     ; // Unique device name char 20 bits
     * @param timestamp ; // TimeStamp or 0 if unused unsigned int 64 bits
     * @param body_size ; // Size of body in bytes unsigned int 64 bits
     * @param crc       ; // 64 bit CRC for body data unsigned int 64 bits
     **/
    public Header(long version, String _type, String _name, long timestamp, long body_size, long crc) {
        bytesArray = new BytesArray();
        this.type = _type;
        this.version = version;
        this.name = _name;
        bytesArray.putLong(version, 2);
        byte[] typeArray = new byte[12];
        for (int m = 0; m < 12; m++) {
            typeArray[m] = 0;
        }
        dataType = new String(typeArray, bytesArray.charset);
        int len = _type.length();
        len = Math.min(len, 12);
        System.arraycopy(typeArray, 0, _type.toCharArray(), 0, len);
        bytesArray.putString(new String(typeArray));
        byte[] nameArray = new byte[20];
        for (int m = 0; m < 20; m++) {
            nameArray[m] = 0;
        }
        len = _name.length();
        len = Math.min(len, 20);
        System.arraycopy(nameArray, 0, _name.toCharArray(), 0, len);
        bytesArray.putString(new String(nameArray));
        Header.timestamp = timestamp;
        bytesArray.putTimeStamp(timestamp);
        this.body_size = body_size;
        bytesArray.putLong(body_size, 8);
        this.crc = crc;
        bytesArray.putLong(crc, 8);
        populate();
    }

    // ------------------------------------------------------------------------

    /**
     * ** Destination Constructor
     * **
     *
     * @param version    ; // Version number unsigned int 16bits
     * @param dataType   ; // Type name of data char 12 bits
     * @param deviceName ; // Unique device name char 20 bits
     * @param body       ; // body in bytes
     **/
    public Header(long version, String dataType, String deviceName, byte[] body) {
        bytesArray = new BytesArray();
        this.version = version;
        bytesArray.putULong(version, 2);
        byte[] typeArray = new byte[12];
        for (int m = 0; m < 12; m++) {
            typeArray[m] = 0;
        }
        int len = dataType.length();
        //byte data[] = dataType.getBytes(bytesArray.charset);
        //Log.debug("len : " + len + " data len : " + data.length);
        if (len > 12)
            len = 12;
        System.arraycopy(dataType.getBytes(bytesArray.charset), 0, typeArray, 0, len);
        bytesArray.putBytes(typeArray);
        byte[] nameArray = new byte[20];
        for (int m = 0; m < 20; m++) {
            nameArray[m] = 0;
        }
        len = deviceName.length();
        if (len > 20)
            len = 20;
        System.arraycopy(deviceName.getBytes(bytesArray.charset), 0, nameArray, 0, len);
        bytesArray.putBytes(nameArray);
        timestamp = bytesArray.putTimeStamp();

        this.body_size = body.length;
        bytesArray.putULong(body_size, 8);
        this.crc = bytesArray.putCrc(body, body.length, 0L);
        populate();

    }

    // ------------------------------------------------------------------------

    /**
     * ** Destination Constructor
     * **
     *
     * @param bytes bytes doit contenir 58 bytes of the header
     **/
    public Header(byte[] bytes) {
        bytesArray = new BytesArray();
        bytesArray.putBytes(bytes);
        populate();
        logger.log(Level.FINE, "New header: " + this);
    }

    private void populate() {
        version = bytesArray.getLong(2); // unsigned int 16bits
        dataType = bytesArray.getString(12); // char 12 bits
        deviceName = bytesArray.getString(20); // char 20 bits
        timestamp = bytesArray.decodeTimeStamp(bytesArray.getBytes(8));
        body_size = bytesArray.getLong(8); // unsigned int 64 bits
        crc = bytesArray.getLong(8); // unsigned int 64 bits
    }

    // ------------------------------------------------------------------------

    /**
     * ** Version number.
     * **
     *
     * @return The current version of the bytesArray
     **/
    public long getVersion() {
        return this.version;
    }

    // ------------------------------------------------------------------------

    /**
     * ** Type name of data
     * **
     *
     * @return The type of the device
     **/
    public String getDataType() {
        return this.dataType;
    }

    // ------------------------------------------------------------------------

    /**
     * ** Unique device name.
     * **
     *
     * @return The current name of the device
     **/
    public String getDeviceName() {
        return this.deviceName;
    }

    // ------------------------------------------------------------------------

    /**
     * ** TimeStamp or 0 if unused.
     * **
     *
     * @return The time stamp at the creation of the header
     **/
    public static double getTimeStamp() {
        return timestamp;
    }

    // ------------------------------------------------------------------------

    /**
     * ** Size of body in bytes.
     * **
     *
     * @return The current body_size of the bytesArray
     **/
    public long getBody_size() {
        return this.body_size;
    }

    // ------------------------------------------------------------------------

    /**
     * ** 64 bit CRC for body data.
     * **
     *
     * @return The current crc of the bytesArray
     **/
    public long getCrc() {
        return this.crc;
    }

    // ------------------------------------------------------------------------

    /**
     * ** ByteArray which contain header bytes.
     * **
     *
     * @return The current bytesArray
     **/
    public BytesArray getBytesArray() {
        return this.bytesArray;
    }
    // ------------------------------------------------------------------------

    /**
     * ** this header bytes.
     * **
     *
     * @return A copy of the byte array currently in the bytesArray (as-is)
     **/
    public byte[] getBytes() {
        return this.bytesArray.getBytes();
    }

    @Override
    public String toString() {
        String s = "";
        s += "Version: " + getVersion();
        s += " Type: " + getDataType();
        s += " Name: " + getDeviceName();
        s += " Timestamp: " + getTimeStamp();
        s += " Body Size:" + getBody_size();
        s += " CRC: " + getCrc();

        //s+=" bytes: "+new ByteList(getBytes());
        return s;
    }
}

