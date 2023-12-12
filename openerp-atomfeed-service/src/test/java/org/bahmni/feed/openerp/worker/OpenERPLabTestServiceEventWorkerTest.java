package org.bahmni.feed.openerp.worker;

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

import static org.bahmni.feed.openerp.domain.labOrderType.OpenMRSLabTestEvent.LAB_TEST_EVENT_NAME;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OpenERPLabTestServiceEventWorkerTest {

    @Mock
    private OpenERPContext openERPContext;
    @Mock
    private OpenMRSWebClient webClient;

    private final String feedUri = "http://feeduri";
    private final String odooURL = "http://odooURL";
    private OpenERPLabTestServiceEventWorker worker;

    @Before
    public void setUp() {
        String labTestJson = "{\n" +
                "  \"resultType\": \"Coded\",\n" +
                "  \"testUnitOfMeasure\": null,\n" +
                "  \"sortOrder\": 999,\n" +
                "  \"codedTestAnswer\": [\n" +
                "    {\n" +
                "      \"uuid\": \"f4838e40-321d-4bf7-b0d5-873dd47cfb68\",\n" +
                "      \"name\": \"Diabetes, Patient on Enalapril\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"uuid\": \"148b3e66-9eb5-4437-8f96-06c7feb95d5c\",\n" +
                "      \"name\": \"COPD, Still Smoking\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"description\": \"newtest1\",\n" +
                "  \"isActive\": true,\n" +
                "  \"dateCreated\": \"2015-09-08T11:59:34.000+0530\",\n" +
                "  \"lastUpdated\": \"2015-09-08T12:04:46.000+0530\",\n" +
                "  \"name\": \"RBC\",\n" +
                "  \"id\": \"5cf81237-e888-46e8-b09a-2f190492c2d7\"\n" +
                "}";

        MockitoAnnotations.initMocks(this);
        worker = new OpenERPLabTestServiceEventWorker(feedUri, odooURL, openERPContext, webClient, "http://prefix/");
        when(webClient.get(URI.create("http://prefix/test"))).thenReturn(labTestJson);

    }

    @Test
    public void shouldProcessLabTestEvent() throws IOException {
        Event event = new Event("2", LAB_TEST_EVENT_NAME, LAB_TEST_EVENT_NAME, feedUri, new Date());

        worker.process(event);
        ArgumentCaptor<OpenERPRequest> erpRequestCatcher = ArgumentCaptor.forClass(OpenERPRequest.class);
        verify(openERPContext).execute(erpRequestCatcher.capture(), eq(odooURL));

        OpenERPRequest openERPRequest = erpRequestCatcher.getValue();
        List<Parameter> parameters = openERPRequest.getParameters();

        Assert.assertTrue(parameters.contains(new Parameter("last_read_entry_id", "2")));
        Assert.assertTrue(parameters.contains(new Parameter("category", "create.lab.test")));
        Assert.assertTrue(parameters.contains(new Parameter("feed_uri_for_last_read_entry", feedUri)));
        Assert.assertFalse(parameters.contains(new Parameter("is_failed_event", "1", "boolean")));
        Assert.assertTrue(parameters.contains(new Parameter("name", "RBC")));
        Assert.assertTrue(parameters.contains(new Parameter("uuid", "5cf81237-e888-46e8-b09a-2f190492c2d7")));
    }

    @Test
    public void testTestFailedEvent() throws IOException {
        Event event = new Event("4", LAB_TEST_EVENT_NAME, LAB_TEST_EVENT_NAME, null, new Date());

        worker.process(event);
        ArgumentCaptor<OpenERPRequest> erpRequestCatcher = ArgumentCaptor.forClass(OpenERPRequest.class);
        verify(openERPContext).execute(erpRequestCatcher.capture(), eq(odooURL));

        OpenERPRequest openERPRequest = erpRequestCatcher.getValue();
        List<Parameter> actualParameters = openERPRequest.getParameters();
        Assert.assertTrue(actualParameters.contains(new Parameter("is_failed_event", "1", "boolean")));

    }

}