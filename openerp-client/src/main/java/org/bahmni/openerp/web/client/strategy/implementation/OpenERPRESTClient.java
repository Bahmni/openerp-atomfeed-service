package org.bahmni.openerp.web.client.strategy.implementation;

import org.bahmni.openerp.web.OpenERPException;
import org.bahmni.openerp.web.OpenERPProperties;
import org.bahmni.openerp.web.client.strategy.OpenERPClientStrategy;
import org.bahmni.openerp.web.http.client.RestClient;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.bahmni.openerp.web.request.builder.RequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Vector;

@Service
@Lazy
public class OpenERPRESTClient implements OpenERPClientStrategy {

    private final int connectionTimeoutInMilliseconds;
    private final int replyTimeoutInMilliseconds;
    private String host;
    private int port;
    private String user;
    private String password;

    private String id;

    private RestClient restClient;


    @Autowired
    public OpenERPRESTClient(OpenERPProperties openERPProperties) {
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
        String requestBody = RequestBuilder.buildNewRestRequest(openERPRequest, id);
        return restClient.post(URI, requestBody);
    }
}
