package org.bahmni.feed.openerp.domain.labOrderType;

public abstract class OpenMRSLabOrderType {

    private String uuid;
    private String name;

    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
