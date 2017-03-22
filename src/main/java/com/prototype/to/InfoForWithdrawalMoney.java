package com.prototype.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prototype.model.BankAccount;

import java.math.BigInteger;

public class InfoForWithdrawalMoney {
    private BigInteger addressId;
    private Integer amount;
    private BankAccount bankAccount;

    public InfoForWithdrawalMoney(@JsonProperty("addressId") BigInteger addressId, @JsonProperty("amount") Integer amount,
                                  @JsonProperty("bankAccount") BankAccount bankAccount) {
        this.addressId = addressId;
        this.amount = amount;
        this.bankAccount = bankAccount;
    }

    public BigInteger getAddressId() {
        return addressId;
    }

    public void setAddressId(BigInteger addressId) {
        this.addressId = addressId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }
}
