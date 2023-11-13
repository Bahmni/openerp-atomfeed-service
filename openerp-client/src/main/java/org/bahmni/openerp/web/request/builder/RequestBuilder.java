package org.bahmni.openerp.web.request.builder;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.bahmni.openerp.web.OpenERPException;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.List;

@Service
public class RequestBuilder {

    public static String buildNewRequest(OpenERPRequest openERPRequest, Object id) {
        try {
            VelocityEngine velocityEngine = new VelocityEngine();
            velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            velocityEngine.init();
            Template template = velocityEngine.getTemplate("request/template/new_customer.vm");
            VelocityContext context = new VelocityContext();
            context.put("parametersList", openERPRequest.getParameters());
            context.put("id", 1);

            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new OpenERPException(e);
        }
    }
}