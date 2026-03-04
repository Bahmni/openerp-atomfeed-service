/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://www.bahmni.org/license/mplv2hd.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.bahmni.feed.openerp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSPersonAttribute {
    private String uuid;
    private Object value;
    private OpenMRSPersonAttributeType attributeType;

    public OpenMRSPersonAttribute(String uuid, Object value, OpenMRSPersonAttributeType attributeType) {
        this.uuid = uuid;
        this.value = value;
        this.attributeType = attributeType;
    }

    public OpenMRSPersonAttribute() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public OpenMRSPersonAttributeType getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(OpenMRSPersonAttributeType attributeType) {
        this.attributeType = attributeType;
    }
}
