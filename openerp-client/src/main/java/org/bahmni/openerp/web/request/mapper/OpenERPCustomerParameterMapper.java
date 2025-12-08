package org.bahmni.openerp.web.request.mapper;


import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.bahmni.openerp.web.service.domain.Customer;

import java.util.Arrays;
import java.util.List;

public class OpenERPCustomerParameterMapper {

    public static final String OPENERP_CUSTOMER_NAME = "name";
    public static final String OPENERP_CUSTOMER_REF = "ref";
    public static final String OPENERP_CUSTOMER_VILLAGE = "village";
    public static final String CUSTOMER_RESOURCE = "res.partner";
    public static final String OPENERP_CUSTOMER_SEX = "sex";
    public static final String OPENERP_CUSTOMER_AGE = "age";

    public OpenERPRequest mapCustomerParams(Customer customer, String operation) {
        Parameter name = new Parameter(OPENERP_CUSTOMER_NAME,customer.getName(),"string") ;
        Parameter ref = new Parameter(OPENERP_CUSTOMER_REF,customer.getRef(),"string") ;
        Parameter village = new Parameter(OPENERP_CUSTOMER_VILLAGE,customer.getVillage(),"string") ;
        Parameter sex = new Parameter(OPENERP_CUSTOMER_SEX, customer.getSex(), "string") ;
        Parameter age = new Parameter(OPENERP_CUSTOMER_AGE,
                              customer.getAge() == null ? "" : customer.getAge().toString(),
                              "integer") ;

        List<Parameter> parameters = Arrays.asList(name, ref, village, sex, age);
        String resource = CUSTOMER_RESOURCE;
        OpenERPRequest request = new OpenERPRequest(resource, operation,parameters);
        return request;
    }
}
