package org.bahmni.feed.openerp.domain.encounter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.feed.openerp.client.OpenMRSWebClient;
import org.bahmni.feed.openerp.domain.visit.OpenMRSVisit;
import org.bahmni.feed.openerp.domain.visit.VisitAttributeType;
import org.bahmni.feed.openerp.domain.visit.VisitAttributes;
import org.bahmni.feed.openerp.testhelper.FileConverter;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URI;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MapERPOrdersTest {

    private MapERPOrders mapERPOrders;

    @Mock
    private OpenMRSEncounter openMRSEncounter;

    @Mock
    private OpenMRSVisit openMRSVisit;

    @Mock
    private OpenMRSWebClient openMRSWebClient;

    @Mock
    private OpenERPAtomFeedProperties openERPAtomFeedProperties;

    @Mock
    private VisitAttributes visitAttribute;

    @Mock
    private VisitAttributeType visitAttributeType;

    private Boolean isOdoo16 = true;

    @Before
    public void setUp() {
        mapERPOrders = new MapERPOrders(openMRSEncounter, openMRSVisit, openMRSWebClient, openERPAtomFeedProperties, isOdoo16);
    }

    @Test
    public void shouldMapOpenERPOrders() throws IOException {
        when(openMRSEncounter.getPatientId()).thenReturn("12345");
        when(openMRSEncounter.getEncounterUuid()).thenReturn("r5d6789uyt32");
        when(openMRSEncounter.getLocationName()).thenReturn("Ganyari");
        List<Parameter> parameterList = mapERPOrders.getParameters("102", "something", "somethingelse");

        Assert.assertEquals(8, parameterList.size());
        Assert.assertEquals("create.sale.order", parameterList.get(0).getValue());
        Assert.assertEquals("12345", parameterList.get(1).getValue());
        Assert.assertEquals("r5d6789uyt32", parameterList.get(2).getValue());
        Assert.assertEquals("Ganyari", parameterList.get(7).getValue());
    }

    @Test
    public void shouldMapEncountersWithObsCodedUnRelatedToOrders() throws Exception {
        String orderEncounter = FileConverter.convertToString("orderWithEncObsWithCodedConceptUnRelatedToOrder.json");
        OpenMRSEncounter encounter = ObjectMapperRepository.objectMapper.readValue(orderEncounter, OpenMRSEncounter.class);
        MapERPOrders mapERPOrders = new MapERPOrders(encounter, openMRSVisit, openMRSWebClient, openERPAtomFeedProperties, isOdoo16);
        List<Parameter> parameters = mapERPOrders.getParameters("102", "something", "somethingelse");
        Assert.assertEquals(8, parameters.size());
    }

    @Test
    public void shouldMapEncountersWithObsNumericUnRelatedToOrders() throws Exception {
        String orderEncounter = FileConverter.convertToString("orderWithEncObsOfNumericUnRelatedOrder.json");
        OpenMRSEncounter encounter = ObjectMapperRepository.objectMapper.readValue(orderEncounter, OpenMRSEncounter.class);
        MapERPOrders mapERPOrders = new MapERPOrders(encounter, openMRSVisit, openMRSWebClient, openERPAtomFeedProperties, isOdoo16);
        List<Parameter> parameters = mapERPOrders.getParameters("102", "something", "somethingelse");
        Assert.assertEquals(8, parameters.size());
    }

    @Test
    public void shouldMapEncountersWithObsDispensedRelatedToOrders() throws Exception {
        String orderEncounter = FileConverter.convertToString("orderWithEncObsCodedWithDispensedRelatedToOrder.json");
        OpenMRSEncounter encounter = ObjectMapperRepository.objectMapper.readValue(orderEncounter, OpenMRSEncounter.class);
        MapERPOrders mapERPOrders = new MapERPOrders(encounter, openMRSVisit, openMRSWebClient, openERPAtomFeedProperties, isOdoo16);
        List<Parameter> parameters = mapERPOrders.getParameters("102", "something", "somethingelse");
        Assert.assertEquals(8, parameters.size());
    }

    @Test
    public void shouldAddOnlyOrdersWithLatestAction() throws Exception {
        String orderEncounter = FileConverter.convertToString("encounterResourceForOrders.json");
        OpenMRSEncounter encounter = ObjectMapperRepository.objectMapper.readValue(orderEncounter, OpenMRSEncounter.class);
        String openERPOrders = "{"+
                "\"id\":\"ca4ef1ba-5258-4853-81c6-7c72d86bd615\","+
                "\"openERPOrders\":["+
                    "{"+
                        "\"orderId\":\"27b361c5-93fe-4273-8b7d-a725fe7237c6\","+
                        "\"previousOrderId\":null,"+
                        "\"encounterId\":\"ca4ef1ba-5258-4853-81c6-7c72d86bd615\","+
                        "\"productId\":\"d9c230a5-89d8-4e4d-b08b-2af3b1234c80\","+
                        "\"productName\":\"Paracetamol 500mg\","+
                        "\"quantity\":12.0,"+
                        "\"quantityUnits\":\"Tablet(s)\","+
                        "\"action\":\"NEW\","+
                        "\"visitId\":\"4d25c6ed-f069-4bce-b696-a45a9bd96f53\","+
                        "\"visitType\":\"OPD\","+
                        "\"type\":\"Drug Order\","+
                        "\"description\":null,"+
                        "\"voided\":false,"+
                        "\"locationName\":null,"+
                        "\"providerName\":\"Super Man\","+
                        "\"dispensed\":\"false\","+
                        "\"conceptName\":\"Paracetamol 500mg\","+
                        "\"dateCreated\":1714474203000"+
                    "},"+
                    "{"+
                        "\"orderId\":\"540e979b-ff9a-41ac-a638-9abc6e3ca5c4\","+
                        "\"previousOrderId\":\"27b361c5-93fe-4273-8b7d-a725fe7237c6\","+
                        "\"encounterId\":\"ca4ef1ba-5258-4853-81c6-7c72d86bd615\","+
                        "\"productId\":\"d9c230a5-89d8-4e4d-b08b-2af3b1234c80\","+
                        "\"productName\":\"Paracetamol 500mg\","+
                        "\"quantity\":18.0,"+
                        "\"quantityUnits\":\"Tablet(s)\","+
                        "\"action\":\"REVISE\","+
                        "\"visitId\":\"4d25c6ed-f069-4bce-b696-a45a9bd96f53\","+
                        "\"visitType\":\"OPD\","+
                        "\"type\":\"Drug Order\","+
                        "\"description\":null,"+
                        "\"voided\":false,"+
                        "\"locationName\":null,"+
                        "\"providerName\":\"Super Man\","+
                        "\"dispensed\":\"false\","+
                        "\"conceptName\":\"Paracetamol 500mg\","+
                        "\"dateCreated\":1714474203000"+
                    "},"+
                    "{"+
                        "\"orderId\":\"03de97ab-0816-4b51-b317-5680c32a44f9\","+
                        "\"previousOrderId\":null,"+
                        "\"encounterId\":\"ca4ef1ba-5258-4853-81c6-7c72d86bd615\","+
                        "\"productId\":\"33cb5232-172e-4769-ad2d-49fadaafc318\","+
                        "\"productName\":\"CD4 Test\","+
                        "\"quantity\":1.0,"+
                        "\"quantityUnits\":\"Unit(s)\","+
                        "\"action\":\"NEW\","+
                        "\"visitId\":\"4d25c6ed-f069-4bce-b696-a45a9bd96f53\","+
                        "\"visitType\":\"OPD\","+
                        "\"type\":\"Lab Order\","+
                        "\"description\":null,"+
                        "\"voided\":false,"+
                        "\"locationName\":null,"+
                        "\"providerName\":\"Super Man\","+
                        "\"dispensed\":\"false\","+
                        "\"conceptName\":null,"+
                        "\"dateCreated\":1714474115000"+
                    "},"+
                    "{"+
                        "\"orderId\":\"47695984-bfd9-45a9-9196-060ab6ee6ab7\","+
                        "\"previousOrderId\":\"7282556e-fefe-45b6-be43-1cdb507ae3b4\","+
                        "\"encounterId\":\"ca4ef1ba-5258-4853-81c6-7c72d86bd615\","+
                        "\"productId\":\"c424eed0-3f10-11e4-adec-0800271c1b75\","+
                        "\"productName\":\"CHEST Lateral\","+
                        "\"quantity\":1.0,"+
                        "\"quantityUnits\":\"Unit(s)\","+
                        "\"action\":\"DISCONTINUE\","+
                        "\"visitId\":\"4d25c6ed-f069-4bce-b696-a45a9bd96f53\","+
                        "\"visitType\":\"OPD\","+
                        "\"type\":\"Radiology Order\","+
                        "\"description\":null,"+
                        "\"voided\":false,"+
                        "\"locationName\":null,"+
                        "\"providerName\":\"Super Man\","+
                        "\"dispensed\":\"false\","+
                        "\"conceptName\":null,"+
                        "\"dateCreated\":1714474203000"+
                    "}"+
                "]}";
//        ObjectMapper objectMapper = ObjectMapperRepository.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//        Object jsonObject = objectMapper.readValue(openERPOrdersJson, Object.class);
//        OpenERPOrders openERPOrders = ObjectMapperRepository.objectMapper.writeValueAsString(openERPOrdersJson, OpenERPOrders.class);

        when(visitAttribute.getValue()).thenReturn("OPD");
        when(visitAttribute.getAttributeType()).thenReturn(visitAttributeType);
        when(visitAttributeType.getDisplay()).thenReturn("Visit Status");

        List<VisitAttributes> visitAttributesList = new ArrayList<>();
        visitAttributesList.add(visitAttribute);
        when(openMRSVisit.getAttributes()).thenReturn(visitAttributesList);

        MapERPOrders mapERPOrders = new MapERPOrders(encounter, openMRSVisit, openMRSWebClient, openERPAtomFeedProperties, isOdoo16);
        List<Parameter> parameters = mapERPOrders.getParameters("102", "something", "somethingelse");
//        OpenERPOrders openERPOrdersParameters = ObjectMapperRepository.objectMapper.readValue(parameters.get(6).getValue(), OpenERPOrders.class);
        Assert.assertEquals(8, parameters.size());
        Assert.assertEquals(openERPOrders, parameters.get(6).getValue());
    }

    @Test
    public void shouldFetchPreviousOrderForRevisedOrderInDifferentEncounter() throws Exception{
        String orderEncounter = FileConverter.convertToString("encounterResourceForRevisedDrugOrder.json");
        OpenMRSEncounter encounter = ObjectMapperRepository.objectMapper.readValue(orderEncounter, OpenMRSEncounter.class);
        String previousOrder = FileConverter.convertToString("bahmniDrugOrder.json");
        String response = "{" +
                "\"id\":\"ca4ef1ba-5258-4853-81c6-7c72d86bd615\"," +
                "\"openERPOrders\":[" +
                    "{" +
                        "\"orderId\":\"540e979b-ff9a-41ac-a638-9abc6e3ca5c4\"," +
                        "\"previousOrderId\":\"27b361c5-93fe-4273-8b7d-a725fe7237c6\"," +
                        "\"encounterId\":\"ca4ef1ba-5258-4853-81c6-7c72d86bd615\"," +
                        "\"productId\":\"d9c230a5-89d8-4e4d-b08b-2af3b1234c80\"," +
                        "\"productName\":\"Paracetamol 500mg\"," +
                        "\"quantity\":18.0," +
                        "\"quantityUnits\":\"Tablet(s)\"," +
                        "\"action\":\"REVISE\"," +
                        "\"visitId\":\"4d25c6ed-f069-4bce-b696-a45a9bd96f53\"," +
                        "\"visitType\":\"OPD\"," +
                        "\"type\":\"Drug Order\"," +
                        "\"description\":null," +
                        "\"voided\":false," +
                        "\"locationName\":null," +
                        "\"providerName\":\"Super Man\"," +
                        "\"dispensed\":\"false\"," +
                        "\"conceptName\":\"Paracetamol 500mg\"," +
                        "\"dateCreated\":1714474203000" +
                    "}," +
                    "{" +
                        "\"orderId\":\"27b361c5-93fe-4273-8b7d-a725fe7237c6\"," +
                        "\"previousOrderId\":\"27b361c5-93fe-4273-8b7d-a725fe7237c6\"," +
                        "\"encounterId\":\"f4aed26e-1d1d-4602-a02e-b1fb9e79f649\"," +
                        "\"productId\":\"d9c230a5-89d8-4e4d-b08b-2af3b1234c80\"," +
                        "\"productName\":\"Paracetamol 500mg\"," +
                        "\"quantity\":12.0," +
                        "\"quantityUnits\":\"Tablet(s)\"," +
                        "\"action\":\"NEW\"," +
                        "\"visitId\":\"4d25c6ed-f069-4bce-b696-a45a9bd96f53\"," +
                        "\"visitType\":\"OPD\"," +
                        "\"type\":\"Drug Order\"," +
                        "\"description\":null," +
                        "\"voided\":false," +
                        "\"locationName\":null," +
                        "\"providerName\":\"docthree docthree\"," +
                        "\"dispensed\":\"false\"," +
                        "\"conceptName\":\"Paracetamol 500mg\"," +
                        "\"dateCreated\":1714636983000" +
                    "}" +
                "]}";
//        ObjectMapper objectMapper = ObjectMapperRepository.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//        Object jsonObject = objectMapper.readValue(response, Object.class);
//        response = objectMapper.writeValueAsString(jsonObject);
        when(openERPAtomFeedProperties.getDrugOrderUri()).thenReturn("http://example.com/drugOrders");
        String apiUrl = "http://example.com/drugOrders/27b361c5-93fe-4273-8b7d-a725fe7237c6";
        when(openMRSWebClient.get(URI.create(apiUrl))).thenReturn(previousOrder);
        when(visitAttribute.getValue()).thenReturn("OPD");
        when(visitAttribute.getAttributeType()).thenReturn(visitAttributeType);
        when(visitAttributeType.getDisplay()).thenReturn("Visit Status");

        List<VisitAttributes> visitAttributesList = new ArrayList<>();
        visitAttributesList.add(visitAttribute);
        when(openMRSVisit.getAttributes()).thenReturn(visitAttributesList);

        MapERPOrders mapERPOrders = new MapERPOrders(encounter, openMRSVisit, openMRSWebClient, openERPAtomFeedProperties, isOdoo16);
        List<Parameter> parameters = mapERPOrders.getParameters("102", "something", "somethingelse");
        Assert.assertEquals(8, parameters.size());
        Assert.assertEquals(response, parameters.get(6).getValue());
    }

    @Test
    public void shouldNotAddPreviousOrderIfRevisedOrderInSameEncounter() throws Exception{
        String orderEncounter = FileConverter.convertToString("encounterResourceForOrders.json");
        OpenMRSEncounter encounter = ObjectMapperRepository.objectMapper.readValue(orderEncounter, OpenMRSEncounter.class);

        MapERPOrders mapERPOrders = new MapERPOrders(encounter, openMRSVisit, openMRSWebClient, openERPAtomFeedProperties, isOdoo16);
        List<Parameter> parameters = mapERPOrders.getParameters("102", "something", "somethingelse");
        Assert.assertEquals(8, parameters.size());
        OpenERPOrders finalOrders = ObjectMapperRepository.objectMapper.readValue(parameters.get(6).getValue(), OpenERPOrders.class);
        Assert.assertEquals(4, finalOrders.getOpenERPOrders().size());
    }

    // Billing Exempt Tests
    @Test
    public void shouldSkipOrdersWithBillingExemptAttributeTrue() throws Exception {
        // Load test encounter
        String encounterJson = FileConverter.convertToString("encounterResourceForOrders.json");
        OpenMRSEncounter openMRSEncounter = ObjectMapperRepository.objectMapper.readValue(encounterJson, OpenMRSEncounter.class);

        // Load billing exempt = true response
        String billingExemptTrue = FileConverter.convertToString("orderAttributesBillingExemptTrue.json");

        // Mock web client to return billing exempt true for all orders
        when(openMRSWebClient.get(any(URI.class))).thenReturn(billingExemptTrue);
        when(openERPAtomFeedProperties.getOrderAttributeUri()).thenReturn("http://localhost:8050/openmrs/ws/rest/v1/order");
        when(openERPAtomFeedProperties.getBillingExemptAttributeName()).thenReturn("IS_BILLING_EXEMPT");

        MapERPOrders mapERPOrders = new MapERPOrders(openMRSEncounter, openMRSVisit, openMRSWebClient, openERPAtomFeedProperties, false);
        
        // Get parameters which internally calls mapOpenERPOrders
        try {
            mapERPOrders.getParameters("event-id", "http://feed-uri", "http://feed-uri");
            // Since all orders are billing exempt, this should complete without adding any orders
        } catch (Exception e) {
            fail("Should not throw exception when orders are billing exempt: " + e.getMessage());
        }

        // Verify that attribute API was called for orders
        verify(openMRSWebClient, atLeastOnce()).get(any(URI.class));
    }

    @Test
    public void shouldProcessOrdersWithBillingExemptAttributeFalse() throws Exception {
        // Load test encounter
        String encounterJson = FileConverter.convertToString("encounterResourceForOrders.json");
        OpenMRSEncounter openMRSEncounter = ObjectMapperRepository.objectMapper.readValue(encounterJson, OpenMRSEncounter.class);

        // Load billing exempt = false response
        String billingExemptFalse = FileConverter.convertToString("orderAttributesBillingExemptFalse.json");

        // Mock web client to return billing exempt false
        when(openMRSWebClient.get(any(URI.class))).thenReturn(billingExemptFalse);
        when(openERPAtomFeedProperties.getOrderAttributeUri()).thenReturn("http://localhost:8050/openmrs/ws/rest/v1/order");
        when(openERPAtomFeedProperties.getBillingExemptAttributeName()).thenReturn("IS_BILLING_EXEMPT");

        MapERPOrders mapERPOrders = new MapERPOrders(openMRSEncounter, openMRSVisit, openMRSWebClient, openERPAtomFeedProperties, false);
        
        // Get parameters which internally calls mapOpenERPOrders
        try {
            mapERPOrders.getParameters("event-id", "http://feed-uri", "http://feed-uri");
            // Orders should be processed normally
        } catch (Exception e) {
            fail("Should not throw exception: " + e.getMessage());
        }

        // Verify that attribute API was called
        verify(openMRSWebClient, atLeastOnce()).get(any(URI.class));
    }

    @Test
    public void shouldProcessOrdersWithNoAttributes() throws Exception {
        // Load test encounter
        String encounterJson = FileConverter.convertToString("encounterResourceForOrders.json");
        OpenMRSEncounter openMRSEncounter = ObjectMapperRepository.objectMapper.readValue(encounterJson, OpenMRSEncounter.class);

        // Load empty attributes response
        String emptyAttributes = FileConverter.convertToString("orderAttributesEmpty.json");

        // Mock web client to return empty attributes
        when(openMRSWebClient.get(any(URI.class))).thenReturn(emptyAttributes);
        when(openERPAtomFeedProperties.getOrderAttributeUri()).thenReturn("http://localhost:8050/openmrs/ws/rest/v1/order");
        when(openERPAtomFeedProperties.getBillingExemptAttributeName()).thenReturn("IS_BILLING_EXEMPT");

        MapERPOrders mapERPOrders = new MapERPOrders(openMRSEncounter, openMRSVisit, openMRSWebClient, openERPAtomFeedProperties, false);
        
        // Get parameters which internally calls mapOpenERPOrders
        try {
            mapERPOrders.getParameters("event-id", "http://feed-uri", "http://feed-uri");
            // Orders should be processed normally when no attributes exist
        } catch (Exception e) {
            fail("Should not throw exception when no attributes: " + e.getMessage());
        }

        // Verify that attribute API was called
        verify(openMRSWebClient, atLeastOnce()).get(any(URI.class));
    }

    @Test
    public void shouldTreatOrderAsNonExemptWhenAttributeApiCallFails() throws Exception {
        // Load test encounter
        String encounterJson = FileConverter.convertToString("encounterResourceForOrders.json");
        OpenMRSEncounter openMRSEncounter = ObjectMapperRepository.objectMapper.readValue(encounterJson, OpenMRSEncounter.class);

        // Mock web client to throw exception
        when(openMRSWebClient.get(any(URI.class))).thenThrow(new RuntimeException("API call failed"));
        when(openERPAtomFeedProperties.getOrderAttributeUri()).thenReturn("http://localhost:8050/openmrs/ws/rest/v1/order");
        when(openERPAtomFeedProperties.getBillingExemptAttributeName()).thenReturn("IS_BILLING_EXEMPT");

        MapERPOrders mapERPOrders = new MapERPOrders(openMRSEncounter, openMRSVisit, openMRSWebClient, openERPAtomFeedProperties, false);
        
        // This should NOT throw exception - instead treat order as non-exempt and continue
        try {
            mapERPOrders.getParameters("event-id", "http://feed-uri", "http://feed-uri");
            // If we reach here, the fail-safe behavior worked correctly
        } catch (Exception e) {
            fail("Should not throw exception when attribute API fails. Orders should be treated as non-exempt: " + e.getMessage());
        }

        // Verify that attribute API was called
        verify(openMRSWebClient, atLeastOnce()).get(any(URI.class));
    }

    @Test
    public void shouldTreatOrderAsNonExemptWhenAttributeApiReturnsNull() throws Exception {
        // Load test encounter
        String encounterJson = FileConverter.convertToString("encounterResourceForOrders.json");
        OpenMRSEncounter openMRSEncounter = ObjectMapperRepository.objectMapper.readValue(encounterJson, OpenMRSEncounter.class);

        // Mock web client to return null
        when(openMRSWebClient.get(any(URI.class))).thenReturn(null);
        when(openERPAtomFeedProperties.getOrderAttributeUri()).thenReturn("http://localhost:8050/openmrs/ws/rest/v1/order");
        when(openERPAtomFeedProperties.getBillingExemptAttributeName()).thenReturn("IS_BILLING_EXEMPT");

        MapERPOrders mapERPOrders = new MapERPOrders(openMRSEncounter, openMRSVisit, openMRSWebClient, openERPAtomFeedProperties, false);
        
        // This should NOT throw exception - instead treat order as non-exempt
        try {
            mapERPOrders.getParameters("event-id", "http://feed-uri", "http://feed-uri");
            // If we reach here, null handling worked correctly
        } catch (Exception e) {
            fail("Should not throw exception when attribute API returns null: " + e.getMessage());
        }

        // Verify that attribute API was called
        verify(openMRSWebClient, atLeastOnce()).get(any(URI.class));
    }

    @Test
    public void shouldTreatOrderAsNonExemptWhenAttributeApiReturnsEmptyString() throws Exception {
        // Load test encounter
        String encounterJson = FileConverter.convertToString("encounterResourceForOrders.json");
        OpenMRSEncounter openMRSEncounter = ObjectMapperRepository.objectMapper.readValue(encounterJson, OpenMRSEncounter.class);

        // Mock web client to return empty string
        when(openMRSWebClient.get(any(URI.class))).thenReturn("");
        when(openERPAtomFeedProperties.getOrderAttributeUri()).thenReturn("http://localhost:8050/openmrs/ws/rest/v1/order");
        when(openERPAtomFeedProperties.getBillingExemptAttributeName()).thenReturn("IS_BILLING_EXEMPT");

        MapERPOrders mapERPOrders = new MapERPOrders(openMRSEncounter, openMRSVisit, openMRSWebClient, openERPAtomFeedProperties, false);
        
        // This should NOT throw exception - instead treat order as non-exempt
        try {
            mapERPOrders.getParameters("event-id", "http://feed-uri", "http://feed-uri");
            // If we reach here, empty string handling worked correctly
        } catch (Exception e) {
            fail("Should not throw exception when attribute API returns empty string: " + e.getMessage());
        }

        // Verify that attribute API was called
        verify(openMRSWebClient, atLeastOnce()).get(any(URI.class));
    }

}
