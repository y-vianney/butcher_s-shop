package com.devway.model;

import java.time.LocalDate;


public class Bill {
    static int billID = 0;
    private Supplier supplier;
    private LocalDate date;
    private Delivery delivery;

    // Constructor
    public Bill() {
        this.supplier = new Supplier();
        this.date = null;
        this.delivery = new Delivery();
        this.billID++;
    }

    // Getters and Setters
    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
}

