package com.prototype.to;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonGoogleAddress {
    private String placeId;
    private String description;
    private Double coordinateX;
    private Double coordinateY;
    private String title;
    private String entrance;
    private Integer firstApartment;
    private Integer lastApartment;
    private Integer monthlyFee;
    private String role;
    private String phoneNumber;
    private String apartment;

    public JsonGoogleAddress(@JsonProperty("placeId") String placeId, @JsonProperty("description") String description,
                             @JsonProperty("lat") Double coordinateX, @JsonProperty("lng") Double coordinateY,
                             @JsonProperty("title") String title, @JsonProperty("entrance") String entrance,
                             @JsonProperty("firstApartment") Integer firstApartment, @JsonProperty("lastApartment") Integer lastApartment,
                             @JsonProperty("monthlyFee") Integer monthlyFee, @JsonProperty("role") String role,
                             @JsonProperty("phoneNumber") String phoneNumber, @JsonProperty("apartment") String apartment) {
        this.placeId = placeId;
        this.description = description;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.title = title;
        this.entrance = entrance;
        this.firstApartment = firstApartment;
        this.lastApartment = lastApartment;
        this.monthlyFee = monthlyFee;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.apartment = apartment;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getDescription() {
        return description;
    }

    public Double getCoordinateX() {
        return coordinateX;
    }

    public Double getCoordinateY() {
        return coordinateY;
    }

    public String getTitle() {
        return title;
    }

    public String getEntrance() {
        return entrance;
    }

    public Integer getFirstApartment() {
        return firstApartment;
    }

    public Integer getLastApartment() {
        return lastApartment;
    }

    public Integer getMonthlyFee() {
        return monthlyFee;
    }

    public String getRole() {
        return role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getApartment() {
        return apartment;
    }
}
