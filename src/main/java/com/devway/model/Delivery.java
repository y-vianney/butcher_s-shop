package com.devway.model;

import java.time.LocalDate;


public class Delivery {
    static int deliveryCount = 0;
    private String description;
    private LocalDate deliveryDate;
    private double amount;
    private Supplier supplier;
    private final int deliveryID;

    // Constructor
    public Delivery() {
        deliveryCount++;
        this.description = null;
        this.deliveryDate = null;
        this.amount = 0.0;
        this.supplier = new Supplier();
        this.deliveryID = deliveryCount;
    }

    // Getters and Setters
    public void setData(Double amount, String description, LocalDate deliveryDate, Supplier supplier) {
        this.setAmount(amount);
        this.setDescription(description);
        this.setDeliveryDate(deliveryDate);
        this.setSupplier(supplier);
    }

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

    public int getDeliveryID() {
        return deliveryID;
    }
}

