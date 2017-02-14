package com.prototype.to;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

public class SingleManagerPayment {
    private String title;
    private String description;
    private BigInteger addressId;
    private Integer totalAmount;
    private boolean fromFundAddress;

    public SingleManagerPayment(@JsonProperty("title") String title, @JsonProperty("addressId") BigInteger addressId,
                                @JsonProperty("description") String description, @JsonProperty("totalAmount") Integer totalAmount,
                                @JsonProperty("fromFundAddress") boolean fromFundAddress) {
        this.title = title;
        this.description = description;
        this.addressId = addressId;
        this.totalAmount = totalAmount;
        this.fromFundAddress = fromFundAddress;
    }

    public boolean isFromFundAddress() {
        return fromFundAddress;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public BigInteger getAddressId() {
        return addressId;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }
}
