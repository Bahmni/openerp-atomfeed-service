package org.bahmni.openerp.web.client.strategy.implementation;

import org.bahmni.openerp.web.OpenERPProperties;
import org.bahmni.openerp.web.client.strategy.OpenERPClientStrategy;
import org.bahmni.openerp.web.http.client.RestClient;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.RequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Lazy
public class OdooRESTClient implements OpenERPClientStrategy {
    private final RestClient restClient;

    @Autowired
    public OdooRESTClient(OpenERPProperties openERPProperties) {
        String host = openERPProperties.getHost();
        int port = openERPProperties.getPort();
        String user = openERPProperties.getUser();
        String password = openERPProperties.getPassword();
        int connectionTimeoutInMilliseconds = openERPProperties.getConnectionTimeoutInMilliseconds();
        restClient = new RestClient("http://" + host + ":" + port, user, password, connectionTimeoutInMilliseconds);
    }

    @Override
    public Object execute(OpenERPRequest openERPRequest, String URI) {
        String requestBody = RequestBuilder.buildNewRestRequest(openERPRequest, UUID.randomUUID().toString());
        return restClient.post(URI, requestBody);
    }
}
