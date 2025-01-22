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

package org.medcare.igtl.network;

import org.medcare.igtl.messages.*;
import org.medcare.igtl.util.Header;
import org.medcare.igtl.util.Quaternion;
import org.medcare.igtl.util.TransformNR;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GenericMessageNodeHandler {
    static Logger logger = Logger.getLogger(GenericMessageNodeHandler.class.getName());
    public OpenIGTMessage openIGTMessage;

    public OpenIGTMessage perform(String messageType, Header head, byte[] body, IOpenIgtPacketListener node) throws Exception {
        //Log.debug("perform messageType : " + messageType);
        //Log.info("Recived IGTLink packet, header="+head+" body="+new ByteList( body));
        //TODO - GSF: Need to add complete set of new IGTLInk commands for BRP robot
        //http://wiki.ncigt.org/index.php/P41:Prostate:BRP:MRI_New_BRP_OpenIGTLink_Protocol_2012_Mar
        //Should support both TRANSFORM and QTRANSFORM packets
        logger.log(java.util.logging.Level.FINE, "perform messageType : " + messageType);
        if (messageType.equals("TRANSFORM")) {
            openIGTMessage = new TransformMessage(head, body);
            TransformMessage transform = (TransformMessage) openIGTMessage;
            transform.Unpack();
            // Position vector and rotation matrix from the received transform
            double[] position = transform.getPosition();
            double[][] rotation = transform.getRotationMatrixArray();
            TransformNR t = new TransformNR(position, rotation);
            node.onRxTransform(openIGTMessage.getDeviceName(), t);
        } else if (messageType.equals("GET_TRANS")) {
            openIGTMessage = new TransformMessage(head, body);
            TransformMessage transform = (TransformMessage) openIGTMessage;
            transform.Unpack();
            // Position vector and rotation matrix from the received transform
            node.getTxTransform(openIGTMessage.getDeviceName());
        } else if (messageType.equals("POSITION") || messageType.equals("MOVE_TO")) {
            logger.log(java.util.logging.Level.FINE, "perform POSITION");
            openIGTMessage = new PositionMessage(head, body);
            PositionMessage transform = (PositionMessage) openIGTMessage;
            transform.Unpack();
            // Position vector and rotation matrix from the received transform
            double[] position = transform.getPosition();
            Quaternion rotation = transform.getQuaternion();
            TransformNR t = new TransformNR(position, rotation);
            //TODO Nirav- This seems wrong, it should be calling getTxTransform() changing it
            node.onRxTransform(openIGTMessage.getDeviceName(), t);
        } else if (messageType.equals("IMAGE")) {
            ImageMessage imgMesg = new ImageMessage(head, body);
            openIGTMessage = imgMesg;
            imgMesg.Unpack();
            node.onRxImage(openIGTMessage.getDeviceName(), imgMesg);

        } else if (messageType.equals("ARRAY")) {
            logger.log(Level.SEVERE, "This method is not complete");
            DataArrayMessage datMesg = new DataArrayMessage(head, body);
            openIGTMessage = datMesg;
            node.onRxDataArray(openIGTMessage.getDeviceName(), datMesg.getDataMatrix());// this is a non functional stub

        } else if (messageType.equals("STRING")) {
            StringMessage strMsg = new StringMessage(head, body);
            strMsg.unpackBody();
            openIGTMessage = strMsg;
            /*Log.error("This method is not complete");
            DataArrayMessage datMesg = new DataArrayMessage(head, body);
            openIGTMessage =(OpenIGTMessage)datMesg;
            Log.debug("Received String Message: Header=" + datMesg.getHeader() + " and Data=" + datMesg.getBody());
            */
            //For string message we have to skip first 4 bytes as 0-1:ENcodinng and 1-2:Length
            //currently using only US-ASCII encoding and we don't need length
            String msgBody = strMsg.getMessage(); //new String(body,4,body.length-4,"US-ASCII");

            node.onRxString(openIGTMessage.getDeviceName(), msgBody);// this is a non functional stub
        } else if (messageType.equals("GET_STATUS")) {
            StatusMessage statMsg = new StatusMessage(head, body);
            statMsg.unpackBody();
            openIGTMessage = statMsg;
            System.out.println("Received Get_Status request");
            node.onGetStatus(openIGTMessage.getDeviceName());// this is a non functional stub
        } else if (messageType.equals("NDARRAY")) {
            NDArrayMessage ndArrayMsg = new NDArrayMessage(head, body);
            ndArrayMsg.unpackBody();
            openIGTMessage = ndArrayMsg;
            node.onRxNDArray(openIGTMessage.getDeviceName(), ndArrayMsg.get1DFloatData());// this is a non functional stub
        } else {

            logger.log(Level.WARNING, "Message Type : " + messageType + " not implemented");
            return null;
        }
        return openIGTMessage;
    }
}
