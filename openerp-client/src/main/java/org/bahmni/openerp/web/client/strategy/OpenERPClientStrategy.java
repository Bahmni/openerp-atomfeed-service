package org.bahmni.openerp.web.client.strategy;

import org.bahmni.openerp.web.request.OpenERPRequest;

public interface OpenERPClientStrategy {
    Object execute(OpenERPRequest openERPRequest, String URL);
}