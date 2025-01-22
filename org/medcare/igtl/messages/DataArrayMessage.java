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
import org.medcare.igtl.util.Matrix3D;

public class DataArrayMessage extends OpenIGTMessage {
    /**
     * This is a stub class for how the Array Data message is to be implemented
     *
     * @param head
     * @param body
     * @throws Exception
     */
    public DataArrayMessage(Header head, byte[] body) throws Exception {
        super(head, body);
    }

    @Override
    public boolean unpackBody() throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public byte[] packBody() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return null;
    }

    public Matrix3D getDataMatrix() {
        // TODO Auto-generated method stub
        return null;
    }

}
