package org.bahmni.openerp.web.http.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bahmni.openerp.web.OpenERPException;
import org.bahmni.openerp.web.client.OpenERPResponseErrorValidator;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.bahmni.openerp.web.request.builder.RequestBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.function.Consumer;

public class RestClient {
    private static final Logger logger = LogManager.getLogger(RestClient.class);
    private WebClient webClient;

    private String accessToken;

    private String baseURL;

    private String username;

    private String password;

    public RestClient(String baseURL, String username, String password) {
        this.baseURL = baseURL;
        this.username = username;
        this.password = password;
    }

    private void login() {
        if (accessToken == null) {
            OpenERPRequest openERPRequest = new OpenERPRequest("res.users", "login", Arrays.asList(new Parameter("username", username, "String"), new Parameter("password", password, "String")));
            String requestBody = RequestBuilder.buildNewRestRequest(openERPRequest, 1);
            WebClient client = getWebClient(baseURL);
            HttpHeaders headers = getHttpHeaders();
            Consumer<HttpHeaders> consumer = httpHeaders -> httpHeaders.addAll(headers);
            try{
                String response = client.post().uri("api/odoo-login").headers(consumer).bodyValue(requestBody).retrieve().bodyToMono(String.class).block();
                if (response == null) {
                    throw new OpenERPException("Login failed");
                }
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode responseNode = objectMapper.readTree(response);
                accessToken = responseNode.get("result").get("data").get("access_token").asText();
            }
            catch (Exception e){
                logger.error("Failed to login for user {}", username);
                throw new OpenERPException(String.format("Failed to login. The login user is : %s", username));
            }
        }
    }

    public String post(String URI, String requestBody) {
        try {
            login();
            logger.debug("Post Data: {}", requestBody);
            WebClient client = getWebClient(baseURL);
            HttpHeaders headers = getHttpHeaders();
            headers.set(HttpHeaders.AUTHORIZATION, accessToken);
            Consumer<HttpHeaders> consumer = httpHeaders -> httpHeaders.addAll(headers);
            String response = client.post().uri(URI).headers(consumer).bodyValue(requestBody).retrieve().bodyToMono(String.class).block();
            System.out.println(response);
            if (response == null) {
                throw new OpenERPException(String.format("Could not post to %s", URI));
            }
            logger.info("Post Data output: {}", response);
            logger.debug("Post Data output: {}", response);
            return response;
        } catch (Exception e) {
            logger.error("Could not post to {}", URI, e);
            logger.error("Post data: {}", requestBody);
            throw new RuntimeException("Could not post message", e);
        }
    }

    private WebClient getWebClient(String baseURL) {
        if(webClient == null){
            webClient =  WebClient.builder()
                    .baseUrl(baseURL)
                    .build();
        }
        return webClient;
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return httpHeaders;
    }

}
