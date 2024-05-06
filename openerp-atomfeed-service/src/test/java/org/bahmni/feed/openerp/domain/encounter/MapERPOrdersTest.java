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
        String openERPOrdersJson = "{\n  " +
                "\"id\": \"ca4ef1ba-5258-4853-81c6-7c72d86bd615\",\n  " +
                "\"openERPOrders\": [\n    " +
                    "{\n      " +
                        "\"orderId\": \"27b361c5-93fe-4273-8b7d-a725fe7237c6\",\n      " +
                        "\"previousOrderId\": null,\n      " +
                        "\"encounterId\": \"ca4ef1ba-5258-4853-81c6-7c72d86bd615\",\n      " +
                        "\"productId\": \"d9c230a5-89d8-4e4d-b08b-2af3b1234c80\",\n      " +
                        "\"productName\": \"Paracetamol 500mg\",\n      " +
                        "\"quantity\": 12.0,\n      " +
                        "\"quantityUnits\": \"Tablet(s)\",\n      " +
                        "\"action\": \"NEW\",\n      " +
                        "\"visitId\": \"4d25c6ed-f069-4bce-b696-a45a9bd96f53\",\n      " +
                        "\"visitType\": \"OPD\",\n      " +
                        "\"type\": \"Drug Order\",\n      " +
                        "\"description\": null,\n      " +
                        "\"voided\": false,\n      " +
                        "\"locationName\": null,\n      " +
                        "\"providerName\": \"Super Man\",\n      " +
                        "\"dispensed\": \"false\",\n      " +
                        "\"conceptName\": \"Paracetamol 500mg\",\n      " +
                        "\"dateCreated\": 1714474203000\n    " +
                    "},\n    " +
                    "{\n      " +
                        "\"orderId\": \"540e979b-ff9a-41ac-a638-9abc6e3ca5c4\",\n      " +
                        "\"previousOrderId\": \"27b361c5-93fe-4273-8b7d-a725fe7237c6\",\n      " +
                        "\"encounterId\": \"ca4ef1ba-5258-4853-81c6-7c72d86bd615\",\n      " +
                        "\"productId\": \"d9c230a5-89d8-4e4d-b08b-2af3b1234c80\",\n      " +
                        "\"productName\": \"Paracetamol 500mg\",\n      " +
                        "\"quantity\": 18.0,\n      " +
                        "\"quantityUnits\": \"Tablet(s)\",\n      " +
                        "\"action\": \"REVISE\",\n      " +
                        "\"visitId\": \"4d25c6ed-f069-4bce-b696-a45a9bd96f53\",\n      " +
                        "\"visitType\": \"OPD\",\n      " +
                        "\"type\": \"Drug Order\",\n      " +
                        "\"description\": null,\n      " +
                        "\"voided\": false,\n      " +
                        "\"locationName\": null,\n      " +
                        "\"providerName\": \"Super Man\",\n      " +
                        "\"dispensed\": \"false\",\n      " +
                        "\"conceptName\": \"Paracetamol 500mg\",\n      " +
                        "\"dateCreated\": 1714474203000\n    " +
                    "},\n    " +
                    "{\n      " +
                        "\"orderId\": \"03de97ab-0816-4b51-b317-5680c32a44f9\",\n     " +
                        "\"previousOrderId\": null,\n      " +
                        "\"encounterId\": \"ca4ef1ba-5258-4853-81c6-7c72d86bd615\",\n      " +
                        "\"productId\": \"33cb5232-172e-4769-ad2d-49fadaafc318\",\n      " +
                        "\"productName\": \"CD4 Test\",\n      \"quantity\": 1.0,\n      " +
                        "\"quantityUnits\": \"Unit(s)\",\n      " +
                        "\"action\": \"NEW\",\n      " +
                        "\"visitId\": \"4d25c6ed-f069-4bce-b696-a45a9bd96f53\",\n      " +
                        "\"visitType\": \"OPD\",\n      " +
                        "\"type\": \"Lab Order\",\n      " +
                        "\"description\": null,\n      " +
                        "\"voided\": false,\n      " +
                        "\"locationName\": null,\n      " +
                        "\"providerName\": \"Super Man\",\n      " +
                        "\"dispensed\": \"false\",\n      " +
                        "\"conceptName\": null,\n      " +
                        "\"dateCreated\": 1714474115000\n    " +
                    "},\n    " +
                    "{\n      " +
                        "\"orderId\": \"47695984-bfd9-45a9-9196-060ab6ee6ab7\",\n      " +
                        "\"previousOrderId\": \"7282556e-fefe-45b6-be43-1cdb507ae3b4\",\n      " +
                        "\"encounterId\": \"ca4ef1ba-5258-4853-81c6-7c72d86bd615\",\n      " +
                        "\"productId\": \"c424eed0-3f10-11e4-adec-0800271c1b75\",\n      " +
                        "\"productName\": \"CHEST Lateral\",\n      " +
                        "\"quantity\": 1.0,\n      " +
                        "\"quantityUnits\": \"Unit(s)\",\n      " +
                        "\"action\": \"DISCONTINUE\",\n      " +
                        "\"visitId\": \"4d25c6ed-f069-4bce-b696-a45a9bd96f53\",\n     " +
                        "\"visitType\": \"OPD\",\n      " +
                        "\"type\": \"Radiology Order\",\n      " +
                        "\"description\": null,\n      " +
                        "\"voided\": false,\n      " +
                        "\"locationName\": null,\n      " +
                        "\"providerName\": \"Super Man\",\n      " +
                        "\"dispensed\": \"false\",\n      " +
                        "\"conceptName\": null,\n      " +
                        "\"dateCreated\": 1714474203000\n    " +
                    "}\n    " +
                "]\n}";
        ObjectMapper objectMapper = ObjectMapperRepository.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        Object jsonObject = objectMapper.readValue(openERPOrdersJson, Object.class);
        openERPOrdersJson = objectMapper.writeValueAsString(jsonObject);

        when(visitAttribute.getValue()).thenReturn("OPD");
        when(visitAttribute.getAttributeType()).thenReturn(visitAttributeType);
        when(visitAttributeType.getDisplay()).thenReturn("Visit Status");

        List<VisitAttributes> visitAttributesList = new ArrayList<>();
        visitAttributesList.add(visitAttribute);
        when(openMRSVisit.getAttributes()).thenReturn(visitAttributesList);

        MapERPOrders mapERPOrders = new MapERPOrders(encounter, openMRSVisit, openMRSWebClient, openERPAtomFeedProperties, isOdoo16);
        List<Parameter> parameters = mapERPOrders.getParameters("102", "something", "somethingelse");
        Assert.assertEquals(8, parameters.size());
        Assert.assertEquals(openERPOrdersJson, parameters.get(6).getValue());
    }

    @Test
    public void shouldFetchPreviousOrderForRevisedOrderInDifferentEncounter() throws Exception{
        String orderEncounter = FileConverter.convertToString("encounterResourceForRevisedDrugOrder.json");
        OpenMRSEncounter encounter = ObjectMapperRepository.objectMapper.readValue(orderEncounter, OpenMRSEncounter.class);
        String previousOrder = FileConverter.convertToString("bahmniDrugOrder.json");
        String response = "{\n  " +
                "\"id\": \"ca4ef1ba-5258-4853-81c6-7c72d86bd615\",\n  " +
                "\"openERPOrders\": [\n    " +
                    "{\n      " +
                        "\"orderId\": \"540e979b-ff9a-41ac-a638-9abc6e3ca5c4\",\n      " +
                        "\"previousOrderId\": \"27b361c5-93fe-4273-8b7d-a725fe7237c6\",\n      " +
                        "\"encounterId\": \"ca4ef1ba-5258-4853-81c6-7c72d86bd615\",\n      " +
                        "\"productId\": \"d9c230a5-89d8-4e4d-b08b-2af3b1234c80\",\n      " +
                        "\"productName\": \"Paracetamol 500mg\",\n      " +
                        "\"quantity\": 18.0,\n      " +
                        "\"quantityUnits\": \"Tablet(s)\",\n      " +
                        "\"action\": \"REVISE\",\n      " +
                        "\"visitId\": \"4d25c6ed-f069-4bce-b696-a45a9bd96f53\",\n      " +
                        "\"visitType\": \"OPD\",\n      " +
                        "\"type\": \"Drug Order\",\n      " +
                        "\"description\": null,\n      " +
                        "\"voided\": false,\n      " +
                        "\"locationName\": null,\n      " +
                        "\"providerName\": \"Super Man\",\n      " +
                        "\"dispensed\": \"false\",\n      " +
                        "\"conceptName\": \"Paracetamol 500mg\",\n      " +
                        "\"dateCreated\": 1714474203000\n    " +
                    "},\n    " +
                    "{\n      " +
                        "\"orderId\": \"27b361c5-93fe-4273-8b7d-a725fe7237c6\",\n      " +
                        "\"previousOrderId\": \"27b361c5-93fe-4273-8b7d-a725fe7237c6\",\n      " +
                        "\"encounterId\": \"f4aed26e-1d1d-4602-a02e-b1fb9e79f649\",\n      " +
                        "\"productId\": \"d9c230a5-89d8-4e4d-b08b-2af3b1234c80\",\n      " +
                        "\"productName\": \"Paracetamol 500mg\",\n      " +
                        "\"quantity\": 12.0,\n      " +
                        "\"quantityUnits\": \"Tablet(s)\",\n      " +
                        "\"action\": \"NEW\",\n      " +
                        "\"visitId\": \"4d25c6ed-f069-4bce-b696-a45a9bd96f53\",\n      " +
                        "\"visitType\": \"OPD\",\n      " +
                        "\"type\": \"Drug Order\",\n      " +
                        "\"description\": null,\n      " +
                        "\"voided\": false,\n      " +
                        "\"locationName\": null,\n      " +
                        "\"providerName\": \"docthree docthree\",\n      " +
                        "\"dispensed\": \"false\",\n      " +
                        "\"conceptName\": \"Paracetamol 500mg\",\n      " +
                        "\"dateCreated\": 1714636983000\n    " +
                    "}\n    " +
                "]\n}";
        ObjectMapper objectMapper = ObjectMapperRepository.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        Object jsonObject = objectMapper.readValue(response, Object.class);
        response = objectMapper.writeValueAsString(jsonObject);
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

}
