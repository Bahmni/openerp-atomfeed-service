package org.bahmni.openerp.web.client.strategy;

import org.bahmni.openerp.web.OpenERPException;
import org.bahmni.openerp.web.OpenERPProperties;
import org.bahmni.openerp.web.client.OpenERPResponseErrorValidator;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.http.client.HttpClient;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcSun15HttpTransportFactory;
import org.bahmni.openerp.web.request.builder.RequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.Vector;
import java.net.URL;

@Service
@Lazy
public class OpenERPXMLClient implements OpenERPClientStrategy {
    public static final String XML_RPC_OBJECT_ENDPOINT = "/xmlrpc/object";
    public static final String XML_RPC_COMMON_ENDPOINT = "/xmlrpc/common";

    private final int connectionTimeoutInMilliseconds;
    private final int replyTimeoutInMilliseconds;
    private final String host;
    private final int port;
    private final String database;
    private final String user;
    private final String password;

    private Object id;

    private XmlRpcClient xmlRpcClient;
    private final HttpClient httpClient;

    @Autowired
    public OpenERPXMLClient(HttpClient httpClient, OpenERPProperties openERPProperties) {
        this.httpClient = httpClient;
        host = openERPProperties.getHost();
        port = openERPProperties.getPort();
        database = openERPProperties.getDatabase();
        user = openERPProperties.getUser();
        password = openERPProperties.getPassword();
        connectionTimeoutInMilliseconds = openERPProperties.getConnectionTimeoutInMilliseconds();
        replyTimeoutInMilliseconds = openERPProperties.getReplyTimeoutInMilliseconds();
    }

    @Override
    public Object delete(String resource, Vector params) {
        return execute(resource, "unlink", params);
    }

    private void login() {
        if (id == null) {
            XmlRpcClient loginRpcClient = xmlRpcClient(XML_RPC_COMMON_ENDPOINT);

            Vector params = new Vector();
            params.addElement(database);
            params.addElement(user);
            params.addElement(password);

            Object loginId = executeRPC(loginRpcClient, params, "login");
            if(loginId == null || loginId.getClass() != Integer.class)
                throw new OpenERPException(String.format("Failed to login. The login id is : %s", loginId));
            id = loginId;
        }
    }

    @Override
    public Object execute(OpenERPRequest openERPRequest, String URI){
        login();
        String request = RequestBuilder.buildNewRequest(openERPRequest, id);
        String response = httpClient().post("http://" + host + ":" + port + XML_RPC_OBJECT_ENDPOINT, request);
        new OpenERPResponseErrorValidator().checkForError(response);
        return response;
    }

    @Override
    public Object execute(String resource, String operation, Vector params) {
        login();
        Object args[] = {database, (Integer) id, password, resource, operation, params};

        try {
            return xmlRpcClient(XML_RPC_OBJECT_ENDPOINT).execute("execute", args);
        } catch (XmlRpcException e) {
            throw new OpenERPException(e);
        }
    }

    @Override
    public Object executeRead(String resource, String operation,Vector ids, Vector params) {
        login();
        Object args[] = {database, (Integer) id, password, resource, operation,ids, params};

        try {
            return xmlRpcClient(XML_RPC_OBJECT_ENDPOINT).execute("execute", args);
        } catch (XmlRpcException e) {
            throw new OpenERPException(e);
        }
    }

    @Override
    public Object read(String resource,Vector ids, Vector params) {
        return executeRead(resource, "read", ids, params);
    }

    @Override
    public Object search(String resource, Vector params) {
        return execute(resource, "search", params);
    }

    @Override
    public Object updateCustomerReceivables(String resource, Vector params) {
        return execute(resource, "update_customer_receivables", params);
    }

    private Object executeRPC(XmlRpcClient loginRpcClient, Vector params, String methodName) {
        try {
            return loginRpcClient.execute(methodName, params);
        } catch (XmlRpcException e) {
            throw new OpenERPException(e);
        }
    }

    private HttpClient httpClient() {
        httpClient.setTimeout(replyTimeoutInMilliseconds);
        return httpClient;
    }

    private XmlRpcClient xmlRpcClient(String endpoint) {
        if (xmlRpcClient == null) {
            xmlRpcClient = createRPCClient();
        }
        XmlRpcClientConfigImpl clientConfig = (XmlRpcClientConfigImpl) xmlRpcClient.getClientConfig();
        try {
            clientConfig.setServerURL(new URL("http", host, port, endpoint));
        } catch (MalformedURLException e) {
            throw new OpenERPException(e);
        }
        return xmlRpcClient;
    }

    private XmlRpcClient createRPCClient() {
        XmlRpcClientConfigImpl clientConfiguration = new XmlRpcClientConfigImpl();
        clientConfiguration.setEnabledForExtensions(true);
        clientConfiguration.setEnabledForExceptions(true);
        clientConfiguration.setConnectionTimeout(connectionTimeoutInMilliseconds);
        clientConfiguration.setReplyTimeout(replyTimeoutInMilliseconds);

        XmlRpcClient rpcClient = new XmlRpcClient();
        rpcClient.setTransportFactory(new XmlRpcSun15HttpTransportFactory(rpcClient));
        rpcClient.setConfig(clientConfiguration);
        return rpcClient;
    }
}
