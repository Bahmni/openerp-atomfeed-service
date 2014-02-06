package org.bahmni.feed.openerp.domain.referencedata;

import org.apache.log4j.Logger;
import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Drug implements ERPParameterizable {

    private String id;
    private String name;
    private String genericName;
    private String manufacturer;
    private String shortName;
    private double costPrice;
    private double salePrice;
    private ProductUOM purchaseUnitOfMeasure;
    private ProductUOM saleUnitOfMeasure;
    private DrugCategory category;
    private boolean active;


    private static final Logger logger = Logger.getLogger(Drug.class);

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public ProductUOM getPurchaseUnitOfMeasure() {
        return purchaseUnitOfMeasure;
    }

    public void setPurchaseUnitOfMeasure(ProductUOM purchaseUnitOfMeasure) {
        this.purchaseUnitOfMeasure = purchaseUnitOfMeasure;
    }

    public ProductUOM getSaleUnitOfMeasure() {
        return saleUnitOfMeasure;
    }

    public void setSaleUnitOfMeasure(ProductUOM saleUnitOfMeasure) {
        this.saleUnitOfMeasure = saleUnitOfMeasure;
    }


    public DrugCategory getCategory() {
        return category;
    }

    public void setCategory(DrugCategory category) {
        this.category = category;
    }

    public boolean getIsActive() {
        return active;
    }

    public void setIsActive(boolean isActive) {
        this.active = isActive;
    }

    @Override
    public List<Parameter> getParameters(String eventId, String feedURIForLastReadEntry, String feedURI) {
        List<Parameter> parameters = new ArrayList<>();

        parameters.add(new Parameter("category", "create.drug", "string"));
        parameters.add(new Parameter("feed_uri", feedURI, "string"));
        parameters.add(new Parameter("last_read_entry_id", eventId, "string"));
        parameters.add(new Parameter("feed_uri_for_last_read_entry", feedURIForLastReadEntry, "string"));
        try {
            parameters.add(new Parameter("drug", getLabTestAsJson(), "string"));
        } catch (IOException e) {
            logger.error("Cannot serialize object to json ",e);
        }

        return parameters;
    }

    private String getLabTestAsJson() throws IOException {
        return ObjectMapperRepository.objectMapper.writeValueAsString(this);
    }
}
