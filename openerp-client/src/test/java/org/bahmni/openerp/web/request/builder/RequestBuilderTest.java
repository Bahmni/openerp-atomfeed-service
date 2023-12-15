package org.bahmni.openerp.web.request.builder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class RequestBuilderTest {

    @Test
    public void shouldCreateNewXMLCustomerRequestWithPatientDataPopulated() throws Exception {

        int id = 1;
        String patientName = "Ramu";
        String patientId = "13466";
        String village = "Ganiyari";
        String database = "openerp";
        String password = "password";

        Parameter name = new Parameter("name", patientName, "string");
        Parameter ref = new Parameter("ref", patientId, "string");
        Parameter villageParam = new Parameter("village", village, "string");
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(name);
        parameters.add(ref);
        parameters.add(villageParam);
        OpenERPRequest request = new OpenERPRequest("res.partner", "execute", parameters);
        String requestXml = RequestBuilder.buildNewXMLRequest(request, id, database, password);

        String expected = "<?xml version='1.0' encoding='utf-8'?>\n" +
                "<methodCall>" +
                "    <methodName>execute</methodName>" +
                "    <params>" +
                "        <param>" +
                "        <value><string>" + database + "</string></value>" +
                "        </param>" +
                "        <param>" +
                "        <value><int>" + id + "</int></value>" +
                "        </param>" +
                "        <param>" +
                "        <value><string>" + password + "</string></value>" +
                "        </param>" +
                "        <param>" +
                "        <value><string>" + "res.partner" + "</string></value>" +
                "        </param>" +
                "        <param>" +
                "        <value><string>" + "execute" + "</string></value>" +
                "        </param>" +
                "        <param>" +
                "        <value><struct>" +
                "            <member>" +
                "                <name>name</name>" +
                "                <value><string>" + patientName + "</string></value>" +
                "            </member>" +
                "            <member>" +
                "                <name>ref</name>" +
                "                <value><string>" + patientId + "</string></value>" +
                "            </member>" +
                "            <member>" +
                "                <name>village</name>" +
                "                <value><string>" + village + "</string></value>" +
                "            </member>" +
                "        </struct></value>" +
                "        </param>" +
                "    </params>\n" +
                "</methodCall>";

        comparingStringWithoutSpaces(requestXml, expected);
    }

    @Test
    public void shouldCreateNewRESTCustomerRequestWithPatientDataPopulated() throws Exception {

        String id = "3a0ee6b3-4e3a-425d-a2ef-806ccb0d1744";
        String patientName = "Ramu";
        String patientId = "13466";
        String village = "Ganiyari";
        String expected = "{\"id\": \"3a0ee6b3-4e3a-425d-a2ef-806ccb0d1744\",\"params\": {\"data\": {\"name\": \"Ramu\",\"ref\": \"13466\",\"village\": \"Ganiyari\"}}\n}";

        Parameter name = new Parameter("name", patientName, "string");
        Parameter ref = new Parameter("ref", patientId, "string");
        Parameter villageParam = new Parameter("village", village, "string");
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(name);
        parameters.add(ref);
        parameters.add(villageParam);
        OpenERPRequest request = new OpenERPRequest("res.partner", "execute", parameters);
        String requestXml = RequestBuilder.buildNewRestRequest(request, id);

        comparingStringWithoutSpaces(requestXml, expected);
    }

    @Test
    public void shouldEscapeSpecialCharactersWhenXMLRequestIsCreated() throws Exception {
        String income = "<=10000";

        int id = 1;
        String database = "openerp";
        String password = "password";

        Parameter parameter = new Parameter("income", income, "string");
        ArrayList<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(parameter);

        OpenERPRequest request = new OpenERPRequest("res.partner", "execute", parameters);

        String requestXml = RequestBuilder.buildNewXMLRequest(request, id, database, password);

        String expected = "<?xml version='1.0' encoding='utf-8'?>\n" +
                "<methodCall>" +
                "    <methodName>execute</methodName>" +
                "    <params>" +
                "        <param>" +
                "        <value><string>" + database + "</string></value>" +
                "        </param>" +
                "        <param>" +
                "        <value><int>" + id + "</int></value>" +
                "        </param>" +
                "        <param>" +
                "        <value><string>" + password + "</string></value>" +
                "        </param>" +
                "        <param>" +
                "        <value><string>" + "res.partner" + "</string></value>" +
                "        </param>" +
                "        <param>" +
                "        <value><string>" + "execute" + "</string></value>" +
                "        </param>" +
                "        <param>" +
                "        <value><struct>" +
                "            <member>" +
                "                <name>income</name>" +
                "                <value><string>&lt;=10000</string></value>" +
                "            </member>" +
                "        </struct></value>" +
                "        </param>" +
                "    </params>\n" +
                "</methodCall>";

        comparingStringWithoutSpaces(requestXml, expected);
    }

    @Test
    public void shouldEscapeSpecialCharactersWhenRESTRequestIsCreated() throws Exception {
        String income = "<=10000";
        String id = "f1cfccea-6f5e-4d6b-9759-f55e92cd4f8f";
        String expected = "{\"id\": \"f1cfccea-6f5e-4d6b-9759-f55e92cd4f8f\",\"params\": {\"data\": {\"income\": \"<=10000\"}}\n}";

        Parameter parameter = new Parameter("income", income, "string");
        ArrayList<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(parameter);
        OpenERPRequest request = new OpenERPRequest("res.partner", "execute", parameters);
        String requestXml = RequestBuilder.buildNewRestRequest(request, id);

        comparingStringWithoutSpaces(requestXml, expected);
    }

    @Test
    public void shouldEscapeHindiCharactersWhenXMLRequestIsCreated() throws Exception {
        String name = "कृपा";

        int id = 1;
        String database = "openerp";
        String password = "password";

        Parameter parameter = new Parameter("name", name, "string");
        ArrayList<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(parameter);

        OpenERPRequest request = new OpenERPRequest("res.partner", "execute", parameters);

        String requestXml = RequestBuilder.buildNewXMLRequest(request, id, database, password);

        String expected = "<?xml version='1.0' encoding='utf-8'?>\n" +
                "<methodCall>" +
                "    <methodName>execute</methodName>" +
                "    <params>" +
                "        <param>" +
                "        <value><string>" + database + "</string></value>" +
                "        </param>" +
                "        <param>" +
                "        <value><int>" + id + "</int></value>" +
                "        </param>" +
                "        <param>" +
                "        <value><string>" + password + "</string></value>" +
                "        </param>" +
                "        <param>" +
                "        <value><string>" + "res.partner" + "</string></value>" +
                "        </param>" +
                "        <param>" +
                "        <value><string>" + "execute" + "</string></value>" +
                "        </param>" +
                "        <param>" +
                "        <value><struct>" +
                "            <member>" +
                "                <name>name</name>" +
                "                <value><string>कृपा</string></value>" +
                "            </member>" +
                "        </struct></value>" +
                "        </param>" +
                "    </params>\n" +
                "</methodCall>";

        comparingStringWithoutSpaces(requestXml, expected);
    }

    @Test
    public void shouldEscapeHindiCharactersWhenRESTRequestIsCreated() throws Exception {

        String name = "कृपा";
        String id = "6397d716-5ec8-47c6-b5c8-dd49e3e09235";
        String expected = "{\"id\": \"6397d716-5ec8-47c6-b5c8-dd49e3e09235\",\"params\": {\"data\": {\"name\": \"कृपा\"}}\n}";

        Parameter parameter = new Parameter("name", name, "string");
        ArrayList<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(parameter);
        OpenERPRequest request = new OpenERPRequest("res.partner", "execute", parameters);

        String requestXml = RequestBuilder.buildNewRestRequest(request, id);

        comparingStringWithoutSpaces(requestXml, expected);
    }

    private void comparingStringWithoutSpaces(String requestXml, String expected) {
        assertEquals(expected.replaceAll("\\s{2,}", ""), requestXml.replaceAll("\\s{2,}", "").trim());
    }

    @Test
    public void shouldBuildNewJsonObject() throws Exception {

        List<Parameter> parameters = Arrays.asList(
                new Parameter("category", "create.sale.order", "string"),
                new Parameter("customer_id", "GAN203012", "string"),
                new Parameter("encounter_id", "8bb8cdbb-82f1-4aac-b610-24498bc2cf70", "string"),
                new Parameter("feed_uri", "http://openmrs:8080/openmrs/ws/atomfeed/encounter/recent", "string"),
                new Parameter("last_read_entry_id", "tag:atomfeed.ict4h.org:6aa022bf-395f-45b6-867b-7a5df9918d41", "string"),
                new Parameter("feed_uri_for_last_read_entry", "http://openmrs:8080/openmrs/ws/atomfeed/encounter/6", "string"),
                new Parameter("locationName", "General Ward", "string"),
                new Parameter("orders", "{\"id\":\"8bb8cdbb-82f1-4aac-b610-24498bc2cf70\",\"openERPOrders\":[{\"orderId\":\"304ddc83-c3dd-4754-bef8-913b8bf68de8\",\"previousOrderId\":null,\"encounterId\":\"8bb8cdbb-82f1-4aac-b610-24498bc2cf70\",\"productId\":\"014bd216-72bd-44ae-b25a-270ed10bb2da\",\"productName\":\"Monocyte\",\"quantity\":1.0,\"quantityUnits\":\"Unit(s)\",\"action\":\"NEW\",\"visitId\":\"fcbe0bca-2638-4281-8f29-a21d4ca15fe6\",\"visitType\":\"OPD\",\"type\":\"Lab Order\",\"description\":null,\"voided\":false,\"locationName\":null,\"providerName\":\"Super Man\",\"dispensed\":\"false\",\"conceptName\":null}]}", "string")
        );

        OpenERPRequest openERPRequest = new OpenERPRequest("res.partner", "execute", parameters);

        String mockUuid = UUID.randomUUID().toString();
        String result = RequestBuilder.buildNewJSONObject(openERPRequest, mockUuid);

        String expectedJson = "{\n" +
                "    \"id\": \"" + mockUuid + "\",\n" +
                "    \"params\": {\n" +
                "        \"data\": {\n" +
                "            \"category\": \"create.sale.order\",\n" +
                "            \"customer_id\": \"GAN203012\",\n" +
                "            \"encounter_id\": \"8bb8cdbb-82f1-4aac-b610-24498bc2cf70\",\n" +
                "            \"feed_uri\": \"http://openmrs:8080/openmrs/ws/atomfeed/encounter/recent\",\n" +
                "            \"last_read_entry_id\": \"tag:atomfeed.ict4h.org:6aa022bf-395f-45b6-867b-7a5df9918d41\",\n" +
                "            \"feed_uri_for_last_read_entry\": \"http://openmrs:8080/openmrs/ws/atomfeed/encounter/6\",\n" +
                "            \"locationName\": \"General Ward\",\n" +
                "            \"orders\": {\n" +
                "                \"id\": \"8bb8cdbb-82f1-4aac-b610-24498bc2cf70\",\n" +
                "                \"openERPOrders\": [\n" +
                "                    {\n" +
                "                        \"orderId\": \"304ddc83-c3dd-4754-bef8-913b8bf68de8\",\n" +
                "                        \"previousOrderId\": null,\n" +
                "                        \"encounterId\": \"8bb8cdbb-82f1-4aac-b610-24498bc2cf70\",\n" +
                "                        \"productId\": \"014bd216-72bd-44ae-b25a-270ed10bb2da\",\n" +
                "                        \"productName\": \"Monocyte\",\n" +
                "                        \"quantity\": 1.0,\n" +
                "                        \"quantityUnits\": \"Unit(s)\",\n" +
                "                        \"action\": \"NEW\",\n" +
                "                        \"visitId\": \"fcbe0bca-2638-4281-8f29-a21d4ca15fe6\",\n" +
                "                        \"visitType\": \"OPD\",\n" +
                "                        \"type\": \"Lab Order\",\n" +
                "                        \"description\": null,\n" +
                "                        \"voided\": false,\n" +
                "                        \"locationName\": null,\n" +
                "                        \"providerName\": \"Super Man\",\n" +
                "                        \"dispensed\": \"false\",\n" +
                "                        \"conceptName\": null\n" +
                "                    }\n" +
                "                ]\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode expectedJsonNode = objectMapper.readTree(expectedJson);
        JsonNode actualJsonNode = objectMapper.readTree(result);

        assertEquals(expectedJsonNode, actualJsonNode);
    }

}