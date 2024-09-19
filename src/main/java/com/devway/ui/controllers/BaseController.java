package com.devway.ui.controllers;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.devway.model.Bill;
import com.devway.model.Delivery;
import com.devway.model.Supplier;
import com.google.gson.Gson;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class BaseController {
    Gson gson = new Gson();
    private String dataFolder = ".data/";
    private Alert alert;

    protected void switchTo(AnchorPane screen, List<AnchorPane> screens) throws IOException {
        if (screens != null) {
            screens.forEach(scr -> scr.setVisible(false));
        }
        if (screen != null) {
            screen.setVisible(true);
        }
    }

    protected void writeToFile(String model, Object data) {
        if (!isValidModel(model, data)) {
            throw new IllegalArgumentException("Invalid data for " + model);
        }

        boolean isExisting = false;
        String which = "";
        switch (model) {
            case "supplier":
                isExisting = isExisting(model, Supplier[].class, (Supplier) data);
                which = "Fournisseur";
                break;

            case "delivery":
                isExisting = isExisting(model, Delivery[].class, (Delivery) data);
                which = "Livraison";
                break;

            default:
                break;
        }

        if (!isExisting) {
            File file = new File(dataFolder + model + ".json");
            boolean isNewFile = !file.exists() || file.length() == 0;

            if (isNewFile) {
                // If the file is new, create it and write the opening bracket
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("[\n");
                    gson.toJson(data, writer);
                    writer.write("\n]");
                    System.out.println("Data written to " + model + ".json");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // If the file already exists, read its content to remove the last line
                try {
                    List<String> lines = Files.readAllLines(file.toPath());
                    if (lines.size() > 1) {
                        lines.remove(lines.size() - 1);
                    }

                    try (FileWriter writer = new FileWriter(file)) {
                        for (String line : lines) {
                            writer.write(line + "\n");
                        }
                        writer.write(",\n"); // Add a comma before the new entry
                        gson.toJson(data, writer);
                        writer.write("\n]"); // Close the array
                        System.out.println("Data appended to " + model + ".json");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            alert = new Alert(AlertType.WARNING);
            alert.setContentText("Un enregistrement (" + which + "), avec ces informations, existe déjà.");
            alert.show();
        }
    }

    private boolean isValidModel(String model, Object data) {
        switch (model.toLowerCase()) {
            case "supplier":
                return data instanceof Supplier;
            case "delivery":
                return data instanceof Delivery;
            case "bill":
                return data instanceof Bill;
            default:
                return false;
        }
    }

    private <T> boolean isExisting(String model, Class<T[]> clazz, T data) {
        List<T> fileData = this.readData(model, clazz);
    
        if (fileData != null) {
            for (T item : fileData) {
                if (item instanceof Supplier && data instanceof Supplier) {
                    Supplier existingSupplier = (Supplier) item;
                    Supplier newSupplier = (Supplier) data;
    
                    if (existingSupplier.getName().equals(newSupplier.getName()) ||
                        existingSupplier.getContact().equals(newSupplier.getContact())) {
                        return true;
                    }
                } else if (item instanceof Delivery && data instanceof Delivery) {
                    Delivery existingDelivery = (Delivery) item;
                    Delivery newDelivery = (Delivery) data;
    
                    if (existingDelivery.getDeliveryDate().equals(newDelivery.getDeliveryDate()) ||
                        existingDelivery.getSupplier().getName().equals(newDelivery.getSupplier().getName())) {
                        return true;
                    }
                }
            }
        }
    
        return false;
    }    

    protected <T> List<T> readData(String model, Class<T[]> clazz) {
        String filename = dataFolder + model + ".json";
        List<T> data = new ArrayList<>();

        try (Reader reader = new FileReader(filename)) {
            T[] array = gson.fromJson(reader, clazz);
            if (array != null) {
                data = Arrays.asList(array);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
