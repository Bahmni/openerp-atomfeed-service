package org.bahmni.openerp.web.client.strategy;

import org.bahmni.openerp.web.request.OpenERPRequest;

import java.util.Vector;

public class OpenERPRestClient implements OpenERPClientStrategy {
    @Override
    public Object delete(String resource, Vector params) {
        return null;
    }

    @Override
    public Object execute(OpenERPRequest openERPRequest, String URI) {
        return null;
    }

    @Override
    public Object execute(String resource, String operation, Vector params) {
        return null;
    }

    @Override
    public Object executeRead(String resource, String operation, Vector ids, Vector params) {
        return null;
    }

    @Override
    public Object read(String resource, Vector ids, Vector params) {
        return null;
    }

    @Override
    public Object search(String resource, Vector params) {
        return null;
    }

    @Override
    public Object updateCustomerReceivables(String resource, Vector params) {
        return null;
    }
}
