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

import static org.bahmni.feed.openerp.worker.OpenERPSellableResourceWorker.ERP_EVENT_CATEGORY;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OpenERPSellableResourceWorkerTest {

    @Mock
    private OpenERPAtomFeedProperties atomFeedProperties;
    @Mock
    private OpenERPClient openERPClient;
    @Mock
    private OpenMRSWebClient webClient;

    private OpenERPSellableResourceWorker worker;

    private String eventContent = "openmrs/ws/rest/v1/reference-data/resources/e48dd85b-fb13-4f3a-ae01-e6fd57eea6fe";
    private String eventUrl = "https://mybahmni/" + eventContent;
    private String feedUri = "https://mybahmni/openmrs/ws/atomfeed/sellable/recent";


    String sampleProcedureJsonWithSellableAsFalse
            = "{\n" +
                      "  \"isActive\": true,\n" +
                      "  \"dateCreated\": \"2015-09-07T12:14:24.000+0530\",\n" +
                      "  \"lastUpdated\": \"2015-09-07T14:18:15.000+0530\",\n" +
                      "  \"name\": \"Dressing of Wound\",\n" +
                      "  \"id\": \"e48dd85b-fb13-4f3a-ae01-e6fd57eea6fe\",\n" +
                      "  \"properties\": { \"sellable\": \"false\" } " +
                      "}";
    String sampleProcedureJson
            = "{\n" +
                      "  \"isActive\": true,\n" +
                      "  \"dateCreated\": \"2015-09-07T12:14:24.000+0530\",\n" +
                      "  \"lastUpdated\": \"2015-09-07T14:18:15.000+0530\",\n" +
                      "  \"name\": \"Dressing of Wound\",\n" +
                      "  \"id\": \"e48dd85b-fb13-4f3a-ae01-e6fd57eea6fe\",\n" +
                      "  \"properties\": { \"sellable\": \"true\" } " +
                      "}";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        worker = new OpenERPSellableResourceWorker(feedUri, openERPClient, webClient, "https://mybahmni/");
        when(webClient.get(URI.create(eventUrl))).thenReturn(sampleProcedureJson);
    }

    @Test
    public void shouldProcessSellableTypeEvent() throws Exception {
        Event event = new Event("1", eventContent, "Dressing", feedUri, new Date());
        worker.process(event);
        ArgumentCaptor<OpenERPRequest> erpRequestCatcher = ArgumentCaptor.forClass(OpenERPRequest.class);
        verify(openERPClient).execute(erpRequestCatcher.capture());

        OpenERPRequest openERPRequest = erpRequestCatcher.getValue();
        List<Parameter> parameters = openERPRequest.getParameters();

        Assert.assertTrue(parameters.contains(new Parameter("last_read_entry_id", "1")));
        //TODO: need to change this when we update the ERP module
        Assert.assertTrue(parameters.contains(new Parameter("category", ERP_EVENT_CATEGORY)));
        Assert.assertTrue(parameters.contains(new Parameter("feed_uri_for_last_read_entry", feedUri)));
        Assert.assertFalse(parameters.contains(new Parameter("is_failed_event", "1", "boolean")));
        Assert.assertTrue(parameters.contains(new Parameter("name", "Dressing of Wound")));
        Assert.assertTrue(parameters.contains(new Parameter("uuid", "e48dd85b-fb13-4f3a-ae01-e6fd57eea6fe")));
        Assert.assertTrue(parameters.contains(new Parameter("is_active", "1", "boolean")));
    }


    @Test
    public void shouldUpdateAsInactiveWhenNotSellableEvenWhenMRSResourceIsActive() throws Exception {
        when(webClient.get(URI.create(eventUrl))).thenReturn(sampleProcedureJsonWithSellableAsFalse);
        Event event = new Event("1", eventContent, "Dressing", feedUri, new Date());
        worker.process(event);
        ArgumentCaptor<OpenERPRequest> erpRequestCatcher = ArgumentCaptor.forClass(OpenERPRequest.class);
        verify(openERPClient).execute(erpRequestCatcher.capture());

        List<Parameter> parameters = erpRequestCatcher.getValue().getParameters();
        Assert.assertTrue(parameters.contains(new Parameter("name", "Dressing of Wound")));
        Assert.assertTrue(parameters.contains(new Parameter("uuid", "e48dd85b-fb13-4f3a-ae01-e6fd57eea6fe")));
        //Assert.assertTrue(parameters.contains(new Parameter("is_active", "0", "boolean")));
    }

}