package org.bahmni.feed.openerp.domain.labOrderType;

import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.ict4h.atomfeed.client.domain.Event;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class OpenMRSLabTestEventTest {
    private String labTestJson;
    private String inActiveLabTestJson;

    @Before
    public void setUp() throws Exception {
        labTestJson = "{\n" +
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
                "  \"description\": \"Red Blood Count\",\n" +
                "  \"isActive\": true,\n" +
                "  \"dateCreated\": \"2015-09-08T11:59:34.000+0530\",\n" +
                "  \"lastUpdated\": \"2015-09-08T12:04:46.000+0530\",\n" +
                "  \"name\": \"RBC\",\n" +
                "  \"id\": \"5cf81237-e888-46e8-b09a-2f190492c2d7\"\n" +
                "}";

        inActiveLabTestJson = "{\n" +
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
                "  \"description\": \"Red Blood Count\",\n" +
                "  \"isActive\": false,\n" +
                "  \"dateCreated\": \"2015-09-08T11:59:34.000+0530\",\n" +
                "  \"lastUpdated\": \"2015-09-08T12:04:46.000+0530\",\n" +
                "  \"name\": \"RBC\",\n" +
                "  \"id\": \"5cf81237-e888-46e8-b09a-2f190492c2d7\"\n" +
                "}";

    }

    @Test
    public void shouldMapToOpenERPEvent() throws Exception {
        String feedUri = "http://feeduri";
        Event event = new Event("1","content","test", feedUri, new Date());
        OpenERPRequest openERPRequest = new OpenMRSLabTestEvent().mapEventToOpenERPRequest(event, labTestJson, feedUri);
        List<Parameter> parameters = openERPRequest.getParameters();

        Assert.assertTrue(parameters.contains(new Parameter("last_read_entry_id","1")));
        Assert.assertTrue(parameters.contains(new Parameter("category","create.lab.test")));
        Assert.assertTrue(parameters.contains(new Parameter("feed_uri_for_last_read_entry", feedUri)));
        Assert.assertFalse(parameters.contains(new Parameter("is_failed_event","1","boolean")));
        Assert.assertTrue(parameters.contains(new Parameter("name","RBC")));
        Assert.assertTrue(parameters.contains(new Parameter("uuid", "5cf81237-e888-46e8-b09a-2f190492c2d7")));
        Assert.assertTrue(parameters.contains(new Parameter("is_active", "1", "boolean")));
    }

    @Test
    public void shouldMapActiveStatusToOpenERPEvent() throws Exception {
        String feedUri = "http://feeduri";
        Event event = new Event("1","content","test", feedUri, new Date());
        OpenERPRequest openERPRequest = new OpenMRSLabTestEvent().mapEventToOpenERPRequest(event, inActiveLabTestJson, feedUri);
        List<Parameter> parameters = openERPRequest.getParameters();

        Assert.assertTrue(parameters.contains(new Parameter("last_read_entry_id","1")));
        Assert.assertTrue(parameters.contains(new Parameter("category","create.lab.test")));
        Assert.assertTrue(parameters.contains(new Parameter("feed_uri_for_last_read_entry", feedUri)));
        Assert.assertFalse(parameters.contains(new Parameter("is_failed_event","1","boolean")));
        Assert.assertTrue(parameters.contains(new Parameter("name","RBC")));
        Assert.assertTrue(parameters.contains(new Parameter("uuid", "5cf81237-e888-46e8-b09a-2f190492c2d7")));
        Assert.assertTrue(parameters.contains(new Parameter("is_active", "0", "boolean")));
    }
}