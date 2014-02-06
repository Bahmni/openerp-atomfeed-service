package org.bahmni.feed.openerp.domain.referencedata;

import org.apache.log4j.Logger;
import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductUOMCategory implements ERPParameterizable {

    private String id;

    private String name;

    private static final Logger logger = Logger.getLogger(ProductUOMCategory.class);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public List<Parameter> getParameters(String eventId, String feedURIForLastReadEntry, String feedURI) {
        List<Parameter> parameters = new ArrayList<>();

        parameters.add(new Parameter("category", "create.drug.uom.category", "string"));
        parameters.add(new Parameter("feed_uri", feedURI, "string"));
        parameters.add(new Parameter("last_read_entry_id", eventId, "string"));
        parameters.add(new Parameter("feed_uri_for_last_read_entry", feedURIForLastReadEntry, "string"));
        try {
            parameters.add(new Parameter("product_uom_category", getLabTestAsJson(), "string"));
        } catch (IOException e) {
            logger.error("Cannot serialize object to json ",e);
        }

        return parameters;
    }

    private String getLabTestAsJson() throws IOException {
        return ObjectMapperRepository.objectMapper.writeValueAsString(this);
    }
}
