package org.bahmni.openerp.web.service;


import org.bahmni.openerp.web.service.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpenERPService {
    private CustomerAccountService customerAccountService;
    private CustomerService customerService;
    private ProductService productService;

    @Autowired
    public OpenERPService(CustomerService customerService, CustomerAccountService customerAccountService, ProductService productService){
        this.customerService = customerService;
        this.customerAccountService = customerAccountService;
        this.productService = productService;
    }

    public void createCustomer(String name, String patientId, String village) {
        customerService.create(new Customer(name,patientId,village));
    }

    public void updateCustomerBalance(String patientId, double balance) {
        customerAccountService.updateCustomerReceivables(patientId, balance);
    }

    public Object[] findCustomers(String patientId) {
        Object[] customerIds = customerService.findCustomersWithPatientReference(patientId);
        return customerIds;
    }

    public void deleteCustomer(String patientId) throws Exception {
        customerService.deleteCustomerWithPatientReference(patientId);
    }

    public String findProduct(String name) {
        return productService.findProductByName(name);
    }


}

