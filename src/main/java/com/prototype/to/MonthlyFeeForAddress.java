package com.prototype.to;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

public class MonthlyFeeForAddress {
    private BigInteger addressId;

    public MonthlyFeeForAddress(@JsonProperty("addressId") BigInteger addressId) {
        this.addressId = addressId;
    }

    public BigInteger getAddressId() {
        return addressId;
    }
}
