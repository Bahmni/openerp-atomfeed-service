package org.bahmni.openerp.web;

public class OdooSessionExpiredException extends RuntimeException {

    public OdooSessionExpiredException() {
        super("Odoo Session Expired");
    }

    public OdooSessionExpiredException(Throwable cause) {
        super("Odoo Session Expired", cause);
    }

}
