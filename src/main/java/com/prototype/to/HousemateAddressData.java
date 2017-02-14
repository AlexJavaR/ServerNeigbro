package com.prototype.to;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HousemateAddressData {
    private final String apartment;
    private final String addressId;


    public HousemateAddressData(@JsonProperty("addressId") String addressId, @JsonProperty("apartment") String apartment) {
        this.addressId = addressId;
        this.apartment = apartment;
    }

    public String getAddressId() {
        return addressId;
    }

    public String getApartment() {
        return apartment;
    }
}
