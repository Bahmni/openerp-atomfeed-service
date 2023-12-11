package org.bahmni.openerp.web.request.builder;

import org.bahmni.openerp.web.request.OpenERPRequest;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
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
}