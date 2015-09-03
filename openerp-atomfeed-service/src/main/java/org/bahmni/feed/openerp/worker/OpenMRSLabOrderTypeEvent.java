package org.bahmni.feed.openerp.worker;

import org.bahmni.feed.openerp.domain.labOrderType.OpenMRSLabOrderType;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.ict4h.atomfeed.client.domain.Event;

import java.io.IOException;
import java.util.List;

public abstract class OpenMRSLabOrderTypeEvent<T extends OpenMRSLabOrderType> {
    protected abstract List<Parameter> buildParameters(Event event, T openMRSLabOrderTypeEvent);

    protected abstract T readLabOrderTypeEvent(String openMRSLabOrderTypeEventJson) throws IOException;

    public OpenERPRequest mapEventToOpenERPRequest(Event event, String openMRSLabOrderTypeEventJson) throws IOException {
        T labOrderType = readLabOrderTypeEvent(openMRSLabOrderTypeEventJson);
        return new OpenERPRequest("atom.event.worker", "process_event", buildParameters(event, labOrderType));
    }
}
