package com.devway.ui.controllers;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.devway.model.Bill;
import com.devway.model.Delivery;
import com.devway.model.Supplier;
import com.google.gson.Gson;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class BaseController {
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    static final String SUPPLIER_MODEL = "supplier";
    static final String DELIVERY_MODEL = "delivery";
    static final String BILL_MODEL = "bill";
    static final String DATA_FOLDER = ".data/";
    Gson gson = new Gson();
    protected Alert alert;

    Object selectedRow = null;

    ObservableList<Supplier> supplierList = FXCollections
            .observableArrayList(readData(SUPPLIER_MODEL, Supplier[].class));
    ObservableList<Delivery> deliveryList = FXCollections
            .observableArrayList(readData(DELIVERY_MODEL, Delivery[].class));
    ObservableList<Bill> billList = FXCollections
            .observableArrayList(readData(BILL_MODEL, Bill[].class));

    protected void switchTo(AnchorPane screen, List<AnchorPane> screens) throws IOException {
        if (screens != null) {
            screens.forEach(scr -> scr.setVisible(false));
        }
        if (screen != null) {
            screen.setVisible(true);
        }
    }

    protected <T> void writeToFile(String model, List<T> data, Class<T[]> clazz, boolean overwrite) {

        if (!overwrite && this.isExisting(model, clazz, data.get(0))) {
            alert = new Alert(AlertType.WARNING);
            alert.setContentText("Un enregistrement (" + model + "), avec ces informations, existe déjà");
        } else {
            File file = new File(DATA_FOLDER + model + ".json");

            List<T> existingData = readData(model, clazz);

            // Combine existing data with new data if not overwriting
            if (!overwrite) {
                if (existingData != null) {
                    existingData = Stream.concat(existingData.stream(), data.stream()).collect(Collectors.toList());
                    data = existingData;
                } else {
                    data = new ArrayList<>(data);
                }
            }

            // Write the entire list as a single JSON array
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(gson.toJson(data));
                System.out.println("Data written to " + model + ".json");

                switch (model) {
                    case SUPPLIER_MODEL:
                        supplierList.clear();
                        supplierList = FXCollections
                                .observableArrayList(readData(SUPPLIER_MODEL, Supplier[].class));
                        break;

                    case DELIVERY_MODEL:
                        deliveryList.clear();
                        deliveryList = FXCollections
                                .observableArrayList(readData(DELIVERY_MODEL, Delivery[].class));
                        break;

                    case BILL_MODEL:
                        billList.clear();
                        billList = FXCollections
                                .observableArrayList(readData(BILL_MODEL, Bill[].class));
                        break;

                    default:
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        List<T> data = new ArrayList<>();

        String filename = DATA_FOLDER + model + ".json";

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

    protected <T> boolean removeOne(String model, Class<T[]> clazz, T data) {
        List<T> fileData = this.readData(model, clazz);

        if (this.isRemovable(model, data, fileData)) {
            if (fileData != null) {
                List<T> updatedList = new ArrayList<>();
                boolean itemRemoved = false;
    
                for (T item : fileData) {
                    if (item instanceof Supplier && data instanceof Supplier) {
                        Supplier existingSupplier = (Supplier) item;
                        Supplier supplierToDelete = (Supplier) data;
    
                        // Check for matches based on name or contact
                        if (existingSupplier.getSupplierID() == supplierToDelete.getSupplierID()) {
                            itemRemoved = true;
                            continue;
                        }
                    } else if (item instanceof Delivery && data instanceof Delivery) {
                        Delivery existingDelivery = (Delivery) item;
                        Delivery deliveryToDelete = (Delivery) data;

                        // Check for matches based on name or contact
                        if (existingDelivery.getDeliveryID() == deliveryToDelete.getDeliveryID()) {
                            itemRemoved = true;
                            continue;
                        }
                    } else {
                        Bill existingBill = (Bill) item;
                        Bill billToDelete = (Bill) data;

                        // Check for matches based on name or contact
                        if (existingBill.getBillID() == billToDelete.getBillID()) {
                            itemRemoved = true;
                            continue;
                        }
                    }
                    updatedList.add(item);
                }
    
                this.writeToFile(model, updatedList, clazz, true);
                return itemRemoved;
            }
        } else {
            System.err.println("Integrity error");
        }

        return false;
    }

    protected <T> boolean isRemovable(String model, T current, List<T> others) {
        Map<String, Function<T, Boolean>> modelCheckers = new HashMap<>();
        modelCheckers.put(SUPPLIER_MODEL, cur -> isCurrentLinkedToSupplier(cur));
        modelCheckers.put(DELIVERY_MODEL, cur -> isCurrentLinkedToDelivery(cur));
        modelCheckers.put(BILL_MODEL, cur -> isCurrentLinkedToBill(cur));

        for (String filename : modelCheckers.keySet()) {
            if (!filename.equals(model) && modelCheckers.get(filename).apply(current)) {
                return false;
            }
        }
        return true;
    }

    // Helper methods for checking relationships
    private boolean isCurrentLinkedToSupplier(Object current) {
        List<Supplier> suppliers = readData(SUPPLIER_MODEL, Supplier[].class);
        if (current instanceof Delivery) {
            return suppliers.stream()
                    .anyMatch(s -> s.getSupplierID() == ((Delivery) current).getSupplier().getSupplierID());
        } else if (current instanceof Bill) {
            return suppliers.stream()
                    .anyMatch(s -> s.getSupplierID() == ((Bill) current).getSupplier().getSupplierID());
        }
        return false;
    }

    private boolean isCurrentLinkedToDelivery(Object current) {
        List<Delivery> deliveries = readData(DELIVERY_MODEL, Delivery[].class);
        if (current instanceof Supplier) {
            return deliveries.stream()
                    .anyMatch(d -> d.getSupplier().getSupplierID() == ((Supplier) current).getSupplierID());
        } else if (current instanceof Bill) {
            return deliveries.stream()
                    .anyMatch(d -> d.getSupplier().getSupplierID() == ((Bill) current).getSupplier().getSupplierID());
        }
        return false;
    }

    private boolean isCurrentLinkedToBill(Object current) {
        List<Bill> bills = readData(BILL_MODEL, Bill[].class);
        if (current instanceof Supplier) {
            return bills.stream()
                    .anyMatch(b -> b.getSupplier().getSupplierID() == ((Supplier) current).getSupplierID());
        } else if (current instanceof Delivery) {
            return bills.stream()
                    .anyMatch(
                            b -> b.getSupplier().getSupplierID() == ((Delivery) current).getSupplier().getSupplierID());
        }
        return false;
    }

}
