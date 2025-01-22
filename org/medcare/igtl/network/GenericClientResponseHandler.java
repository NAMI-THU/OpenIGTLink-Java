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

import org.medcare.igtl.messages.OpenIGTMessage;
import org.medcare.igtl.util.Header;

public class GenericClientResponseHandler extends ResponseHandler {

    public OpenIGTMessage openIGTMessage;
    public IOpenIgtPacketListener client;

    public GenericClientResponseHandler(Header header, byte[] body, OpenIGTClient client, IOpenIgtPacketListener openIGTClient) {
        super(header, body, client);
        getCapabilityList().add("GET_CAPABIL");
        getCapabilityList().add("TRANSFORM");
        getCapabilityList().add("POSITION");
        getCapabilityList().add("IMAGE");
        getCapabilityList().add("STATUS");
        getCapabilityList().add("NDARRAY");
        this.client = openIGTClient;
    }

    @Override
    public boolean perform(String messageType) throws Exception {

        //Log.info("Recived IGTLink packet, header="+ getHeader() +" body="+new ByteList(getBody()));

        //http://wiki.ncigt.org/index.php/P41:Prostate:BRP:MRI_New_BRP_OpenIGTLink_Protocol_2012_Mar
        //Should support both TRANSFORM and QTRANSFORM packets

        openIGTMessage = new GenericMessageNodeHandler().perform(messageType, getHeader(), getBody(), client);
        return openIGTMessage != null;
    }


    @Override
    public void manageError(String message, Exception exception, int errorCode) {
        // TODO Auto-generated method stub

    }

}
