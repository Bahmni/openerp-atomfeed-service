package org.bahmni.openerp.web.config;

import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;

import java.io.IOException;

public class FreeMarkerConfig {

    private static Configuration config;

    public static Configuration getConfiguration() throws IOException, TemplateModelException {
        if (config == null) {
            config = new Configuration(Configuration.VERSION_2_3_23);
            config.setClassForTemplateLoading(FreeMarkerConfig.class, "/request/template/");
            config.setDefaultEncoding("UTF-8");
        }
        return config;
    }
}
