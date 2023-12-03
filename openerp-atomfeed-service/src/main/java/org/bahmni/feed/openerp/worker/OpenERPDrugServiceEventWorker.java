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
    private final String feedUrl;
    private final String odooURL;
    private final OpenMRSWebClient webClient;
    private final String urlPrefix;


    public OpenERPDrugServiceEventWorker(String feedUrl, String odooURL, OpenERPContext openERPContext, OpenMRSWebClient openMRSWebClient, String urlPrefix) {
        this.openERPContext = openERPContext;
        this.feedUrl = feedUrl;
        this.odooURL = odooURL;
        this.webClient = openMRSWebClient;
        this.urlPrefix = urlPrefix;
    }

    @Override
    public void process(Event event) {
        logger.debug("Processing the event [{}]", event.getContent());
        try {
            openERPContext.execute(mapRequest(event), odooURL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private OpenERPRequest mapRequest(Event event) throws IOException {
        String drugJson = webClient.get(URI.create(urlPrefix + event.getContent()));
        OpenMRSDrug drug = ObjectMapperRepository.objectMapper.readValue(drugJson, OpenMRSDrug.class);

        return new OpenERPRequest("atom.event.worker", "process_event", buildParameters(event,drug));
    }

    private List<Parameter> buildParameters(Event event,OpenMRSDrug drug) {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter("name",drug.getName()));
        parameters.add(new Parameter("shortName",drug.getShortName()));
        parameters.add(new Parameter("uuid",drug.getUuid()));
        parameters.add(new Parameter("combination",drug.getCombination()));
        parameters.add(new Parameter("strength",drug.getStrength()));
        parameters.add(new Parameter("dosageForm",drug.getDosageForm()));
        parameters.add(new Parameter("genericName",drug.getGenericName()));
        parameters.add(new Parameter("maximumDose",drug.getMaximumDose()));
        parameters.add(new Parameter("minimumDose",drug.getMinimumDose()));

        parameters.add(new Parameter("last_read_entry_id",event.getId()));
        parameters.add(new Parameter("category", "create.drug"));
        parameters.add(new Parameter("feed_uri_for_last_read_entry",event.getFeedUri()));

        if (event.getFeedUri() == null) {
            parameters.add(new Parameter("is_failed_event","1","boolean"));
        }

        return parameters;
    }

    @Override
    public void cleanUp(Event event) {

    }
}
