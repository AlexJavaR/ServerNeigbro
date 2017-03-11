package com.prototype.to.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Amount {
    private String total;
    private String currency;

    public Amount(@JsonProperty("total") String total, @JsonProperty("currency") String currency) {
        this.total = total;
        this.currency = currency;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "com.prototype.to.paypal.Amount{" +
                "total='" + total + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
