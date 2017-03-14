package com.prototype.model;

import org.springframework.data.geo.Point;

import java.io.Serializable;

public class GoogleAddress implements Serializable {
    private String placeId;
    private String description;
    private Double latitude;
    private Double longitude;

    public GoogleAddress() {
    }

    public GoogleAddress(String placeId, String description, Double latitude, Double longitude) {
        this.placeId = placeId;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
