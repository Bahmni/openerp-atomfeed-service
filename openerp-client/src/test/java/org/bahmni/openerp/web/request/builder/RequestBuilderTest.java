package org.bahmni.openerp.web.request.builder;

import org.apache.commons.lang3.StringEscapeUtils;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:applicationContext-Test.xml")
public class RequestBuilderTest {

    @Autowired
    RequestBuilder requestBuilder;
    private final OpenERPRequestTestHelper openERPRequestTestHelper = new OpenERPRequestTestHelper();

    @Test
    public void shouldCreateNewCustomerRequestWithPatientDataPopulated() throws Exception {

        String patientName = "Ramu";
        String patientId = "13466";
        String village = "Ganiyari";

        List<Parameter> parameters = openERPRequestTestHelper.createCustomerRequest(patientName, patientId, village);

        int id = 1;
        String database = "openerp";
        String password = "password";

        OpenERPRequest request = new OpenERPRequest("res.partner", "execute", parameters);

        String requestXml = requestBuilder.buildNewRequest(request, id, database, password);
        //String requestXmlForComparison = requestXml.replace("", " ");

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
    public void shouldEscapeSpecialCharacters() throws Exception {
        String income = "<=10000";

        int id = 1;
        String database = "openerp";
        String password = "password";

        Parameter parameter = new Parameter("income", income, "string");
        ArrayList<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(parameter);

        OpenERPRequest request = new OpenERPRequest("res.partner", "execute", parameters);

        String requestXml = requestBuilder.buildNewRequest(request, id, database, password);

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
    public void shouldEscapeHindiCharacters() throws Exception {
        String name = "कृपा";

        int id = 1;
        String database = "openerp";
        String password = "password";

        Parameter parameter = new Parameter("name", name, "string");
        ArrayList<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(parameter);

        OpenERPRequest request = new OpenERPRequest("res.partner", "execute", parameters);

        String requestXml = requestBuilder.buildNewRequest(request, id, database, password);

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

    private void comparingStringWithoutSpaces(String requestXml, String expected) {
        assertEquals(expected.replaceAll("\\s{2,}", ""), requestXml.replaceAll("\\s{2,}", "").trim());
    }
}
