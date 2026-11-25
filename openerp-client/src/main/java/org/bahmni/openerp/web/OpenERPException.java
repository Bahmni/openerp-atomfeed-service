/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://www.bahmni.org/license/mplv2hd.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.bahmni.openerp.web;

public class OpenERPException extends RuntimeException {
    public OpenERPException(String message, Throwable cause) {
        super(message, cause);
    }

    public OpenERPException(Throwable cause) {
        super(cause);
    }

    public OpenERPException(String message) {
        super(message);
    }
}