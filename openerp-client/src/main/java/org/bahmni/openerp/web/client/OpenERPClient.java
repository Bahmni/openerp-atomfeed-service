package org.bahmni.openerp.web.client;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcSun15HttpTransportFactory;
import org.bahmni.openerp.web.OpenERPException;
import org.bahmni.openerp.web.OpenERPProperties;
import org.bahmni.openerp.web.http.client.HttpClient;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.RequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Vector;

@Service
@Lazy
public class OpenERPClient {
    public static final String XML_RPC_OBJECT_ENDPOINT = "/xmlrpc/object";
    public static final String XML_RPC_COMMON_ENDPOINT = "/xmlrpc/common";

    private final int connectionTimeoutInMilliseconds;
    private final int replyTimeoutInMilliseconds;
    private String host;
    private int port;
    private String database;
    private String user;
    private String password;

    private Object id;

    private XmlRpcClient xmlRpcClient;
    private HttpClient httpClient;

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public OpenERPClient(HttpClient httpClient, OpenERPProperties openERPProperties) {
        this.httpClient = httpClient;
        host = openERPProperties.getHost();
        port = openERPProperties.getPort();
        database = openERPProperties.getDatabase();
        user = openERPProperties.getUser();
        password = openERPProperties.getPassword();
        connectionTimeoutInMilliseconds = openERPProperties.getConnectionTimeoutInMilliseconds();
        replyTimeoutInMilliseconds = openERPProperties.getReplyTimeoutInMilliseconds();
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders requestHeaders = new HttpHeaders();
        return requestHeaders;
    }

    private Object executeRPC(XmlRpcClient loginRpcClient, Vector params, String methodName) {
        try {
            return loginRpcClient.execute(methodName, params);
        } catch (XmlRpcException e) {
            throw new OpenERPException(e);
        }
    }

    public Object search(String resource, Vector params) {
        return execute(resource, "search", params);
    }

    public Object read(String resource,Vector ids, Vector params) {
        return executeRead(resource, "read", ids, params);
    }

    public String execute(OpenERPRequest openERPRequest) {
        System.out.println("New execute With REST API Calls");
        WebClient client =  WebClient.builder()
                .baseUrl("http://localhost" + ":" + port)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        login();
        String requestBody = RequestBuilder.buildNewRequest(openERPRequest, id);
        String response = client.post().uri("/api/bahmni_data/").bodyValue(requestBody).retrieve().bodyToMono(String.class).block();
        if (response == null) {
            throw new OpenERPException("Login failed");
        }
        new OpenERPResponseErrorValidator().checkForError(response);
        return response;
    }

    private void login() {
        System.out.println("New Login With REST API Calls");

        HttpHeaders httpHeaders = getHttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (id == null) {
//            XmlRpcClient loginRpcClient = xmlRpcClient(XML_RPC_COMMON_ENDPOINT);
            System.out.println("Login started");
            System.out.println("host: " + host);
            System.out.println("port: " + port);
            Parameter username = new Parameter("username", user, "String");
            Parameter pass = new Parameter("password", password, "String");
            OpenERPRequest openERPRequest = new OpenERPRequest("res.users", "login", Arrays.asList(username, pass));
            System.out.println("openERPRequest Created");
            Object requestBody = RequestBuilder.buildNewRequest(openERPRequest, id);
            System.out.println("requestBody Created");
            System.out.println(requestBody);

            try{
                String url = "http://localhost:8069/api/login";
                ResponseEntity response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(requestBody, httpHeaders), String.class);
            System.out.println("response Created");
            System.out.println(response);
            if (response == null) {
                throw new OpenERPException("Login failed");
            }
//            new OpenERPResponseErrorValidator().checkForError(response);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println(e.toString());
            }

        }
    }

    public Object delete(String resource, Vector params) {
        return execute(resource, "unlink", params);
    }

    public Object execute(String resource, String operation, Vector params) {
        login();
        Object args[] = {database, (Integer) id, password, resource, operation, params};

        try {
            return xmlRpcClient(XML_RPC_OBJECT_ENDPOINT).execute("execute", args);
        } catch (XmlRpcException e) {
            throw new OpenERPException(e);
        }
    }

    public Object executeRead(String resource, String operation,Vector ids, Vector params) {
        login();
        Object args[] = {database, (Integer) id, password, resource, operation,ids, params};

        try {
            return xmlRpcClient(XML_RPC_OBJECT_ENDPOINT).execute("execute", args);
        } catch (XmlRpcException e) {
            throw new OpenERPException(e);
        }
    }

    public Object updateCustomerReceivables(String resource, Vector params) {
        return execute(resource, "update_customer_receivables", params);
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
