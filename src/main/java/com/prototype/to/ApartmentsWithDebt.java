package com.prototype.to;

public class ApartmentsWithDebt {
    private String apartment;
    private Integer amount;

    public ApartmentsWithDebt() {
    }

    public ApartmentsWithDebt(String apartment, Integer amount) {
        this.apartment = apartment;
        this.amount = amount;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
