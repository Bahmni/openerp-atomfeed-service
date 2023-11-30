package org.bahmni.openerp.web.http.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bahmni.openerp.web.OpenERPException;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.bahmni.openerp.web.request.builder.RequestBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Consumer;

public class RestClient {
    private static final Logger logger = LogManager.getLogger(RestClient.class);
    private WebClient webClient;
    private final int connectionTimeout;
    private String accessToken;

    private final String baseURL;

    private final String username;

    private final String password;

    public RestClient(String baseURL, String username, String password, int connectionTimeout) {
        this.baseURL = baseURL;
        this.username = username;
        this.password = password;
        this.connectionTimeout = connectionTimeout;
    }

    private void login() {
        if (accessToken == null) {
            OpenERPRequest openERPRequest = new OpenERPRequest("res.users", "login", Arrays.asList(new Parameter("username", username), new Parameter("password", password)));
            String requestBody = RequestBuilder.buildNewRestRequest(openERPRequest, UUID.randomUUID().toString());
            WebClient client = getWebClient(baseURL);
            HttpHeaders headers = getHttpHeaders();
            Consumer<HttpHeaders> consumer = httpHeaders -> httpHeaders.addAll(headers);
            try{
                String response = client.post().uri("api/odoo-login").headers(consumer).bodyValue(requestBody).retrieve().bodyToMono(String.class).timeout(Duration.ofMillis(connectionTimeout)).block();
                if (response == null) {
                    throw new OpenERPException("Login failed");
                }
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode responseNode = objectMapper.readTree(response);
                accessToken = responseNode.get("result").get("data").get("access_token").asText();
            }
            catch (Exception e){
                logger.debug("Failed to login for user {}", username);
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
            String response = client.post().uri(URI).headers(consumer).bodyValue(requestBody).retrieve().bodyToMono(String.class).timeout(Duration.ofMillis(connectionTimeout)).block();
            if (response == null) {
                throw new OpenERPException(String.format("Could not post to %s", URI));
            }
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
