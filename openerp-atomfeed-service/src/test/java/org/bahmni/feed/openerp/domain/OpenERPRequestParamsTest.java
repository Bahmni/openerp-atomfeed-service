package org.bahmni.feed.openerp.domain;


import org.bahmni.feed.openerp.testhelper.SampleEncounter;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.ict4h.atomfeed.client.domain.Event;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

public class OpenERPRequestParamsTest {
    @Mock
    private Event event;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldMapEncounterToOpenERPRequestparams() throws IOException {
        OpenERPRequestParams requestParams = new OpenERPRequestParams(event,"http://test");

        OpenERPRequest openERPRequest = requestParams.getRequest(SampleEncounter.json());
        List<Parameter> parameters = openERPRequest.getParameters();
        assertTrue(parameters.size() == 6);
    }
}
