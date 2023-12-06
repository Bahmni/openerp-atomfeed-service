package org.bahmni.feed.openerp.worker;

import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.feed.openerp.client.OpenMRSWebClient;
import org.bahmni.openerp.web.client.strategy.OpenERPContext;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.ict4h.atomfeed.client.domain.Event;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.List;

import static org.bahmni.feed.openerp.domain.labOrderType.OpenMRSRadiologyTestEvent.RADIOLOGY_TEST_EVENT_NAME;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OpenERPRadiologyTestServiceEventWorkerTest {

    @Mock
    private OpenERPContext openERPContext;
    @Mock
    private OpenMRSWebClient webClient;

    private final String feedUri = "http://feeduri";
    private final String odooURL = "http://odooURL";
    private OpenERPRadiologyTestServiceEventWorker worker;

    @Before
    public void setUp() {
        String radiologyTestJson = "{\n" +
                "  \"isActive\": true,\n" +
                "  \"dateCreated\": \"2015-09-07T12:14:24.000+0530\",\n" +
                "  \"lastUpdated\": \"2015-09-07T14:18:15.000+0530\",\n" +
                "  \"name\": \"Chest X RAY\",\n" +
                "  \"id\": \"f59dd85b-fb13-4f3a-ae01-e6fd57eea6fe\"\n" +
                "}";

        MockitoAnnotations.initMocks(this);
        worker = new OpenERPRadiologyTestServiceEventWorker(feedUri, odooURL, openERPContext, webClient, "http://prefix/");
        when(webClient.get(URI.create("http://prefix/radiology"))).thenReturn(radiologyTestJson);

    }

    @Test
    public void shouldProcessRadiologyTestEvent() throws IOException {
        Event event = new Event("1", RADIOLOGY_TEST_EVENT_NAME, RADIOLOGY_TEST_EVENT_NAME, feedUri, new Date());

        worker.process(event);
        ArgumentCaptor<OpenERPRequest> erpRequestCatcher = ArgumentCaptor.forClass(OpenERPRequest.class);
        verify(openERPContext).execute(erpRequestCatcher.capture(), eq(odooURL));

        OpenERPRequest openERPRequest = erpRequestCatcher.getValue();
        List<Parameter> parameters = openERPRequest.getParameters();

        Assert.assertTrue(parameters.contains(new Parameter("last_read_entry_id", "1")));
        Assert.assertTrue(parameters.contains(new Parameter("category", "create.radiology.test")));
        Assert.assertTrue(parameters.contains(new Parameter("feed_uri_for_last_read_entry", feedUri)));
        Assert.assertFalse(parameters.contains(new Parameter("is_failed_event", "1", "boolean")));
        Assert.assertTrue(parameters.contains(new Parameter("name", "Chest X RAY")));
        Assert.assertTrue(parameters.contains(new Parameter("uuid", "f59dd85b-fb13-4f3a-ae01-e6fd57eea6fe")));
    }

    @Test
    public void testRadiologyFailedEvent() throws IOException {
        Event event = new Event("3", RADIOLOGY_TEST_EVENT_NAME, RADIOLOGY_TEST_EVENT_NAME, null, new Date());

        worker.process(event);
        ArgumentCaptor<OpenERPRequest> erpRequestCatcher = ArgumentCaptor.forClass(OpenERPRequest.class);
        verify(openERPContext).execute(erpRequestCatcher.capture(), eq(odooURL));

        OpenERPRequest openERPRequest = erpRequestCatcher.getValue();
        List<Parameter> actualParameters = openERPRequest.getParameters();
        Assert.assertTrue(actualParameters.contains(new Parameter("is_failed_event", "1", "boolean")));

    }

}