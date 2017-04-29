package com.prototype.model;

import java.io.Serializable;

public class GoogleAddress implements Serializable {
    private String placeId;
    private String description;
    private Double latitude;
    private Double longitude;
    private String house;
    private String street;
    private String city;
    private Integer amountUser;

    public GoogleAddress() {
    }

    public GoogleAddress(String placeId, String description, Double latitude, Double longitude, String house, String street, String city, Integer amountUser) {
        this.placeId = placeId;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.house = house;
        this.street = street;
        this.city = city;
        this.amountUser = amountUser;
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

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getAmountUser() {
        return amountUser;
    }

    public void setAmountUser(Integer amountUser) {
        this.amountUser = amountUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoogleAddress that = (GoogleAddress) o;

        if (!placeId.equals(that.placeId)) return false;
        if (latitude != null ? !latitude.equals(that.latitude) : that.latitude != null) return false;
        if (longitude != null ? !longitude.equals(that.longitude) : that.longitude != null) return false;
        if (house != null ? !house.equals(that.house) : that.house != null) return false;
        if (street != null ? !street.equals(that.street) : that.street != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        return amountUser != null ? amountUser.equals(that.amountUser) : that.amountUser == null;
    }

    @Override
    public int hashCode() {
        int result = placeId.hashCode();
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        return result;
    }
}
