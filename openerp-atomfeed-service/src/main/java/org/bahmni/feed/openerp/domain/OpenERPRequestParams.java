package org.bahmni.feed.openerp.domain;

import org.bahmni.feed.openerp.EncounterEventParser;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.service.ProductService;

import java.io.IOException;
import java.util.List;

public class OpenERPRequestParams {

    private ProductService productService;
    private List<EncounterEventParser> encounterEventParsers;

    public OpenERPRequestParams(ProductService productService, List<EncounterEventParser> encounterEventParsers) {
        this.productService = productService;
        this.encounterEventParsers = encounterEventParsers;
    }

    public OpenERPRequest getRequest(String encounterEventContent, String feedURIForLastReadEntry, String feedURI, String eventId) throws IOException {
        for (EncounterEventParser encounterEventParser : encounterEventParsers) {
            OpenERPRequest openERPRequest = encounterEventParser.parse(encounterEventContent,
                    productService, eventId, feedURIForLastReadEntry, feedURI);
            if (openERPRequest != OpenERPRequest.DO_NOT_CONSUME)
                return openERPRequest;
        }

        return OpenERPRequest.DO_NOT_CONSUME;
    }

}
