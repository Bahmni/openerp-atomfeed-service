package org.bahmni.feed.openerp.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.feed.openerp.worker.Jsonify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
        HashMap<String, Object> personAttributes = new HashMap<>();
        String attrName;
        Object attrValue = null;
        for (OpenMRSPersonAttribute openMRSPersonAttribute : this) {
            attrName = openMRSPersonAttribute.getAttributeType().getDisplay();
            try{
                if (openMRSPersonAttribute.getValue() instanceof HashMap) {
                    attrValue = ((HashMap) openMRSPersonAttribute.getValue()).get("display").toString();
                } else {
                    attrValue = openMRSPersonAttribute.getValue().toString();
                }
            }
             catch (ClassCastException e){
                 Logger logger = LoggerFactory.getLogger(OpenMRSPersonAttributes.class);
                 logger.error("Unable to convert personAttributes {} to json string. {}", openMRSPersonAttribute.getValue().getClass(), e.getMessage());
             }

            personAttributes.put(attrName, attrValue);
        }

        String personAttributesJson = "";
        try {
            personAttributesJson = ObjectMapperRepository.objectMapper.writeValueAsString(personAttributes);
        } catch (IOException e) {
            Logger logger = LoggerFactory.getLogger(OpenMRSPersonAttributes.class);
            logger.error("Unable to convert personAttributes hash to json string. {}", e.getMessage());
        }

        return personAttributesJson;
    }

}
