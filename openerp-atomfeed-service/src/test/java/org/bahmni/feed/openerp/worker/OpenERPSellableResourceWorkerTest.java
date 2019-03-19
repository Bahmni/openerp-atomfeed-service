package org.bahmni.feed.openerp.worker;

import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.feed.openerp.client.OpenMRSWebClient;
import org.bahmni.openerp.web.client.OpenERPClient;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.ict4h.atomfeed.client.domain.Event;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.URI;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OpenERPSellableResourceWorkerTest {

    @Mock
    private OpenERPAtomFeedProperties atomFeedProperties;
    @Mock
    private OpenERPClient openERPClient;
    @Mock
    private OpenMRSWebClient webClient;

    private String feedUri = "http://feeduri";
    private String resourceJson;
    private OpenERPSellableResourceWorker worker;

    @Before
    public void setUp() {
        resourceJson = "{\n" +
                "    \"name\": \"A sellable\",\n" +
                "    \"id\": \"3ea122c2-d39a-4376-a502-0b6d0e7d33ed\",\n" +
                "    \"isActive\": \"true\"\n" +
                "}";
        MockitoAnnotations.initMocks(this);
        worker = new OpenERPSellableResourceWorker(feedUri, openERPClient, webClient, "http://prefix/");
    }

    @Test
    public void testProcess() throws Exception {
        Event event = new Event("event-id","content","test",feedUri, new Date());
        when(webClient.get(URI.create("http://prefix/content"))).thenReturn(resourceJson);

        worker.process(event);
        ArgumentCaptor<OpenERPRequest> erpRequestCatcher = ArgumentCaptor.forClass(OpenERPRequest.class);
        verify(openERPClient).execute(erpRequestCatcher.capture());

        OpenERPRequest openERPRequest = erpRequestCatcher.getValue();
        List<Parameter> actualParameters = openERPRequest.getParameters();

        Assert.assertTrue(actualParameters.contains(new Parameter("name", "A sellable")));
        Assert.assertTrue(actualParameters.contains(new Parameter("uuid", "3ea122c2-d39a-4376-a502-0b6d0e7d33ed")));
        Assert.assertTrue(actualParameters.contains(new Parameter("is_active", "true")));
    }
}
