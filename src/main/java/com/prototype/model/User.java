package com.prototype.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "User")
//@JsonIgnoreProperties({ "birthday", "registered", "updatedDate" })
public class User extends BaseEntity {
    private String firstName;
    private String lastName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String email;
    private boolean sex;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private LocalDateTime birthday;

    private boolean enabled = true;
    private List<AddressData> addressDataList;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private LocalDateTime registered = LocalDateTime.now();

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private LocalDateTime updatedDate;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private BankAccount bankAccount;

    public User(BigInteger id, String className, String firstName, String lastName, String password, String email, boolean sex,
                LocalDateTime birthday, boolean enabled, LocalDateTime registered, LocalDateTime updatedDate, BankAccount bankAccount) {
        super(id, className);
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.sex = sex;
        this.birthday = birthday;
        this.enabled = enabled;
        this.registered = registered;
        this.updatedDate = updatedDate;
        this.addressDataList = new ArrayList<>();
        this.bankAccount = bankAccount;
    }

    public User() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @JsonIgnore
    public List<AddressData> getAddressDataList() {
        return addressDataList;
    }

    public void setAddressDataList(List<AddressData> addressDataList) {
        this.addressDataList = addressDataList;
    }

    public LocalDateTime getRegistered() {
        return registered;
    }

    public void setRegistered(LocalDateTime registered) {
        this.registered = registered;
    }

    public LocalDateTime getUpdatedDate() { return updatedDate; }

    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getApartment(BigInteger addressId) {
        for (AddressData addressData : getAddressDataList()) {
            if (Objects.equals(addressData.getAddress().getId(), addressId) && addressData.getRole().equals(Role.HOUSEMATE)) {
                return addressData.getApartment();
            }
        }
        return null;
    }
}
