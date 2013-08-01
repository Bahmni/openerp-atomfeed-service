package org.bahmni.feed.openerp.event;

import org.bahmni.openerp.web.client.OpenERPClient;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.codehaus.jackson.map.ObjectMapper;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OpenERPCustomerServiceEventWorker implements EventWorker {
    OpenERPClient openERPClient;
    private String feedUrl;

    public OpenERPCustomerServiceEventWorker(String feedUrl,OpenERPClient openERPClient) {
        this.feedUrl = feedUrl;
        this.openERPClient = openERPClient;
    }

    @Override
    public void process(Event event) {
        try {
            openERPClient.execute(mapRequest(event));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cleanUp(Event event) {
    }

    private OpenERPRequest mapRequest(Event event) throws IOException {
        List<Parameter> parameters = new ArrayList<Parameter>();

        HashMap<String,Object> paramMap = new ObjectMapper().readValue(event.getContent(), HashMap.class) ;

        parameters.add(createParameter("name",(String)paramMap.get("name"),"string"));
        parameters.add(createParameter("ref",(String)paramMap.get("ref"),"string"));
        parameters.add(createParameter("village", (String)paramMap.get("village"), "string"));

        parameters.add(createParameter("category", "create.customer", "string"));
        parameters.add(createParameter("feed_uri", feedUrl, "string"));
        parameters.add(createParameter("last_read_entry_id", event.getId(), "string"));
        parameters.add(createParameter("feed_uri_for_last_read_entry", event.getFeedUri(), "string"));

        return new OpenERPRequest("atom.event.worker","process_event",parameters);
    }

    private Parameter createParameter(String name, String value, String type) {
        return new Parameter(name, value, type);
    }

}
