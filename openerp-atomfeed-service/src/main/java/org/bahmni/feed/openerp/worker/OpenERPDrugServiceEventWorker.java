package org.bahmni.feed.openerp.worker;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.feed.openerp.domain.OpenMRSDrug;
import org.bahmni.feed.openerp.client.OpenMRSWebClient;
import org.bahmni.openerp.web.client.strategy.OpenERPContext;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class OpenERPDrugServiceEventWorker implements EventWorker {

    private static final Logger logger = LogManager.getLogger(OpenERPDrugServiceEventWorker.class);

    private final OpenERPContext openERPContext;
    private String feedUrl;

    private final String endpointURI;

    private final OpenMRSWebClient webClient;
    private final String urlPrefix;


    public OpenERPDrugServiceEventWorker(String feedUrl, String endpointURI, OpenERPContext openERPContext, OpenMRSWebClient openMRSWebClient, String urlPrefix) {
        this.openERPContext = openERPContext;
        this.feedUrl = feedUrl;
        this.endpointURI = endpointURI;
        this.webClient = openMRSWebClient;
        this.urlPrefix = urlPrefix;
    }

    @Override
    public void process(Event event) {
        logger.debug("Processing the event [{}]", event.getContent());
        try {
            openERPContext.execute(mapRequest(event), endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private OpenERPRequest mapRequest(Event event) throws IOException {
        String drugJson = webClient.get(URI.create(urlPrefix + event.getContent()));
        OpenMRSDrug drug = ObjectMapperRepository.objectMapper.readValue(drugJson, OpenMRSDrug.class);
        return new OpenERPRequest("atom.event.worker", "process_event", buildParameters(event,drug));
    }

    private List<Parameter> buildParameters(Event event, OpenMRSDrug drug) {
        List<Parameter> parameters = new ArrayList<>();
        addToParametersIfNotEmpty(parameters, "name", drug.getName());
        addToParametersIfNotEmpty(parameters, "shortName", drug.getShortName());
        addToParametersIfNotEmpty(parameters, "uuid", drug.getUuid());
        addToParametersIfNotEmpty(parameters, "combination", drug.getCombination());
        addToParametersIfNotEmpty(parameters, "strength", drug.getStrength());
        addToParametersIfNotEmpty(parameters, "dosageForm", drug.getDosageForm());
        addToParametersIfNotEmpty(parameters, "genericName", drug.getGenericName());
        addToParametersIfNotEmpty(parameters, "maximumDose", drug.getMaximumDose());
        addToParametersIfNotEmpty(parameters, "minimumDose", drug.getMinimumDose());
        addToParametersIfNotEmpty(parameters, "last_read_entry_id", event.getId());
        addToParametersIfNotEmpty(parameters, "category", Jobs.DRUG_JOB.getJobRef());
        addToParametersIfNotEmpty(parameters, "feed_uri_for_last_read_entry", event.getFeedUri());
        if (event.getFeedUri() == null) {
            parameters.add(new Parameter("is_failed_event", "1", "boolean"));
        }
        return parameters;
    }

    private void addToParametersIfNotEmpty(List<Parameter> parameters, String name, String value) {
        if (value != null && !value.isEmpty()) {
            parameters.add(new Parameter(name, value));
        }
    }

    @Override
    public void cleanUp(Event event) {
    }
}
