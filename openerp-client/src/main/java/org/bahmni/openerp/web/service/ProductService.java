package org.bahmni.openerp.web.service;

import org.bahmni.openerp.web.client.OpenERPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Vector;

@Service
public class ProductService {
    private OpenERPClient openERPClient;

    @Autowired
    public ProductService(OpenERPClient openERPClient) {
        this.openERPClient = openERPClient;
    }

    public String findProductByName(String name) {
        Object args[] = {"name", "=", name};
        Vector params = new Vector();
        params.addElement(args);
        Object[] productIds = (Object[])openERPClient.search("product.product", params);
        Object[] productUUIDs = null;
        if(productIds.length > 0)   {
            Integer prodId = (Integer) productIds[0];
            Vector ids = new Vector();
            ids.add(prodId);
            Vector paramFields = new Vector();
            paramFields.add("uuid");
            productUUIDs = (Object[])openERPClient.read("product.product",ids, paramFields);
            if(productUUIDs.length > 0){
                Map productUUID = (Map) productUUIDs[0];
                return (String) productUUID.get("uuid");
            }
        }

        return null;
    }
}
