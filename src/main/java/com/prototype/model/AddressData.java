package com.prototype.model;

import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;

public class AddressData implements Serializable {
    @DBRef
    private Address address;
    private String title;
    private String apartment;
    private Role role;

    public AddressData() {
    }

    public AddressData(Address address, String title, String apartment, Role role) {
        this.address = address;
        this.title = title;
        this.role = role;
        this.apartment = apartment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddressData that = (AddressData) o;

        if (!address.equals(that.address)) return false;
        if (apartment != null ? !apartment.equals(that.apartment) : that.apartment != null) return false;
        return role == that.role;

    }

    @Override
    public int hashCode() {
        int result = address.hashCode();
        result = 31 * result + (apartment != null ? apartment.hashCode() : 0);
        result = 31 * result + role.hashCode();
        return result;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
