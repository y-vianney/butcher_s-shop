package com.devway.model;

import java.time.LocalDate;


public class Delivery {
    static int deliveryCount = 0;
    private String description;
    private LocalDate deliveryDate;
    private double amount;
    private Supplier supplier;

    // Constructor
    public Delivery() {
        this.description = null;
        this.deliveryDate = null;
        this.amount = 0.0;
        this.supplier = new Supplier();
        this.deliveryCount++;
    }

    // Getters and Setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
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
}

