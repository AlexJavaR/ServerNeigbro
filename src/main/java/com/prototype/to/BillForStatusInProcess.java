package com.prototype.to;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

public class BillForStatusInProcess {
    private BigInteger billId;
    private Boolean inProcess;
    private Integer blockedAmount;

    public BillForStatusInProcess(@JsonProperty("billId") BigInteger billId,
                                  @JsonProperty("inProcess") Boolean inProcess,
                                  @JsonProperty("blockedAmount") Integer blockedAmount) {
        this.billId = billId;
        this.inProcess = inProcess;
        this.blockedAmount = blockedAmount;
    }

    public BigInteger getBillId() {
        return billId;
    }

    public Boolean isInProcess() {
        return inProcess;
    }

    public Integer getBlockedAmount() {
        return blockedAmount;
    }
}
