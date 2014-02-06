package org.bahmni.feed.openerp.domain.referencedata.parser;

import org.bahmni.feed.openerp.WebClientResponseParser;
import org.bahmni.feed.openerp.domain.referencedata.Drug;
import org.bahmni.feed.openerp.domain.referencedata.DrugCategory;
import org.bahmni.feed.openerp.domain.referencedata.ERPParameterizable;
import org.bahmni.feed.openerp.domain.referencedata.LabTest;
import org.bahmni.feed.openerp.domain.referencedata.ProductUOM;
import org.bahmni.feed.openerp.domain.referencedata.ProductUOMCategory;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.bahmni.openerp.web.service.ProductService;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReferenceDataResponseParser implements WebClientResponseParser {
    private ObjectMapper objectMapper;

    public ReferenceDataResponseParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public OpenERPRequest parse(String responseContent, String feedEventTitle, ProductService productService, String eventId, String feedURIForLastReadEntry, String feedURI) throws IOException {
        ERPParameterizable mappedObject = getMappedObject(feedEventTitle, responseContent);
        List<Parameter> parameters;
        if(mappedObject != null){
            parameters = mappedObject.getParameters(eventId, feedURIForLastReadEntry, feedURI);
        }
        else{
            parameters = new ArrayList<>();
        }
        return new OpenERPRequest("atom.event.worker", "process_event", parameters);
    }

    private ERPParameterizable getMappedObject(String feedEventTitle,String responseContent) throws IOException {

        switch(feedEventTitle){
            case "test" :   return objectMapper.readValue(responseContent, LabTest.class);
            case "panel" :   return objectMapper.readValue(responseContent, LabTest.class);
            case "drug" :   return objectMapper.readValue(responseContent, Drug.class);
            case "drug_category" :   return objectMapper.readValue(responseContent, DrugCategory.class);
            case "unit_of_measure" :   return objectMapper.readValue(responseContent, ProductUOM.class);
            case "unit_of_measure_category" :   return objectMapper.readValue(responseContent, ProductUOMCategory.class);
        }
        return null;
    }

    @Override
    public OpenERPRequest parse(String responseContent, ProductService productService, String eventId, String feedURIForLastReadEntry, String feedURI) throws IOException {
        return null;
    }


}
