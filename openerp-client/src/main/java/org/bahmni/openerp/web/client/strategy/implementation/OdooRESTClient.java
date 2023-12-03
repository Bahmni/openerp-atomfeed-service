package org.bahmni.openerp.web.client.strategy.implementation;

import org.bahmni.openerp.web.OpenERPProperties;
import org.bahmni.openerp.web.client.strategy.OpenERPClientStrategy;
import org.bahmni.openerp.web.http.client.RestClient;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.RequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class OdooRESTClient implements OpenERPClientStrategy {
    private final int connectionTimeoutInMilliseconds;
    private final int replyTimeoutInMilliseconds;
    private String host;
    private final int port;
    private final String user;
    private final String password;
    private final RestClient restClient;


    @Autowired
    public OdooRESTClient(OpenERPProperties openERPProperties) {
        host = openERPProperties.getHost();
        port = openERPProperties.getPort();
        user = openERPProperties.getUser();
        password = openERPProperties.getPassword();
        restClient = new RestClient("http://" + host + ":" + port, user, password);
        connectionTimeoutInMilliseconds = openERPProperties.getConnectionTimeoutInMilliseconds();
        replyTimeoutInMilliseconds = openERPProperties.getReplyTimeoutInMilliseconds();
    }

    @Override
    public Object execute(OpenERPRequest openERPRequest, String URI) {
        String requestBody = RequestBuilder.buildNewRestRequest(openERPRequest, UUID.randomUUID().toString());
        return restClient.post(URI, requestBody);
    }
}
