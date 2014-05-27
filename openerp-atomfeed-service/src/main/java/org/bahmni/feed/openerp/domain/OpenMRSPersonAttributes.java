package org.bahmni.feed.openerp.domain;

import org.apache.log4j.Logger;
import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.feed.openerp.worker.Jsonify;
import org.bahmni.feed.openerp.worker.OpenElisSaleOrderEventWorker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OpenMRSPersonAttributes extends ArrayList<OpenMRSPersonAttribute> implements Jsonify{

    public String getGivenLocalName() {
        for (OpenMRSPersonAttribute attribute : this) {
            if(attribute.getAttributeType().isGivenLocalName()) {
                return attribute.getValue().toString();
            }
        }
        return "";
    }

    public String getFamilyLocalName() {
        for (OpenMRSPersonAttribute attribute : this) {
            if(attribute.getAttributeType().isFamilyLocalName()) {
                return attribute.getValue().toString();
            }
        }
        return "";
    }

    public String getMiddleLocalName() {
        for (OpenMRSPersonAttribute attribute : this) {
            if(attribute.getAttributeType().isMiddleLocalName()) {
                return attribute.getValue().toString();
            }
        }
        return "";
    }

    @Override
    public String toJsonString() {
        Map personAttributes = new HashMap<String, String>();
        String attrName, attrValue = null;
        for (OpenMRSPersonAttribute openMRSPersonAttribute : this) {
            attrName = openMRSPersonAttribute.getAttributeType().getDisplay();
            if (openMRSPersonAttribute.getValue() instanceof String) {
                attrValue = (String) openMRSPersonAttribute.getValue();
            } else if (openMRSPersonAttribute.getValue() instanceof HashMap) {
                attrValue = (String) ((HashMap) openMRSPersonAttribute.getValue()).get("display");
            } else {
                continue;
            }

            personAttributes.put(attrName, attrValue);
        }

        String personAttributesJson = "";
        try {
            personAttributesJson = ObjectMapperRepository.objectMapper.writeValueAsString(personAttributes);
        } catch (IOException e) {
            Logger logger = Logger.getLogger(OpenElisSaleOrderEventWorker.class);
            logger.error("Unable to convert personAttributes hash to json string. " + e.getMessage());
        }

        return personAttributesJson;
    }

}
