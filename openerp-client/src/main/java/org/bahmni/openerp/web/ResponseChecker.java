package org.bahmni.openerp.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;

public class ResponseChecker {
    public static void checkResponse(ResponseEntity<String> responseEntity, String response,String URL) throws JsonProcessingException {
        if (response == null) {
            throw new OpenERPException(String.format("Could not post to %s", URL));
        }
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new OpenERPException(String.format("Failed to post to %s. Response status: %s", URL, responseEntity.getStatusCode()));
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(response);
        if (jsonResponse.has("result")) {
            JsonNode result = jsonResponse.get("result");
            if (result != null && result.has("error")) {
                String errorMsg = result.get("error").asText();
                int status = result.get("status").asInt();
                throw new OpenERPException(String.format("Failed to post to %s. Response status: %s. Error message: %s", URL, status, errorMsg));
            }
        }
    }
}
