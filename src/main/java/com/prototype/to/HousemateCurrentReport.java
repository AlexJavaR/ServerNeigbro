package com.prototype.to;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class HousemateCurrentReport {
    private BigInteger addressId;

    public HousemateCurrentReport(@JsonProperty("addressId") BigInteger addressId) {
        this.addressId = addressId;
    }

    public BigInteger getAddressId() {
        return addressId;
    }
}
