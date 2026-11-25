/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://www.bahmni.org/license/mplv2hd.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.bahmni.feed.openerp.domain.encounter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSOrderAttributeResponse {
    private List<OpenMRSOrderAttribute> results;

    public List<OpenMRSOrderAttribute> getResults() {
        return results;
    }

    public void setResults(List<OpenMRSOrderAttribute> results) {
        this.results = results;
    }
}
