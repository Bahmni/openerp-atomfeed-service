package org.bahmni.feed.openerp.domain;

import org.bahmni.feed.openerp.WebClientResponseParser;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.service.ProductService;

import java.io.IOException;
import java.util.List;

public class OpenERPRequestParams {

    private ProductService productService;
    private List<WebClientResponseParser> webClientResponseParsers;

    public OpenERPRequestParams(ProductService productService, List<WebClientResponseParser> webClientResponseParsers) {
        this.productService = productService;
        this.webClientResponseParsers = webClientResponseParsers;
    }

    public OpenERPRequest getRequest(String eventContent, String feedURIForLastReadEntry, String feedURI, String eventId) throws IOException {
        for (WebClientResponseParser webClientResponseParser : webClientResponseParsers) {
            OpenERPRequest openERPRequest = webClientResponseParser.parse(eventContent,
                    productService, eventId, feedURIForLastReadEntry, feedURI);
            if (openERPRequest != OpenERPRequest.DO_NOT_CONSUME)
                return openERPRequest;
        }

        return OpenERPRequest.DO_NOT_CONSUME;
    }

}
