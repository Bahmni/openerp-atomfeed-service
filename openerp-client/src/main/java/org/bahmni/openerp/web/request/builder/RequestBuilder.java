package org.bahmni.openerp.web.request.builder;


import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.Template;
import org.bahmni.openerp.web.OpenERPException;
import org.bahmni.openerp.web.config.FreeMarkerConfig;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

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
    public static String buildNewJSONObject(OpenERPRequest openERPRequest, String id) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("id", id);

            Map<String, Object> data = getParameters(openERPRequest);
            Map<String, Object> params = new HashMap<>();
            params.put("data", data);
            requestBody.put("params", params);

            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(requestBody);
            return jsonString;
        } catch (Exception e) {
            throw new OpenERPException(e);
        }
    }

    private static Map<String, Object> getParameters(OpenERPRequest openERPRequest) {
        Map<String, Object> parameters = new HashMap<>();
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
}