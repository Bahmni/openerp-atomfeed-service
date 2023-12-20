package org.bahmni.openerp.web.exception;

import org.springframework.http.HttpStatus;

public class RestExceptionHandler {

    public static OpenERPException handleLoginError(HttpStatus statusCode) {
        switch (statusCode) {
            case UNAUTHORIZED:
                return new OpenERPException("Authentication failed. Please check your credentials.");
            case FORBIDDEN:
                return new OpenERPException("Authorization failed. You don't have permission to access this resource.");
            case INTERNAL_SERVER_ERROR:
                return new OpenERPException("Internal Server Error occurred during login. Please try again later.");
            default:
                return new OpenERPException("Login failed with unexpected status code " + statusCode);
        }
    }

    public static OpenERPException handlePostError(HttpStatus statusCode, String url) {
        switch (statusCode) {
            case NOT_FOUND:
                return new OpenERPException("Resource not found at " + url + ". Please verify the request URL.");
            case INTERNAL_SERVER_ERROR:
                return new OpenERPException("Internal Server Error occurred while processing the request to " + url);
            case BAD_REQUEST:
                return new OpenERPException("Bad request. Please check your request parameters for " + url);
            default:
                return new OpenERPException("Could not post to " + url + ". Status code: " + statusCode);
        }
    }
}
