package com.devway.model;


public class Supplier {
    static int supplierCount = 0;
    private String name;
    private String contact;
    private String address;
    private final int supplierID;

    // Constructor
    public Supplier() {
        supplierCount++;
        this.name = null;
        this.contact = null;
        this.address = null;
        this.supplierID = supplierCount;
    }

    // Getters and Setters
    public void setData(String name, String contact, String address) {
        this.setName(name);
        this.setAddress(address);
        this.setContact(contact);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSupplierID() {
        return supplierID;
    }
}

