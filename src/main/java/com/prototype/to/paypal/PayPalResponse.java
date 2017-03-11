package com.prototype.to.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PayPalResponse {
    private String id;
    private String createTime;
    private String resourceType;
    private String eventType;
    private String summary;
    private String eventVersion;
    private Resource resource;

    public PayPalResponse() {
    }

    public PayPalResponse(@JsonProperty("id") String id, @JsonProperty("create_time") String createTime, @JsonProperty("resource_type") String resourceType,
                          @JsonProperty("event_type") String eventType, @JsonProperty("summary") String summary,
                          @JsonProperty("event_version") String eventVersion, @JsonProperty("resource") Resource resource) {
        this.id = id;
        this.createTime = createTime;
        this.resourceType = resourceType;
        this.eventType = eventType;
        this.summary = summary;
        this.eventVersion = eventVersion;
        this.resource = resource;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getEventVersion() {
        return eventVersion;
    }

    public void setEventVersion(String eventVersion) {
        this.eventVersion = eventVersion;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
