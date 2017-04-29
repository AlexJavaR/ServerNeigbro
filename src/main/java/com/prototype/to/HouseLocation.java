package com.prototype.to;

public class HouseLocation {
    private Integer totalResident;
    private Double latitude;
    private Double longitude;

    public HouseLocation(Integer totalResident, Double latitude, Double longitude) {
        this.totalResident = totalResident;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public HouseLocation() {
    }

    public Integer getTotalResident() {
        return totalResident;
    }

    public void setTotalResident(Integer totalResident) {
        this.totalResident = totalResident;
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
