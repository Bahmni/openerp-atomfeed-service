package org.bahmni.feed.openerp.worker;

import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.feed.openerp.client.ReferenceDataWebClient;
import org.bahmni.feed.openerp.domain.referencedata.Drug;
import org.bahmni.feed.openerp.domain.referencedata.LabTest;
import org.bahmni.feed.openerp.testhelper.SampleEncounter;
import org.bahmni.openerp.web.client.OpenERPClient;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.codehaus.jackson.map.ObjectMapper;
import org.ict4h.atomfeed.client.domain.Event;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReferenceDataEventWorkerTest  {

    String labOrderJson;
    ReferenceDataEventWorker referenceDataEventWorker;

    @Mock
    private OpenERPAtomFeedProperties atomFeedProperties;
    @Mock
    private OpenERPClient openERPClient;
    @Mock
    private ReferenceDataWebClient webClient;

    private String feedUri = "http://feeduri";
    private String drugOrderJson;

    @Before
    public void setUp(){
        labOrderJson = SampleEncounter.json("referenceDataTest.json");
        drugOrderJson = SampleEncounter.json("referenceDataDrug.json");
        MockitoAnnotations.initMocks(this);
        referenceDataEventWorker = new ReferenceDataEventWorker(feedUri,openERPClient,webClient,"prefix");

    }


    @Test
    public void shouldMapToLabRequest() throws Exception {
        Event event = new Event("event-id","","test",feedUri);
        ObjectMapper objectMapper = new ObjectMapper();
        LabTest labTestFromFeed = objectMapper.readValue(labOrderJson, LabTest.class);
        when(webClient.get(any(String.class),any(Class.class))).thenReturn(labTestFromFeed);

        referenceDataEventWorker.process(event);

        ArgumentCaptor<OpenERPRequest> labTestRequestCatcher = ArgumentCaptor.forClass(OpenERPRequest.class);
        verify(openERPClient).execute(labTestRequestCatcher.capture());

        OpenERPRequest labTestRequest = labTestRequestCatcher.getValue();
        List<Parameter> parameters = labTestRequest.getParameters();

        Assert.assertTrue(isCorrect(parameters.get(0),"category", "create.lab.test", "string"));
        Assert.assertTrue(isCorrect(parameters.get(2),"last_read_entry_id", "event-id", "string"));
        Assert.assertTrue(isCorrect(parameters.get(3),"feed_uri_for_last_read_entry", feedUri, "string"));

        String labTestJson = parameters.get(4).getValue();
        LabTest labTest = objectMapper.readValue(labTestJson, LabTest.class);

        Assert.assertEquals("Test",labTest.getCategory());
        Assert.assertEquals("2c11be15-1691-4809-9e3c-d82914edfaa0",labTest.getId());
        Assert.assertEquals("haemoglobin",labTest.getName());
        Assert.assertEquals("haemo", labTest.getShortName());
        Assert.assertEquals(10.0, labTest.getSalePrice(),0.00009);

    }

    @Test
    public void shouldNotMakeWebClientCallIfWeAreNotProcessingThisfeed() throws Exception {
        Event event = new Event("event-id","","INVALID TITLE",feedUri);
        when(webClient.get(any(String.class),any(Class.class))).thenThrow(new RuntimeException("Should never have been called"));

        referenceDataEventWorker.process(event);
    }


    @Test
    public void shouldMapToDrugRequest() throws Exception {
        Event event = new Event("event-id","","drug",feedUri);
        ObjectMapper objectMapper = new ObjectMapper();
        Drug drugFromFeed = objectMapper.readValue(drugOrderJson, Drug.class);
        when(webClient.get(any(String.class),any(Class.class))).thenReturn(drugFromFeed);

        referenceDataEventWorker.process(event);

        ArgumentCaptor<OpenERPRequest> drugRequestCatcher = ArgumentCaptor.forClass(OpenERPRequest.class);
        verify(openERPClient).execute(drugRequestCatcher.capture());

        OpenERPRequest labTestRequest = drugRequestCatcher.getValue();
        List<Parameter> parameters = labTestRequest.getParameters();

        Assert.assertTrue(isCorrect(parameters.get(0),"category", "create.drug", "string"));
        Assert.assertTrue(isCorrect(parameters.get(2),"last_read_entry_id", "event-id", "string"));
        Assert.assertTrue(isCorrect(parameters.get(3),"feed_uri_for_last_read_entry", feedUri, "string"));

        String drugJson = parameters.get(4).getValue();
        Drug drug = objectMapper.readValue(drugJson, Drug.class);

        Assert.assertEquals("249f3416-ad2b-4ef9-b8e9-d21965b05161",drug.getCategory().getId());
        Assert.assertEquals("e1c7904a-366f-484a-a388-04d267073e85",drug.getId());
        Assert.assertEquals("kofarrest",drug.getName());
        Assert.assertTrue(drug.getIsActive());
        Assert.assertEquals("810904ff-e601-4a9d-8935-d034a60d406d",drug.getPurchaseUnitOfMeasure().getId());
        Assert.assertEquals("e97967f1-7d01-43d9-8533-063b274df7be",drug.getSaleUnitOfMeasure().getId());
        Assert.assertEquals(2.0, drug.getSalePrice(),0.00009);
        Assert.assertEquals(60.0, drug.getCostPrice(),0.00009);

    }




    private  boolean isCorrect(Parameter parameter, String name,String value,String type){
        if(!name.equals(parameter.getName())){
            return false;
        }
        if(!value.equals(parameter.getValue())){
            return false;
        }
        if(!type.equals(parameter.getType())){
            return false;
        }
        return true;
    }
}
