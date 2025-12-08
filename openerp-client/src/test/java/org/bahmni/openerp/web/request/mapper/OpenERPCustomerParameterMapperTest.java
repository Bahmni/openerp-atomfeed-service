package org.bahmni.openerp.web.request.mapper;


import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.bahmni.openerp.web.service.domain.Customer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;

public class OpenERPCustomerParameterMapperTest {

    private OpenERPCustomerParameterMapper mapper;

    @Before
    public void setUp(){
        mapper = new OpenERPCustomerParameterMapper();
    }


    @Test
    public void shouldConvertCustomerToParameterList(){
        String name = "Ram Singh";
        String patientId ="GAN12345";
        String village="Ganiyari";
        String sex = "M";
        Integer age = 30;
        Customer customer = new Customer(name,patientId,village,age,sex);

        List<Parameter> expectedParams = asList(createParameter("name", name, "string"),
                createParameter("ref", patientId, "string"),
                createParameter("village", village, "string"),
                createParameter("sex", sex, "string"),
                createParameter("age", age.toString(), "integer"));

        OpenERPRequest expectedRequest = new OpenERPRequest("res.partner", "execute", expectedParams);

        OpenERPRequest request = mapper.mapCustomerParams(customer, "execute");
        Assert.assertEquals(expectedRequest, request);

    }

    private Parameter createParameter(String name, String value, String type) {
        return new Parameter(name, value, type);
    }

}
