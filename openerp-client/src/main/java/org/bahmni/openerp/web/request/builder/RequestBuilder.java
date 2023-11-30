package org.bahmni.openerp.web.request.builder;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.tools.generic.EscapeTool;
import org.bahmni.openerp.web.OpenERPException;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.springframework.stereotype.Service;

import java.io.StringWriter;

@Service
public class RequestBuilder {

    public static String buildNewXMLRequest(OpenERPRequest openERPRequest, Object id, String database, String password) {
        try {
            VelocityEngine velocityEngine = new VelocityEngine();
            velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            velocityEngine.init();
            Template template = velocityEngine.getTemplate("request/template/xml_template.vm");
            VelocityContext context = new VelocityContext();
            context.put("esc", new EscapeTool());
            context.put("parametersList", openERPRequest.getParameters());
            context.put("id", id);
            context.put("database", database);
            context.put("password", password);
            context.put("resource", openERPRequest.getResource());
            context.put("operation", openERPRequest.getOperation());

            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new OpenERPException(e);
        }
    }

    public static String buildNewRestRequest(OpenERPRequest openERPRequest, String id) {
        try {
            VelocityEngine velocityEngine = new VelocityEngine();
            velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            velocityEngine.init();
            Template template = velocityEngine.getTemplate("request/template/rest_template.vm");
            VelocityContext context = new VelocityContext();
            context.put("parametersList", openERPRequest.getParameters());
            context.put("id", id);
            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new OpenERPException(e);
        }
    }
}