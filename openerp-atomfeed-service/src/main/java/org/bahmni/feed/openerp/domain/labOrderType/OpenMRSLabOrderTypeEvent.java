package org.bahmni.feed.openerp.domain.labOrderType;

import org.bahmni.feed.openerp.domain.labOrderType.OpenMRSLabOrderType;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.ict4h.atomfeed.client.domain.Event;

import java.io.IOException;
import java.util.List;

public abstract class OpenMRSLabOrderTypeEvent<T extends OpenMRSLabOrderType> {
    protected abstract List<Parameter> buildParameters(Event event, T openMRSLabOrderTypeEvent);

    protected abstract T readLabOrderTypeEvent(String openMRSLabOrderTypeEventJson) throws IOException;

    public OpenERPRequest mapEventToOpenERPRequest(Event event, String openMRSLabOrderTypeEventJson, String feedUrl) throws IOException {
        T labOrderType = readLabOrderTypeEvent(openMRSLabOrderTypeEventJson);
        List<Parameter> parameters = addAtomFeedParameters(event, feedUrl, buildParameters(event, labOrderType));
        return new OpenERPRequest("atom.event.worker", "process_event", parameters);
    }

    private List<Parameter> addAtomFeedParameters(Event event, String feedUrl, List<Parameter> parameters) {
        parameters.add(new Parameter("last_read_entry_id",event.getId()));
        parameters.add(new Parameter("feed_uri_for_last_read_entry",event.getFeedUri()));
        parameters.add(new Parameter("feed_uri", feedUrl, "string"));
        if (event.getFeedUri() == null) {
            parameters.add(new Parameter("is_failed_event","1","boolean"));
        }
        return parameters;
    }
}
