package org.bahmni.feed.openerp;

import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.service.ProductService;

import java.io.IOException;

public interface WebClientResponseParser {
    OpenERPRequest parse(String responseContent, ProductService productService, String eventId,
                         String feedURIForLastReadEntry, String feedURI) throws IOException;

    OpenERPRequest parse(String responseContent,String feedEventTitle, ProductService productService, String eventId,
                         String feedURIForLastReadEntry, String feedURI) throws IOException;

}
