package org.bahmni.feed.openerp.domain.referencedata;

import org.apache.log4j.Logger;
import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductUOM implements ERPParameterizable {
    private String id;
    private ProductUOMCategory category;
    private String name;
    private double ratio;
    private boolean isActive;

    private static final Logger logger = Logger.getLogger(ProductUOM.class);


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProductUOMCategory getCategory() {
        return category;
    }

    public void setCategory(ProductUOMCategory category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsActive() {
        return isActive;
    }

    @Override
    public List<Parameter> getParameters(String eventId, String feedURIForLastReadEntry, String feedURI) {
        List<Parameter> parameters = new ArrayList<>();

        parameters.add(new Parameter("category", "create.drug.uom", "string"));
        parameters.add(new Parameter("feed_uri", feedURI, "string"));
        parameters.add(new Parameter("last_read_entry_id", eventId, "string"));
        parameters.add(new Parameter("feed_uri_for_last_read_entry", feedURIForLastReadEntry, "string"));
        try {
            parameters.add(new Parameter("product_uom", getLabTestAsJson(), "string"));
        } catch (IOException e) {
            logger.error("Cannot serialize object to json ",e);
        }

        return parameters;
    }

    private String getLabTestAsJson() throws IOException {
        return ObjectMapperRepository.objectMapper.writeValueAsString(this);
    }
}
