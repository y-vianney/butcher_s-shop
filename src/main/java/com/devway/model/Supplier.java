package com.devway.model;


public class Supplier {
    private static int supplierCount = 0;
    private String name;
    private String contact;
    private String address;
    private int supplierID;

    // Constructor
    public Supplier() {
        this.name = null;
        this.contact = null;
        this.address = null;
        this.supplierID = ++supplierCount;
    }

    // Set data method
    public void setData(String name, String contact, String address) {
        this.name = name;
        this.contact = contact;
        this.address = address;
    }

    // Getters and Setters
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

    public void setID(int ID) {
        this.supplierID = ID;
    }
}
