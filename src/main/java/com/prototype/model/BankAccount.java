package com.prototype.model;

import java.io.Serializable;

public class BankAccount implements Serializable {
    private String firstName;
    private String lastName;
    private String bank;
    private String branch;
    private String account;
    private String comment;  //optional
    //private String swift;  //optional
    //private String iBan;  //optional


    public BankAccount(String firstName, String lastName, String bank, String branch, String account, String comment) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.bank = bank;
        this.branch = branch;
        this.account = account;
        this.comment = comment;
    }

    public BankAccount() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
