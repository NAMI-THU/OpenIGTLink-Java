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

public class CrcException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 8854925645976405597L;

    /**
     * Constructs a FormatException with no detail message.
     */
    public CrcException() {
        super();
    }

    /**
     * Constructs a FormatException with the specified detail message
     *
     * @param s the detail message
     */
    public CrcException(String s) {
        super(s);
    }
}

