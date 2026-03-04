/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://www.bahmni.org/license/mplv2hd.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.bahmni.feed.openerp.domain.encounter;

import org.bahmni.openerp.web.request.builder.Parameter;

public class OpenMRSEncounterEvent {
    protected Parameter createParameter(String name, String value, String type) {
        return new Parameter(name, value, type);
    }

    protected void validateUrls(String feedUri, String feedUrl) {
        if ((feedUrl != null && feedUrl.contains("$param")) || (feedUri != null && feedUri.contains("$param")))
            throw new RuntimeException("Junk values in the feedUrl:$param**");
    }
}
