package org.bahmni.feed.openerp.worker;

import org.apache.log4j.*;
import org.bahmni.feed.openerp.client.*;
import org.bahmni.feed.openerp.domain.referencedata.*;
import org.bahmni.openerp.web.client.*;
import org.bahmni.openerp.web.request.*;
import org.bahmni.openerp.web.request.builder.*;
import org.ict4h.atomfeed.client.domain.*;
import org.ict4h.atomfeed.client.service.*;

import java.io.*;
import java.util.*;

public class ReferenceDataEventWorker implements EventWorker {
    private final String feedUrl;
    private final OpenERPClient openERPClient;
    private final String urlPrefix;
    private ReferenceDataWebClient webClient;

    private static Logger logger = Logger.getLogger(ReferenceDataEventWorker.class);


    public ReferenceDataEventWorker(String feedUrl, OpenERPClient openERPClient, ReferenceDataWebClient webClient, String urlPrefix) {
        this.feedUrl = feedUrl;
        this.openERPClient = openERPClient;
        this.webClient = webClient;
        this.urlPrefix = urlPrefix;
    }

    @Override
    public void process(Event event) {
        try {
            if (!canProcess(event)) return;
            OpenERPRequest openERPRequest = mapToERPRequest(event);
            openERPClient.execute(openERPRequest);
        } catch (IOException e) {
            logger.error("Error processing reference data event : "+event.toString(),e);
        }
    }

    private boolean canProcess(Event event) throws IOException {
        return (getObjectType(event.getTitle()) != null);
    }

    private OpenERPRequest mapToERPRequest(Event event) throws IOException {
        ERPParameterizable responseObject = (ERPParameterizable) webClient.get(urlPrefix + event.getContent(), getObjectType(event.getTitle()));
        return parse(responseObject,event.getId(),event.getFeedUri(),feedUrl);
    }

    private OpenERPRequest parse(ERPParameterizable responseContent, String eventId, String feedURIForLastReadEntry, String feedURI) throws IOException {
        List<Parameter> parameters;
        if(responseContent != null){
            parameters = responseContent.getParameters(eventId, feedURIForLastReadEntry, feedURI);
        }
        else{
            parameters = new ArrayList<>();
        }
        return new OpenERPRequest("atom.event.worker", "process_event", parameters);
    }

    private <T> Class<T> getObjectType(String feedEventTitle) throws IOException {

        switch(feedEventTitle.toLowerCase()){
            case "test" :   return  (Class<T>) LabTest.class;
            case "panel" :   return  (Class<T>)LabTest.class;
            case "drug" :   return (Class<T>)Drug.class;
            case "drug_category" :   return (Class<T>)DrugCategory.class;
            case "product_unit_of_measure" :   return  (Class<T>)ProductUOM.class;
            case "product_unit_of_measure_category" :   return (Class<T>)ProductUOMCategory.class;
            default: return null;
        }
    }
    @Override
    public void cleanUp(Event event) {

    }
}
