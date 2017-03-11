package com.prototype.to;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

public class BillForStatusInProcess {
    private BigInteger billId;

    public BillForStatusInProcess(@JsonProperty("billId") BigInteger billId) {
        this.billId = billId;
    }

    public BigInteger getBillId() {
        return billId;
    }
}
