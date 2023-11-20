package org.bahmni.openerp.web.client.strategy;

import org.bahmni.openerp.web.request.OpenERPRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Vector;

@Service
@Lazy
public class OpenERPContext {
    private final OpenERPClientStrategy strategy;

    @Autowired
    public OpenERPContext(OpenERPClientStrategy strategy) {
        this.strategy = strategy;
    }

    public void delete(String resource, Vector params) {
        strategy.delete(resource, params);
    }

    public Object execute(OpenERPRequest openERPRequest) {
        return strategy.execute(openERPRequest);
    }

    public Object execute(String resource, String operation, Vector params) {
        return strategy.execute(resource, operation, params);
    }

    public Object executeRead(String resource, String operation, Vector ids, Vector params) {
        return strategy.executeRead(resource, operation, ids, params);
    }

    public Object read(String resource, Vector ids, Vector params) {
        return strategy.read(resource, ids, params);
    }

    public Object search(String resource, Vector params) {
        return strategy.search(resource, params);
    }

    public Object updateCustomerReceivables(String resource, Vector params) {
        return strategy.updateCustomerReceivables(resource, params);
    }
}
