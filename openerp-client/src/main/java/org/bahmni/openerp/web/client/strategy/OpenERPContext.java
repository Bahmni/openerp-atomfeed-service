package org.bahmni.openerp.web.client.strategy;

import org.bahmni.openerp.web.request.OpenERPRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Vector;

@Service
@Lazy
public class OpenERPContext {
    private final OpenERPClientStrategy openERPClient;

    @Autowired
    public OpenERPContext(OpenERPClientStrategy openERPClient) {
        this.openERPClient = openERPClient;
    }

    public void delete(String resource, Vector params) {
        openERPClient.delete(resource, params);
    }

    public Object execute(OpenERPRequest openERPRequest) {
        return openERPClient.execute(openERPRequest);
    }

    public Object execute(String resource, String operation, Vector params) {
        return openERPClient.execute(resource, operation, params);
    }

    public Object executeRead(String resource, String operation, Vector ids, Vector params) {
        return openERPClient.executeRead(resource, operation, ids, params);
    }

    public Object read(String resource, Vector ids, Vector params) {
        return openERPClient.read(resource, ids, params);
    }

    public Object search(String resource, Vector params) {
        return openERPClient.search(resource, params);
    }

    public Object updateCustomerReceivables(String resource, Vector params) {
        return openERPClient.updateCustomerReceivables(resource, params);
    }
}