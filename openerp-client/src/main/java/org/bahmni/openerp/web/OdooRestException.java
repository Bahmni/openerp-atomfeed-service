package org.bahmni.openerp.web;

public class OdooRestException extends RuntimeException{
    public OdooRestException(String message, Throwable cause) {
        super(message, cause);
    }

    public OdooRestException(Throwable cause) {
        super(cause);
    }

    public OdooRestException(String message) {
        super(message);
    }
}
