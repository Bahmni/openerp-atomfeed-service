package org.bahmni.feed.openerp;

import org.bahmni.feed.openerp.domain.openelis.OpenElisLabOrder;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.bahmni.openerp.web.service.ProductService;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class OpenElisLabOrderParser implements WebClientResponseParser {
    private ObjectMapper objectMapper;

    public OpenElisLabOrderParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public OpenERPRequest parse(String responseContent, ProductService productService, String eventId,
                                String feedURIForLastReadEntry, String feedURI) throws IOException {
        OpenElisLabOrder openElisLabOrder = objectMapper.readValue(responseContent, OpenElisLabOrder.class);;
        List<Parameter> parameters = openElisLabOrder.getParameters(eventId, productService, feedURIForLastReadEntry, feedURI);
        return new OpenERPRequest("atom.event.worker", "process_event", parameters);
    }

}
