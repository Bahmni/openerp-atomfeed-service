package org.bahmni.feed.openerp.domain.labOrderType;

import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.ict4h.atomfeed.client.domain.Event;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class OpenMRSRadiologyTestEventTest {
    private String radiologyTestJson;
    private String inactiveRadiologyTestJson;

    @Before
    public void setUp() throws Exception {
        radiologyTestJson = "{\n" +
                "  \"isActive\": true,\n" +
                "  \"dateCreated\": \"2015-09-07T12:14:24.000+0530\",\n" +
                "  \"lastUpdated\": \"2015-09-07T14:18:15.000+0530\",\n" +
                "  \"name\": \"Chest X RAY\",\n" +
                "  \"id\": \"f59dd85b-fb13-4f3a-ae01-e6fd57eea6fe\"\n" +
                "}";

        inactiveRadiologyTestJson = "{\n" +
                "  \"isActive\": false,\n" +
                "  \"dateCreated\": \"2015-09-07T12:14:24.000+0530\",\n" +
                "  \"lastUpdated\": \"2015-09-07T14:18:15.000+0530\",\n" +
                "  \"name\": \"Chest X RAY\",\n" +
                "  \"id\": \"f59dd85b-fb13-4f3a-ae01-e6fd57eea6fe\"\n" +
                "}";
    }

    @Test
    public void shouldMapToOpenERPEvent() throws Exception {
        String feedUri = "http://feeduri";
        Event event = new Event("1", "content", "test", feedUri, new Date());
        OpenERPRequest openERPRequest = new OpenMRSRadiologyTestEvent().mapEventToOpenERPRequest(event, radiologyTestJson, feedUri);
        List<Parameter> parameters = openERPRequest.getParameters();

        Assert.assertTrue(parameters.contains(new Parameter("last_read_entry_id", "1")));
        Assert.assertTrue(parameters.contains(new Parameter("category", "create.radiology.test")));
        Assert.assertTrue(parameters.contains(new Parameter("feed_uri_for_last_read_entry", feedUri)));
        Assert.assertFalse(parameters.contains(new Parameter("is_failed_event", "1", "boolean")));
        Assert.assertTrue(parameters.contains(new Parameter("name", "Chest X RAY")));
        Assert.assertTrue(parameters.contains(new Parameter("uuid", "f59dd85b-fb13-4f3a-ae01-e6fd57eea6fe")));
        Assert.assertTrue(parameters.contains(new Parameter("is_active", "1", "boolean")));
    }

    @Test
    public void shouldMapActiveStatusToOpenERPEvent() throws Exception {
        String feedUri = "http://feeduri";
        Event event = new Event("1", "content", "test", feedUri, new Date());
        OpenERPRequest openERPRequest = new OpenMRSRadiologyTestEvent().mapEventToOpenERPRequest(event, inactiveRadiologyTestJson, feedUri);
        List<Parameter> parameters = openERPRequest.getParameters();

        Assert.assertTrue(parameters.contains(new Parameter("last_read_entry_id", "1")));
        Assert.assertTrue(parameters.contains(new Parameter("category", "create.radiology.test")));
        Assert.assertTrue(parameters.contains(new Parameter("feed_uri_for_last_read_entry", feedUri)));
        Assert.assertFalse(parameters.contains(new Parameter("is_failed_event", "1", "boolean")));
        Assert.assertTrue(parameters.contains(new Parameter("name", "Chest X RAY")));
        Assert.assertTrue(parameters.contains(new Parameter("uuid", "f59dd85b-fb13-4f3a-ae01-e6fd57eea6fe")));
        Assert.assertTrue(parameters.contains(new Parameter("is_active", "0", "boolean")));
    }
}