package org.bahmni.openerp.web.service;

import org.bahmni.openerp.web.OpenERPException;
import org.bahmni.openerp.web.client.strategy.OpenERPContext;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.mapper.OpenERPCustomerParameterMapper;
import org.bahmni.openerp.web.service.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Vector;

@Service
public class CustomerService {
    private OpenERPContext openERPContext;
    private OpenERPCustomerParameterMapper parameterMapper;

    @Autowired
    public CustomerService(OpenERPContext openERPContext) {
        this.openERPContext = openERPContext;
        this.parameterMapper = new OpenERPCustomerParameterMapper();
    }

    CustomerService(OpenERPContext openERPContext,OpenERPCustomerParameterMapper parameterMapper) {
        this.openERPContext = openERPContext;
        this.parameterMapper = parameterMapper;
    }

    public void create(Customer customer) {
        if (noCustomersFound(findCustomersWithPatientReference(customer.getRef()))) {
            OpenERPRequest request = parameterMapper.mapCustomerParams(customer, "create");
            openERPContext.execute(request);
        } else
            throw new OpenERPException(String.format("Customer with id, name already exists: %s, %s ", customer.getRef(), customer.getName()));
    }

    public void deleteCustomerWithPatientReference(String patientId) {
        Object[] customerIds = findCustomersWithPatientReference(patientId);
        Vector params = new Vector();
        params.addElement(customerIds[0]);
        openERPContext.delete("res.partner", params);
    }

    public Object[] findCustomersWithPatientReference(String patientId) {
        Object args[] = {"ref", "=", patientId};
        Vector params = new Vector();
        params.addElement(args);
        return (Object[]) openERPContext.search("res.partner", params);
    }

    private boolean noCustomersFound(Object[] customers) {
        return customers.length == 0;
    }
}
