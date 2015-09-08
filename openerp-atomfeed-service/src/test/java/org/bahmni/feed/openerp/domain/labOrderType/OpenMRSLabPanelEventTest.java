package org.bahmni.feed.openerp.domain.labOrderType;

import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.ict4h.atomfeed.client.domain.Event;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class OpenMRSLabPanelEventTest {
    private String labPanelJson;
    private String inactiveLabPanelJson;

    @Before
    public void setUp() throws Exception {
        labPanelJson = "{\n" +
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

        inactiveLabPanelJson = "{\n" +
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
                "  \"isActive\": false,\n" +
                "  \"name\": \"Smear Test\",\n" +
                "  \"id\": \"2dc3855a-263f-48df-8071-8ff974a11972\"\n" +
                "}";
    }

    @Test
    public void shouldMapToOpenERPEvent() throws Exception {
        String feedUri = "http://feeduri";
        Event event = new Event("1", "content", "panel", feedUri, new Date());
        OpenERPRequest openERPRequest = new OpenMRSLabPanelEvent().mapEventToOpenERPRequest(event, labPanelJson, feedUri);
        List<Parameter> parameters = openERPRequest.getParameters();

        Assert.assertTrue(parameters.contains(new Parameter("last_read_entry_id", "1")));
        Assert.assertTrue(parameters.contains(new Parameter("category", "create.lab.panel")));
        Assert.assertTrue(parameters.contains(new Parameter("feed_uri_for_last_read_entry", feedUri)));
        Assert.assertFalse(parameters.contains(new Parameter("is_failed_event", "1", "boolean")));
        Assert.assertTrue(parameters.contains(new Parameter("name", "Smear Test")));
        Assert.assertTrue(parameters.contains(new Parameter("uuid", "2dc3855a-263f-48df-8071-8ff974a11972")));
        Assert.assertTrue(parameters.contains(new Parameter("is_active", "1", "boolean")));
    }

    @Test
    public void shouldNotMapInActivePanelToOpenERPEvent() throws Exception {
        String feedUri = "http://feeduri";
        Event event = new Event("1", "content", "panel", feedUri, new Date());
        OpenERPRequest openERPRequest = new OpenMRSLabPanelEvent().mapEventToOpenERPRequest(event, inactiveLabPanelJson, feedUri);
        List<Parameter> parameters = openERPRequest.getParameters();

        Assert.assertTrue(parameters.contains(new Parameter("last_read_entry_id", "1")));
        Assert.assertTrue(parameters.contains(new Parameter("category", "create.lab.panel")));
        Assert.assertTrue(parameters.contains(new Parameter("feed_uri_for_last_read_entry", feedUri)));
        Assert.assertFalse(parameters.contains(new Parameter("is_failed_event", "1", "boolean")));
        Assert.assertTrue(parameters.contains(new Parameter("name", "Smear Test")));
        Assert.assertTrue(parameters.contains(new Parameter("uuid", "2dc3855a-263f-48df-8071-8ff974a11972")));
        Assert.assertTrue(parameters.contains(new Parameter("is_active", "0", "boolean")));
    }
}