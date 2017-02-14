package com.prototype.to;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

public class HousemateBillPayment {
    private final BigInteger addressId;
    private final String apartment;
    private final BigInteger billId;

    public HousemateBillPayment(@JsonProperty("addressId") BigInteger addressId, @JsonProperty("apartment") String apartment, @JsonProperty("billId") BigInteger billId) {
        this.addressId = addressId;
        this.apartment = apartment;
        this.billId = billId;
    }

    public BigInteger getAddressId() {
        return addressId;
    }

    public String getApartment() {
        return apartment;
    }

    public BigInteger getBillId() {
        return billId;
    }
}
