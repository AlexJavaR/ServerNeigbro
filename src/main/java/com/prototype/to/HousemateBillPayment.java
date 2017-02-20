package com.prototype.to;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

public class HousemateBillPayment {
    private final BigInteger addressId;
    private final String apartment;
    private final BigInteger billId;
    private final Integer partAmount;

    public HousemateBillPayment(@JsonProperty("addressId") BigInteger addressId, @JsonProperty("apartment") String apartment,
                                @JsonProperty("billId") BigInteger billId, @JsonProperty("partAmount") Integer partAmount) {
        this.addressId = addressId;
        this.apartment = apartment;
        this.billId = billId;
        this.partAmount = partAmount;
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

    public Integer getPartAmount() {
        return partAmount;
    }
}
