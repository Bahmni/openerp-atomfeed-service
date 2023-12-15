package org.bahmni.openerp.web.client.strategy.implementation;

import org.bahmni.openerp.web.OpenERPProperties;
import org.bahmni.openerp.web.client.strategy.OpenERPClientStrategy;
import org.bahmni.openerp.web.http.client.RestClient;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.bahmni.openerp.web.request.builder.RequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Lazy
public class OdooRESTClient implements OpenERPClientStrategy {
    private final RestClient restClient;

    @Autowired
    public OdooRESTClient(OpenERPProperties openERPProperties) {
        final String host = openERPProperties.getHost();
        final int port = openERPProperties.getPort();
        final String user = openERPProperties.getUser();
        final String password = openERPProperties.getPassword();
        final int connectionTimeoutInMilliseconds = openERPProperties.getConnectionTimeoutInMilliseconds();
        restClient = new RestClient("http://" + host + ":" + port, user, password, connectionTimeoutInMilliseconds);
    }

    @Override
    public Object execute(OpenERPRequest openERPRequest, String URL) {
        String requestBody = "";
        List<Parameter> parameters = openERPRequest.getParameters();
        for (Parameter parameter : parameters) {
            if (parameter.getName().equals("category") && parameter.getValue().equals("create.sale.order")) {
                requestBody = RequestBuilder.buildNewJSONObject(openERPRequest, UUID.randomUUID().toString());
                return restClient.post(URL, requestBody);
            }
        }
        requestBody = RequestBuilder.buildNewRestRequest(openERPRequest, UUID.randomUUID().toString());
        return restClient.post(URL, requestBody);
    }
}
