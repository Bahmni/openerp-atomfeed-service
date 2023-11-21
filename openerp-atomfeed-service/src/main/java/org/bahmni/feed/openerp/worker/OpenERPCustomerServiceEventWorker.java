package org.bahmni.feed.openerp.worker;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.feed.openerp.OpenMRSPatientMapper;
import org.bahmni.feed.openerp.client.OpenMRSWebClient;
import org.bahmni.feed.openerp.domain.OpenMRSPatient;
import org.bahmni.feed.openerp.domain.OpenMRSPatientIdentifier;
import org.bahmni.feed.openerp.domain.OpenMRSPerson;
import org.bahmni.feed.openerp.domain.OpenMRSPersonAddress;
import org.bahmni.openerp.web.client.strategy.OpenERPContext;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class OpenERPCustomerServiceEventWorker implements EventWorker {
    OpenERPContext openERPContext;
    private String feedUrl;
    private OpenMRSWebClient webClient;
    private String urlPrefix;

    private static Logger logger = LogManager.getLogger(OpenERPCustomerServiceEventWorker.class);

    public OpenERPCustomerServiceEventWorker(String feedUrl, OpenERPContext openERPContext, OpenMRSWebClient webClient, String urlPrefix) {
        this.feedUrl = feedUrl;
        this.openERPContext = openERPContext;
        this.webClient = webClient;
        this.urlPrefix = urlPrefix;
    }

    @Override
    public void process(Event event) {
        try {
            openERPContext.execute(mapRequest(event));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cleanUp(Event event) {
    }

    private OpenERPRequest mapRequest(Event event) throws IOException {
        return new OpenERPRequest("atom.event.worker", "process_event", getParameters(event));
    }

    private List<Parameter> getParameters(Event event) throws IOException {
        String content = event.getContent();
        String patientJSON = webClient.get(URI.create(urlPrefix + content));

        OpenMRSPatientMapper openMRSPatientMapper = new OpenMRSPatientMapper(ObjectMapperRepository.objectMapper);
        OpenMRSPatient openMRSPatient = openMRSPatientMapper.map(patientJSON);

        return mapParameters(openMRSPatient, event.getId(), event.getFeedUri(), event.getFeedUri() == null);
    }

    private List<Parameter> mapParameters(OpenMRSPatient openMRSPatient, String eventId, String feedUri, boolean isFailedEvent) {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(createParameter("name", openMRSPatient.getName(), "string"));
        parameters.add(createParameter("local_name", openMRSPatient.getLocalName(), "string"));
        parameters.add(createParameter("ref", getPrimaryIdentifier(openMRSPatient).getIdentifier(), "string"));
        parameters.add(createParameter("uuid", openMRSPatient.getUuid(), "string"));
        String village = identifyVillage(openMRSPatient);
        if (!StringUtils.isBlank(village)) {
            parameters.add(createParameter("village", village, "string"));
        }
        OpenMRSPerson person = openMRSPatient.getPerson();
        if(person.getAttributes() != null){
            parameters.add(createParameter("attributes", person.getAttributes().toJsonString(), "string"));
        }
        else{
            parameters.add(createParameter("attributes", null, "string"));
        }
        if(person.getPreferredAddress() != null){
            parameters.add(createParameter("preferredAddress", person.getPreferredAddress().toJsonString(), "string"));
        }
        else{
            parameters.add(createParameter("preferredAddress", "{}", "string"));
        }

        parameters.add(createParameter("category", "create.customer", "string"));
        if((feedUrl != null && feedUrl.contains("$param.value")) || (feedUri != null && feedUri.contains("$param.value")))
            throw new RuntimeException("Junk values in the feedUrl:$param.value");
        parameters.add(createParameter("feed_uri", feedUrl, "string"));
        parameters.add(createParameter("last_read_entry_id", eventId, "string"));
        parameters.add(createParameter("feed_uri_for_last_read_entry", feedUri, "string"));
        if (isFailedEvent)
            parameters.add(createParameter("is_failed_event", "1", "boolean"));
        return parameters;
    }

    private OpenMRSPatientIdentifier getPrimaryIdentifier(OpenMRSPatient patient) {
        for (OpenMRSPatientIdentifier identifier : patient.getIdentifiers()) {
            if (identifier.isPreferred()){
                return identifier;
            }
        }
        throw new RuntimeException("Preferred or Primary identifier is not available for the patient: "+patient.getName());
    }

    private String identifyVillage(OpenMRSPatient openMRSPatient) {
        OpenMRSPersonAddress preferredAddress = openMRSPatient.getPerson().getPreferredAddress();
        return (preferredAddress != null) ?  preferredAddress.getCityVillage() : null;
    }


    private Parameter createParameter(String name, String value, String type) {
        return new Parameter(name, value, type);
    }

}
