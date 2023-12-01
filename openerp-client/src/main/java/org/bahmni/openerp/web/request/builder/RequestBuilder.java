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
            context.put("esc", new EscapeTool()); //Refer : https://github.com/Bahmni/openerp-atomfeed-service/pull/80#discussion_r1410242808
            context.put("parametersList", openERPRequest.getParameters());
            context.put("id", id);
            context.put("database", database);
            context.put("password", password);
            context.put("resource", openERPRequest.getResource());
            context.put("operation", openERPRequest.getOperation());
            System.out.println("------------------------------");
            System.out.println(template.getName());
            System.out.println("Context Parameters " + context.get("parametersList"));
            System.out.println("Context id " + context.get("id"));
            System.out.println("Context database " + context.get("database"));
            System.out.println("Context password " + context.get("password"));
            System.out.println("Context resource " + context.get("resource"));
            System.out.println("Context operation " + context.get("operation"));
            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            System.out.println("writer.toString() = \n" + writer);
            System.out.println("------------------------------");
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
            System.out.println("------------------------------");
            System.out.println("writer.toString() = \n" + writer);
            System.out.println("------------------------------");
            return writer.toString();
        } catch (Exception e) {
            throw new OpenERPException(e);
        }
    }
}