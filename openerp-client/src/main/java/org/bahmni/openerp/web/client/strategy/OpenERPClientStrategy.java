package org.bahmni.openerp.web.client.strategy;

import org.bahmni.openerp.web.request.OpenERPRequest;

import java.util.Vector;

public interface OpenERPClientStrategy {

    void delete(String resource, Vector params);

    Object execute(OpenERPRequest openERPRequest);

    Object execute(String resource, String operation, Vector params);

    Object executeRead(String resource, String operation, Vector ids, Vector params);

    Object read(String resource, Vector ids, Vector params);

    Object search(String resource, Vector params);

    Object updateCustomerReceivables(String resource, Vector params);
}
