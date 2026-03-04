/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://www.bahmni.org/license/mplv2hd.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.bahmni.openerp.web;

public class OdooRestException extends RuntimeException{
    public OdooRestException(String message, Throwable cause) {
        super(message, cause);
    }

    public OdooRestException(Throwable cause) {
        super(cause);
    }

    public OdooRestException(String message) {
        super(message);
    }
}
