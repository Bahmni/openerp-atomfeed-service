package org.bahmni.openerp.web.request.builder;


import freemarker.template.Template;
import org.bahmni.openerp.web.OpenERPException;
import org.bahmni.openerp.web.config.FreeMarkerConfig;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.HashMap;

@Service
public class RequestBuilder {

    public static String buildNewXMLRequest(OpenERPRequest openERPRequest, Object id, String database, String password) {
        try {
            HashMap<String, Object> context = getXMLContext(openERPRequest, id, database, password);
            return buildRequest("xml_template.ftl", context);
        } catch (Exception e) {
            throw new OpenERPException(e);
        }
    }

    private static HashMap<String, Object> getXMLContext(OpenERPRequest openERPRequest, Object id, String database, String password) {
        HashMap<String, Object> context = new HashMap<>();
        context.put("parametersList", openERPRequest.getParameters());
        context.put("id", id);
        context.put("database", database);
        context.put("password", password);
        context.put("resource", openERPRequest.getResource());
        context.put("operation", openERPRequest.getOperation());
        return context;
    }

    public static String buildNewRestRequest(OpenERPRequest openERPRequest, String id) {
        try {
            HashMap<String, Object> context = getRestContext(openERPRequest, id);
            return buildRequest("rest_template.ftl", context);
        } catch (Exception e) {
            throw new OpenERPException(e);
        }
    }

    private static HashMap<String, Object> getRestContext(OpenERPRequest openERPRequest, String id) {
        HashMap<String, Object> context = new HashMap<>();
        context.put("parametersList", openERPRequest.getParameters());
        context.put("id", id);
        return context;
    }

    private static String buildRequest(String templateName, HashMap<String, Object> context){
        try {
            Template template= FreeMarkerConfig.getConfiguration().getTemplate(templateName);
            StringWriter writer = new StringWriter();
            template.process(context, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new OpenERPException(e);
        }
    }
}