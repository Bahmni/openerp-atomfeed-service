/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://www.bahmni.org/license/mplv2hd.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.bahmni.feed.openerp.domain.labOrderType;

import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.ict4h.atomfeed.client.domain.Event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OpenMRSLabPanelEvent extends OpenMRSLabOrderTypeEvent<OpenMRSLabPanel> {
    public static final String LAB_PANEL_EVENT_NAME = "panel";

    @Override
    protected List<Parameter> buildParameters(Event event, OpenMRSLabPanel openMRSLabOrderTypeEvent) {
        List<Parameter> parameters = new ArrayList<>();
        addToParametersIfNotEmpty(parameters, "name", openMRSLabOrderTypeEvent.getName(),"string");
        addToParametersIfNotEmpty(parameters, "uuid", openMRSLabOrderTypeEvent.getUuid(),"string");
        addToParametersIfNotEmpty(parameters, "is_active", openMRSLabOrderTypeEvent.getActive(),"boolean");
        addToParametersIfNotEmpty(parameters, "category", "create.lab.panel","string");
        return parameters;
    }
    private void addToParametersIfNotEmpty(List<Parameter> parameters, String name, String value, String type) {
        if (value != null && !value.isEmpty()) {
            parameters.add(new Parameter(name, value, type));
        }
    }
    @Override
    protected OpenMRSLabPanel readLabOrderTypeEvent(String openMRSLabOrderTypeEventJson) throws IOException {
        return ObjectMapperRepository.objectMapper.readValue(openMRSLabOrderTypeEventJson, OpenMRSLabPanel.class);
    }
}
