package org.bahmni.openerp.web.request.builder;


import com.fasterxml.jackson.databind.ObjectMapper;
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
            return buildRequest(context);
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

    public static String buildNewRestRequest(OpenERPRequest openERPRequest) {
        try {
            HashMap<String, Object> requestBody  = getParameters(openERPRequest);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(requestBody);
        } catch (Exception e) {
            throw new OpenERPException(e);
        }
    }

    private static HashMap<String, Object> getParameters(OpenERPRequest openERPRequest) {
        HashMap<String, Object> parameters = new HashMap<>();
        for (Parameter parameter : openERPRequest.getParameters()) {
            parameters.put(parameter.getName(), parseParameterValue(parameter.getValue()));
        }
        return parameters;
    }

    private static Object parseParameterValue(String value) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(value, Object.class);
        } catch (Exception e) {
            return value;
        }
    }

    private static String buildRequest(HashMap<String, Object> context){
        try {
            Template template= FreeMarkerConfig.getConfiguration().getTemplate("xml_template.ftl");
            StringWriter writer = new StringWriter();
            template.process(context, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new OpenERPException(e);
        }
    }
}