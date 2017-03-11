package com.prototype.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "Address")
public class Address extends BaseEntity {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String title;
    private GoogleAddress googleAddress;
    private String entrance;
    private Integer firstApartment;
    private Integer lastApartment;
    private List<String> listOfApartment;
    private Integer monthlyFee;
    private String phoneNumber;
    private Integer fundAddress;
    private Integer accountBalance;
    private boolean managerExist;
    private LocalDateTime latestGeneratedMonthlyFee;

    public Address() {
    }

    public Address(BigInteger id, String className, String title, GoogleAddress googleAddress, String entrance, Integer firstApartment, Integer lastApartment,
                   Integer monthlyFee, String phoneNumber, Integer fundAddress, Integer accountBalance, LocalDateTime latestGeneratedMonthlyFee) {
        super(id, className);
        this.title = title;
        this.googleAddress = googleAddress;
        this.entrance = entrance;
        this.firstApartment = firstApartment;
        this.lastApartment = lastApartment;
        this.listOfApartment = new ArrayList<>();
        this.monthlyFee = monthlyFee;
        this.phoneNumber = phoneNumber;
        this.fundAddress = fundAddress;
        this.accountBalance = accountBalance;
        setManagerExist(false);
        this.latestGeneratedMonthlyFee = latestGeneratedMonthlyFee;
    }

    public GoogleAddress getGoogleAddress() {
        return googleAddress;
    }

    public void setGoogleAddress(GoogleAddress googleAddress) {
        this.googleAddress = googleAddress;
    }

    public Integer getFirstApartment() {
        return firstApartment;
    }

    public void setFirstApartment(Integer firstApartment) {
        this.firstApartment = firstApartment;
    }

    public Integer getLastApartment() {
        return lastApartment;
    }

    public void setLastApartment(Integer lastApartment) {
        this.lastApartment = lastApartment;
    }

    @JsonIgnore
    public List<String> getListOfApartment() {
        return listOfApartment;
    }

    public void setListOfApartment(List<String> listOfApartment) {
        this.listOfApartment = listOfApartment;
    }

    public Integer getFundAddress() {
        return fundAddress;
    }

    public void setFundAddress(Integer fundAddress) {
        this.fundAddress = fundAddress;
    }

    public Integer getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Integer accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Integer getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(Integer monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEntrance() {
        return entrance;
    }

    public void setEntrance(String entrance) {
        this.entrance = entrance;
    }
    
    public boolean isManagerExist() {
        return managerExist;
    }

    public void setManagerExist(boolean managerExist) {
        this.managerExist = managerExist;
    }

    public LocalDateTime getLatestGeneratedMonthlyFee() {
        return latestGeneratedMonthlyFee;
    }

    public void setLatestGeneratedMonthlyFee(LocalDateTime latestGeneratedMonthlyFee) {
        this.latestGeneratedMonthlyFee = latestGeneratedMonthlyFee;
    }
}
