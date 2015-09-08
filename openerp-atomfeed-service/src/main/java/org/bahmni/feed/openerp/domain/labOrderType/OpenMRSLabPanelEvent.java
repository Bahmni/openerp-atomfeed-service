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
        parameters.add(new Parameter("name", openMRSLabOrderTypeEvent.getName()));
        parameters.add(new Parameter("uuid", openMRSLabOrderTypeEvent.getUuid()));
        parameters.add(new Parameter("is_active", openMRSLabOrderTypeEvent.getActive(), "boolean"));
        parameters.add(new Parameter("category", "create.lab.panel"));
        return parameters;
    }

    @Override
    protected OpenMRSLabPanel readLabOrderTypeEvent(String openMRSLabOrderTypeEventJson) throws IOException {
        return ObjectMapperRepository.objectMapper.readValue(openMRSLabOrderTypeEventJson, OpenMRSLabPanel.class);
    }
}
