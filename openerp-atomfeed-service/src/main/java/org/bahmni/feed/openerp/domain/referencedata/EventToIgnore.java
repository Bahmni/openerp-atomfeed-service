package org.bahmni.feed.openerp.domain.referencedata;

import org.bahmni.openerp.web.request.builder.Parameter;

import java.util.ArrayList;
import java.util.List;

public class EventToIgnore implements ERPParameterizable {
    @Override
    public List<Parameter> getParameters(String eventId, String feedURIForLastReadEntry, String feedURI) {
        List<Parameter> parameters = new ArrayList<>();

        parameters.add(new Parameter("category", "event.ignore", "string"));
        parameters.add(new Parameter("feed_uri", feedURI, "string"));
        parameters.add(new Parameter("last_read_entry_id", eventId, "string"));
        parameters.add(new Parameter("feed_uri_for_last_read_entry", feedURIForLastReadEntry, "string"));
        return  parameters;
    }
}
