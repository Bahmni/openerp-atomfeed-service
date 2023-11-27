//package org.bahmni.openerp.web.client.strategy.implementation;
//
//import org.bahmni.openerp.web.OpenERPException;
//import org.bahmni.openerp.web.OpenERPProperties;
//import org.bahmni.openerp.web.client.strategy.OpenERPClientStrategy;
////import org.bahmni.openerp.web.http.client.RestClient;
//import org.bahmni.openerp.web.request.OpenERPRequest;
//import org.bahmni.openerp.web.request.builder.Parameter;
//import org.bahmni.openerp.web.request.builder.RequestBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Vector;
//
//@Service
//@Lazy
//public class OpenERPRESTClient implements OpenERPClientStrategy {
//
//    private final int connectionTimeoutInMilliseconds;
//    private final int replyTimeoutInMilliseconds;
//    private String host;
//    private int port;
//    private String user;
//    private String password;
//
//    private String id;
//
////    private RestClient restClient;
//
//
//    @Autowired
//    public OpenERPRESTClient(OpenERPProperties openERPProperties) {
//        host = openERPProperties.getHost();
//        port = openERPProperties.getPort();
//        user = openERPProperties.getUser();
//        password = openERPProperties.getPassword();
////        restClient = new RestClient("http://" + host + ":" + port, user, password);
//        connectionTimeoutInMilliseconds = openERPProperties.getConnectionTimeoutInMilliseconds();
//        replyTimeoutInMilliseconds = openERPProperties.getReplyTimeoutInMilliseconds();
//    }
//    @Override
//    public void delete(String resource, Vector params)  {
//        execute(resource, "unlink", params);
//
//    }
//
//    @Override
//    public Object execute(OpenERPRequest openERPRequest) {
//        String requestBody = RequestBuilder.buildNewRestRequest(openERPRequest, id);
//        return "";
////        return restClient.post("", requestBody);
//    }
//
//    @Override
//    public Object execute(String resource, String operation, Vector params) {
//
//        return execute(parameterMapper(resource, operation, params));
//    }
//
//    @Override
//    public Object executeRead(String resource, String operation, Vector ids, Vector params) {
//        return null;
//    }
//
//    @Override
//    public Object read(String resource, Vector ids, Vector params) {
//        return executeRead(resource, "read", ids, params);
//    }
//
//    @Override
//    public Object search(String resource, Vector params)  {
//        return execute(resource, "search", params);
//    }
//
//    @Override
//    public Object updateCustomerReceivables(String resource, Vector params) {
//        return execute(resource, "update_customer_receivables", params);
//    }
//
//    private OpenERPRequest parameterMapper(String resource, String operation, Vector<Object[]> params) {
//        ArrayList<Parameter> paramsList = new ArrayList<Parameter>();
//        for (Object[] arr : params) {
//            if (arr.length >= 2) {
//                paramsList.add(new Parameter(arr[0].toString(), arr[1].toString()));
//            }else{
//                throw new OpenERPException("Invalid parameters");
//            }
//        }
//        return new OpenERPRequest(resource, operation, paramsList);
//    }
//}
