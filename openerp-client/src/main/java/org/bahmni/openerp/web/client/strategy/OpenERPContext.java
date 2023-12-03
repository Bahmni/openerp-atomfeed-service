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

    public Object execute(OpenERPRequest openERPRequest, String URL) {
        return openERPClient.execute(openERPRequest, URL);
    }

}