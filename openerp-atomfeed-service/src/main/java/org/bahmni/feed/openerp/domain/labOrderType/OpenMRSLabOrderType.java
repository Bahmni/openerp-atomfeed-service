/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://www.bahmni.org/license/mplv2hd.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.bahmni.feed.openerp.domain.labOrderType;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class OpenMRSLabOrderType {

    private String uuid;
    private String name;
    private boolean active;

    @JsonProperty("uuid")
    public String getUuid() {
        return uuid;
    }

    @JsonProperty("id")
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActive() {
        return active ? "1": "0";
    }

    @JsonProperty("isActive")
    public void setActive(boolean active) {
        this.active = active;
    }
}
