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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OpenERPLabPanelServiceEventWorkerTest {
    @Mock
    private OpenERPContext openERPContext;
    @Mock
    private OpenMRSWebClient webClient;

    private final String feedUri = "http://feeduri";
    private final String odooURL = "http://odooURL";
    private OpenERPLabPanelServiceEventWorker worker;

    @Before
    public void setUp() {

        String labPanelJson = "{\n" +
                "  \"sortOrder\": 999,\n" +
                "  \"tests\": [\n" +
                "    {\n" +
                "      \"uuid\": \"e20fd44e-1516-4a85-bc1e-ba50556ea10d\",\n" +
                "      \"name\": \"Smear Test (Slit Skin)\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"description\": \"newpanell\",\n" +
                "  \"dateCreated\": \"2015-09-08T14:53:02.000+0530\",\n" +
                "  \"lastUpdated\": \"2015-09-08T14:53:02.000+0530\",\n" +
                "  \"isActive\": true,\n" +
                "  \"name\": \"Smear Test\",\n" +
                "  \"id\": \"2dc3855a-263f-48df-8071-8ff974a11972\"\n" +
                "}";

        MockitoAnnotations.initMocks(this);
        worker = new OpenERPLabPanelServiceEventWorker(feedUri, odooURL, openERPContext, webClient, "http://prefix/");
        when(webClient.get(URI.create("http://prefix/panel"))).thenReturn(labPanelJson);

    }

    @Test
    public void shouldProcessLabPanelEvent() throws Exception {
        Event event = new Event("6", "panel", "panel", feedUri, new Date());

        worker.process(event);
        ArgumentCaptor<OpenERPRequest> erpRequestCatcher = ArgumentCaptor.forClass(OpenERPRequest.class);
        verify(openERPContext).execute(erpRequestCatcher.capture(), eq(odooURL));

        OpenERPRequest openERPRequest = erpRequestCatcher.getValue();
        List<Parameter> parameters = openERPRequest.getParameters();

        Assert.assertTrue(parameters.contains(new Parameter("last_read_entry_id","6")));
        Assert.assertTrue(parameters.contains(new Parameter("category","create.lab.panel")));
        Assert.assertTrue(parameters.contains(new Parameter("feed_uri_for_last_read_entry", feedUri)));
        Assert.assertFalse(parameters.contains(new Parameter("is_failed_event","1","boolean")));
        Assert.assertTrue(parameters.contains(new Parameter("name","Smear Test")));
        Assert.assertTrue(parameters.contains(new Parameter("uuid", "2dc3855a-263f-48df-8071-8ff974a11972")));
    }

    @Test
    public void testPanelFailedEvent() throws IOException {
        Event event = new Event("4", "panel", "panel", null, new Date());

        worker.process(event);
        ArgumentCaptor<OpenERPRequest> erpRequestCatcher = ArgumentCaptor.forClass(OpenERPRequest.class);
        verify(openERPContext).execute(erpRequestCatcher.capture(), eq(odooURL));

        OpenERPRequest openERPRequest = erpRequestCatcher.getValue();
        List<Parameter> actualParameters = openERPRequest.getParameters();
        Assert.assertTrue(actualParameters.contains(new Parameter("is_failed_event", "1", "boolean")));

    }


}