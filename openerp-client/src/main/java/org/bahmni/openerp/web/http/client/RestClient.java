package org.bahmni.openerp.web.http.client;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.http.ResponseCookie;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Consumer;

public class RestClient {
    private static final Logger logger = LogManager.getLogger(RestClient.class);
    private WebClient webClient;
    private String accessToken;
    private ResponseCookie sessionCookie;
    private final String baseURL;
    private final String username;
    private final String password;
    private final int connectionTimeout;

    private final String dbName;

    public RestClient(String baseURL, String username, String password, String dbName, int connectionTimeout) {
        this.baseURL = baseURL;
        this.username = username;
        this.password = password;
        this.connectionTimeout = connectionTimeout;
        this.dbName = dbName;
    }

    private void login() {
        if (accessToken == null) {
            OpenERPRequest openERPRequest = new OpenERPRequest("res.users", "login", Arrays.asList(new Parameter("username", username), new Parameter("password", password)));
            String requestBody = RequestBuilder.buildNewRestRequest(openERPRequest);
            WebClient client = getWebClient(baseURL);
            HttpHeaders headers = getHttpHeaders();
            Consumer<HttpHeaders> consumer = httpHeaders -> httpHeaders.addAll(headers);
            try{
                String response = client.post().uri("api/odoo-login").headers(consumer).bodyValue(requestBody).retrieve().bodyToMono(String.class).timeout(Duration.ofMillis(connectionTimeout)).block();
                logger.debug("\n-----------------------------------------------------Login Initiated-----------------------------------------------------\n* Request : {}\n* Response : {}\n-----------------------------------------------------End of Login-----------------------------------------------------", requestBody, response);
                if (response == null) {
                    throw new OpenERPException("Login failed");
                }
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode responseNode = objectMapper.readTree(response);
                accessToken = responseNode.get("result").get("data").get("access_token").asText();
            }
            catch (Exception e){
                logger.warn("Failed to login for user {}", username);
                throw new OpenERPException(String.format("Failed to login. The login user is : %s", username));
            }
        }
    }

    private void authenticate() throws JsonProcessingException {
        logger.info(String.format("Authenticating with Odoo as %s for database %s..", username, dbName));
        WebClient client = getWebClient(baseURL);
        HttpHeaders headers = getHttpHeaders();
        Consumer<HttpHeaders> consumer = httpHeaders -> httpHeaders.addAll(headers);
        HashMap<String, Object> requestBody = new HashMap<>();
        HashMap<String, String> params = new HashMap<>();
        params.put("login", username);
        params.put("password", password);
        params.put("db", dbName);
        requestBody.put("params", params);
        ObjectMapper mapper = new ObjectMapper();
        ClientResponse clientResponse = client.post().uri("web/session/authenticate").bodyValue(mapper.writeValueAsString(requestBody)).headers(consumer).exchangeToMono(Mono::just).timeout(Duration.ofMillis(connectionTimeout)).block();
        String SESSION_ID_COOKIE_NAME = "session_id";
        sessionCookie = Objects.requireNonNull(clientResponse).cookies().getFirst(SESSION_ID_COOKIE_NAME);
    }

    private boolean isCookieExpired(ResponseCookie responseCookie) {
        return responseCookie.getMaxAge().isNegative();
    }

    public String post(String URL, String requestBody) {
        try {
            if (sessionCookie == null || isCookieExpired(sessionCookie)) {
                authenticate();
            }
            logger.debug("Post Data: {}", requestBody);
            WebClient client = getWebClient(baseURL);
            HttpHeaders headers = getHttpHeaders();
            Consumer<HttpHeaders> consumer = httpHeaders -> httpHeaders.addAll(headers);
            String response = client.post().uri(URL).headers(consumer).cookie(sessionCookie.getName(), sessionCookie.getValue()).bodyValue(requestBody).retrieve().bodyToMono(String.class).timeout(Duration.ofMillis(connectionTimeout)).block();
            logger.debug("\n-----------------------------------------------------{} Initiated-----------------------------------------------------\n* Request : {}\n* Response : {}\n-----------------------------------------------------End of {}-----------------------------------------------------", URL, requestBody, response, URL);
            if (response == null) {
                throw new OpenERPException(String.format("Could not post to %s", URL));
            }
            logger.debug("Post Data output: {}", response);
            logger.debug("Post Data output: {}", response);
            return response;
        } catch (Exception e) {
            logger.error("Could not post to {}", URL, e);
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
