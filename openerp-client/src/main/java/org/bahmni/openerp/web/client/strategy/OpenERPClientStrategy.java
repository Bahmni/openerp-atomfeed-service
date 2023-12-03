package org.bahmni.openerp.web.client.strategy;

import org.bahmni.openerp.web.request.OpenERPRequest;

import java.util.Vector;

public interface OpenERPClientStrategy {
    Object execute(OpenERPRequest openERPRequest, String URI);

}