package org.bahmni.feed.openerp;

import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.service.ProductService;

import java.io.IOException;

public interface EncounterEventParser {
    OpenERPRequest parse(String encounterEventContent, ProductService productService, String eventId,
                         String feedURIForLastReadEntry, String feedURI) throws IOException;
}
