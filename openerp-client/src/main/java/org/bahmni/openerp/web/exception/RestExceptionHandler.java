package org.bahmni.openerp.web.exception;

import org.springframework.http.HttpStatus;

public class RestExceptionHandler {

    public static OpenERPException handleLoginError(HttpStatus statusCode) {
        switch (statusCode) {
            case UNAUTHORIZED:
                return new OpenERPException("Unauthorized access");
            case FORBIDDEN:
                return new OpenERPException("Forbidden access");
            case INTERNAL_SERVER_ERROR:
                return new OpenERPException("Internal Server Error");
            default:
                return new OpenERPException("Login failed with status code " + statusCode);
        }
    }

    public static OpenERPException handlePostError(HttpStatus statusCode, String url) {
        switch (statusCode) {
            case NOT_FOUND:
                return new OpenERPException("Resource not found at " + url);
            case INTERNAL_SERVER_ERROR:
                return new OpenERPException("Internal Server Error at " + url);
            default:
                return new OpenERPException("Could not post to " + url + ". Status code: " + statusCode);
        }
    }
}
