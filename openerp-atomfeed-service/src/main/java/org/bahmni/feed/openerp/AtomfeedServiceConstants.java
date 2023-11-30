package org.bahmni.feed.openerp;

public enum AtomfeedServiceConstants {
    IS_REST_ENABLED("IS_ODOO_16");

    private final String atomfeedServiceConstants;

    AtomfeedServiceConstants(String atomfeedServiceConstants) {
        this.atomfeedServiceConstants = atomfeedServiceConstants;
    }

    public String getAtomfeedServiceConstants() {
        return atomfeedServiceConstants;
    }
}
