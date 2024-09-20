package com.devway.model;

import java.time.LocalDate;


public class Delivery {
    static int deliveryCount = 0;
    private String description;
    private String deliveryDate;
    private double amount;
    private Supplier supplier;
    private String supplierName;

    private int deliveryID;

    // Constructor
    public Delivery() {
        this.description = null;
        this.deliveryDate = null;
        this.supplierName = null;
        this.amount = 0.0;
        this.supplier = new Supplier();
        this.deliveryID = ++deliveryCount;
    }

    // Getters and Setters
    public void setData(Double amount, String description, String deliveryDate, Supplier supplier) {
        this.setAmount(amount);
        this.setDescription(description);
        this.setDeliveryDate(deliveryDate);
        this.setSupplier(supplier);
        this.setSupplierName(supplier.getName());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public int getDeliveryID() {
        return deliveryID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}

