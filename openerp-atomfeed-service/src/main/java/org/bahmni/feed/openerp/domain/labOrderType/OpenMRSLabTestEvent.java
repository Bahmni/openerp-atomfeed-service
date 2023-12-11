package org.bahmni.feed.openerp.domain.labOrderType;

import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.ict4h.atomfeed.client.domain.Event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OpenMRSLabTestEvent extends OpenMRSLabOrderTypeEvent<OpenMRSLabTest> {
    public static final String LAB_TEST_EVENT_NAME = "test";

    @Override
    protected List<Parameter> buildParameters(Event event, OpenMRSLabTest openMRSLabOrderTypeEvent) {
        List<Parameter> parameters = new ArrayList<>();
        addToParametersIfNotEmpty(parameters, "name", openMRSLabOrderTypeEvent.getName(), "string");
        addToParametersIfNotEmpty(parameters, "uuid", openMRSLabOrderTypeEvent.getUuid(), "string");
        addToParametersIfNotEmpty(parameters, "is_active", openMRSLabOrderTypeEvent.getActive(), "boolean");
        addToParametersIfNotEmpty(parameters, "category", "create.lab.test", "string");
        return parameters;
    }
    private void addToParametersIfNotEmpty(List<Parameter> parameters, String name, String value, String type) {
        if (value != null && !value.isEmpty()) {
            parameters.add(new Parameter(name, value, type));
        }
    }
    @Override
    protected OpenMRSLabTest readLabOrderTypeEvent(String openMRSLabOrderTypeEventJson) throws IOException {
        return ObjectMapperRepository.objectMapper.readValue(openMRSLabOrderTypeEventJson, OpenMRSLabTest.class);
    }
}
