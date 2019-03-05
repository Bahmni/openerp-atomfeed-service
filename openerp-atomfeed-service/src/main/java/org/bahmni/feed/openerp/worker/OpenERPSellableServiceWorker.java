package org.bahmni.feed.openerp.worker;

import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.feed.openerp.client.OpenMRSWebClient;
import org.bahmni.feed.openerp.domain.OpenMRSResource;
import org.bahmni.openerp.web.client.OpenERPClient;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class OpenERPSellableServiceWorker implements EventWorker {

    private OpenERPClient openERPClient;
    private String feedUrl;
    private OpenMRSWebClient openMRSWebClient;
    private String urlPrefix;


    public OpenERPSellableServiceWorker(String feedUrl, OpenERPClient openERPClient, OpenMRSWebClient openMRSWebClient, String urlPrefix) {
        this.openERPClient = openERPClient;
        this.feedUrl = feedUrl;
        this.openMRSWebClient = openMRSWebClient;
        this.urlPrefix = urlPrefix;
    }

    @Override
    public void process(Event event) {
        try {
            openERPClient.execute(mapToOpenERPRequest(event));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cleanUp(Event event) {

    }

    private OpenERPRequest mapToOpenERPRequest(Event event) throws IOException {
        String resourceJson = openMRSWebClient.get(URI.create(urlPrefix + event.getContent()));
        OpenMRSResource resource = ObjectMapperRepository.objectMapper.readValue(resourceJson, OpenMRSResource.class);
        List<Parameter> parameters = buildParameters(resource);

        return new OpenERPRequest("atom.event.worker", "process_event", parameters);
    }

    private List<Parameter> buildParameters(OpenMRSResource resource) {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter("name", resource.getName()));
        return parameters;
    }
}
