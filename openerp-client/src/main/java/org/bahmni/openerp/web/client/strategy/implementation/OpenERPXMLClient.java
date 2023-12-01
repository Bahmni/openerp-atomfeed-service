package org.bahmni.openerp.web.client.strategy.implementation;

import org.bahmni.openerp.web.OpenERPException;
import org.bahmni.openerp.web.OpenERPProperties;
import org.bahmni.openerp.web.client.OpenERPResponseErrorValidator;
import org.bahmni.openerp.web.client.strategy.OpenERPClientStrategy;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.http.client.XMLClient;
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
    private final XMLClient xmlClient;

    @Autowired
    public OpenERPXMLClient(XMLClient xmlClient, OpenERPProperties openERPProperties) {
        this.xmlClient = xmlClient;
        host = openERPProperties.getHost();
        port = openERPProperties.getPort();
        database = openERPProperties.getDatabase();
        user = openERPProperties.getUser();
        password = openERPProperties.getPassword();
        connectionTimeoutInMilliseconds = openERPProperties.getConnectionTimeoutInMilliseconds();
        replyTimeoutInMilliseconds = openERPProperties.getReplyTimeoutInMilliseconds();
    }

    private void login() {
        if (id == null) {
            System.out.println("-------------LOGIN STARTED-----------------");
            XmlRpcClient loginRpcClient = xmlRpcClient(XML_RPC_COMMON_ENDPOINT);
            Vector<String> params = new Vector<String>();
            params.addElement(database);
            params.addElement(user);
            params.addElement(password);
            Object loginId = executeRPC(loginRpcClient, params, "login");
            if(loginId == null || loginId.getClass() != Integer.class)
                throw new OpenERPException(String.format("Failed to login. The login id is : %s", loginId));
            id = loginId;
            System.out.println("-------------LOGIN ENDED-----------------");
        }
    }

    @Override
    public Object execute(OpenERPRequest openERPRequest, String URI) {
        login();
        System.out.println("-------------REQUEST EXECUTION STARTED-----------------");
        String request = RequestBuilder.buildNewXMLRequest(openERPRequest, id, database, password);
        System.out.println("Request Body : "+request);
        String response = httpClient().post("http://" + host + ":" + port + XML_RPC_OBJECT_ENDPOINT, request);
        new OpenERPResponseErrorValidator().checkForError(response);
        System.out.println("Response Body : "+response);
        System.out.println("-------------REQUEST EXECUTION ENDED-----------------");
        return response;
    }


    private Object executeRPC(XmlRpcClient loginRpcClient, Vector params, String methodName) {
        try {
            return loginRpcClient.execute(methodName, params);
        } catch (XmlRpcException e) {
            throw new OpenERPException(e);
        }
    }

    private XMLClient httpClient() {
        xmlClient.setTimeout(replyTimeoutInMilliseconds);
        return xmlClient;
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