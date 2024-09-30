package com.devway.model;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public enum Constants {
    INSTANCE;

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);
    public static final String FILE_EXTENSION = ".json";
    public static final String SUPPLIER_MODEL = "supplier";
    public static final String DELIVERY_MODEL = "delivery";
    public static final String MERCHANDISE_MODEL = "merchandise";
    public static final String BILL_MODEL = "bill";
    public static final String DATA_FOLDER = ".data/";
}

