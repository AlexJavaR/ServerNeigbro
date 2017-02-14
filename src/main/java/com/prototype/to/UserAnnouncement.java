package com.prototype.to;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

public class UserAnnouncement {
    private BigInteger addressId;
    private String titleAnnouncement;
    private String textAnnouncement;

    public UserAnnouncement(@JsonProperty("addressId") BigInteger addressId,
                            @JsonProperty("titleAnnouncement") String titleAnnouncement,
                            @JsonProperty("textAnnouncement") String textAnnouncement) {
        this.addressId = addressId;
        this.titleAnnouncement = titleAnnouncement;
        this.textAnnouncement = textAnnouncement;
    }

    public BigInteger getAddressId() {
        return addressId;
    }

    public String getTitleAnnouncement() {
        return titleAnnouncement;
    }

    public String getTextAnnouncement() {
        return textAnnouncement;
    }
}
