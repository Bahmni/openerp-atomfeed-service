package org.bahmni.feed.openerp.domain;


import org.bahmni.feed.openerp.WebClientResponseParser;
import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.feed.openerp.OpenMRSEncounterParser;
import org.bahmni.feed.openerp.testhelper.FileConverter;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.bahmni.openerp.web.service.ProductService;
import org.ict4h.atomfeed.client.domain.Event;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

public class OpenERPRequestParamsTest {
    @Mock
    private Event event;
    @Mock
    private ProductService productService;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldMapEncounterToOpenERPRequestparams() throws IOException {
        OpenMRSEncounterParser openMRSEncounterParser = new OpenMRSEncounterParser(ObjectMapperRepository.objectMapper);
        List<WebClientResponseParser> encounterParsers = new ArrayList<>();
        encounterParsers.add(openMRSEncounterParser);

        OpenERPRequestParams requestParams = new OpenERPRequestParams(productService, encounterParsers);

        OpenERPRequest openERPRequest = requestParams.getRequest(FileConverter.convertToString("encounterResourceForLabOrder.json"), "http://feedUriLastReadEntry", "http://feedUri", "eventId");
        List<Parameter> parameters = openERPRequest.getParameters();
        assertTrue(parameters.size() == 6);
    }
}
