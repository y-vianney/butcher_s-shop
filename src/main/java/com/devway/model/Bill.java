package com.devway.model;


public class Bill {
    static int billCount = 0;
    private Supplier supplier;
    private String date;
    private Delivery delivery;
    private int billID;

    // Constructor
    public Bill() {
        this.supplier = new Supplier();
        this.date = null;
        this.delivery = new Delivery();
        this.billID = ++billCount;
    }

    // Getters and Setters
    public void setData(Supplier supplier, String date, Delivery delivery) {
        this.supplier = supplier;
        this.date = date;
        this.delivery = delivery;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public int getBillID() {
        return billID;
    }
}

