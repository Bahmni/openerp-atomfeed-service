package org.bahmni.feed.openerp.worker;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final String feedUrl;
    private final String odooURL;
    private final OpenMRSWebClient webClient;
    private final String urlPrefix;

    private static Logger logger = LoggerFactory.getLogger(OpenERPCustomerServiceEventWorker.class);

    public OpenERPCustomerServiceEventWorker(String feedUrl, String odooURL, OpenERPContext openERPContext, OpenMRSWebClient webClient, String urlPrefix) {
        this.feedUrl = feedUrl;
        this.odooURL = odooURL;
        this.openERPContext = openERPContext;
        this.webClient = webClient;
        this.urlPrefix = urlPrefix;
    }

    @Override
    public void process(Event event) {
        try {
            openERPContext.execute(mapRequest(event), odooURL);
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
        addToParametersIfNotEmpty(parameters, "name", openMRSPatient.getName());
        addToParametersIfNotEmpty(parameters, "local_name", openMRSPatient.getLocalName().isEmpty()?"-":openMRSPatient.getLocalName());
        addToParametersIfNotEmpty(parameters,"ref", getPrimaryIdentifier(openMRSPatient).getIdentifier());
        addToParametersIfNotEmpty(parameters, "uuid", openMRSPatient.getUuid());
        String village = identifyVillage(openMRSPatient);
        if (!StringUtils.isBlank(village)) {
            addToParametersIfNotEmpty(parameters, "village", village);
        }
        OpenMRSPerson person = openMRSPatient.getPerson();

         // Add Sex (Gender)
        if (person.getGender() != null && !person.getGender().isEmpty()) {
            addToParametersIfNotEmpty(parameters, "sex", person.getGender());
        }

        // Add Age
        if (person.getAge() != null) {
            addToParametersIfNotEmpty(parameters, "age", String.valueOf(person.getAge()));
        } 
        
        if(person.getAttributes() != null){
            addToParametersIfNotEmpty(parameters,"attributes", person.getAttributes().toJsonString());
        }
        else{
            addToParametersIfNotEmpty(parameters,"attributes", "{}");
        }
        if(person.getPreferredAddress() != null){
            addToParametersIfNotEmpty(parameters,"preferredAddress", person.getPreferredAddress().toJsonString());
        }
        else{
            addToParametersIfNotEmpty(parameters,"preferredAddress", "{}");
        }
        addToParametersIfNotEmpty(parameters, "category", "create.customer");
        if((feedUrl != null && feedUrl.contains("$param.value")) || (feedUri != null && feedUri.contains("$param.value")))
            throw new RuntimeException("Junk values in the feedUrl:$param.value");
        addToParametersIfNotEmpty(parameters, "feed_uri", feedUrl);
        addToParametersIfNotEmpty(parameters, "feed_uri_for_last_read_entry", feedUri);
        addToParametersIfNotEmpty(parameters, "last_read_entry_id", eventId);
        if (isFailedEvent)
            parameters.add(createParameter("is_failed_event", "1", "boolean"));
        return parameters;
    }

    private void addToParametersIfNotEmpty(List<Parameter> parameters, String name, String value) {
        if (value != null && !value.isEmpty()) {
            parameters.add(new Parameter(name, value));
        }
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
