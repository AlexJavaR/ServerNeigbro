package com.prototype.to.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Resource {
    private String id;
    private String parentPayment;
    private String updateTime;
    private String clearingTime;
    private String state;
    private String paymentMode;
    private String createTime;
    private String protectionEligibilityType;
    private String protectionEligibility;
    private Amount amount;
    private String invoiceNumber;
    private String custom;

    public Resource(@JsonProperty("id") String id, @JsonProperty("parent_payment") String parentPayment, @JsonProperty("update_time") String updateTime,
                    @JsonProperty("clearing_time") String clearingTime, @JsonProperty("state") String state, @JsonProperty("payment_mode") String paymentMode,
                    @JsonProperty("create_time") String createTime, @JsonProperty("protection_eligibility_type") String protectionEligibilityType,
                    @JsonProperty("protection_eligibility") String protectionEligibility, @JsonProperty("amount") Amount amount,
                    @JsonProperty("invoice_number") String invoiceNumber, @JsonProperty("custom") String custom) {
        this.id = id;
        this.parentPayment = parentPayment;
        this.updateTime = updateTime;
        this.clearingTime = clearingTime;
        this.state = state;
        this.paymentMode = paymentMode;
        this.createTime = createTime;
        this.protectionEligibilityType = protectionEligibilityType;
        this.protectionEligibility = protectionEligibility;
        this.amount = amount;
        this.invoiceNumber = invoiceNumber;
        this.custom = custom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentPayment() {
        return parentPayment;
    }

    public void setParentPayment(String parentPayment) {
        this.parentPayment = parentPayment;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getClearingTime() {
        return clearingTime;
    }

    public void setClearingTime(String clearingTime) {
        this.clearingTime = clearingTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getProtectionEligibilityType() {
        return protectionEligibilityType;
    }

    public void setProtectionEligibilityType(String protectionEligibilityType) {
        this.protectionEligibilityType = protectionEligibilityType;
    }

    public String getProtectionEligibility() {
        return protectionEligibility;
    }

    public void setProtectionEligibility(String protectionEligibility) {
        this.protectionEligibility = protectionEligibility;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }
}
