package com.devway.ui.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.devway.model.Bill;
import com.devway.model.Constants;
import com.devway.model.Delivery;
import com.devway.model.Merchandise;
import com.devway.model.MerchandiseStore;
import com.devway.model.Supplier;
import com.google.gson.Gson;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class BaseController {
    private static final String FILE_EXTENSION = ".json";
    // pattern: DateTimeFormatter.ofPattern("dd MMMM yyyy - HH'h'mm", Locale.FRENCH)
    Gson gson = new Gson();
    protected Alert alert;

    Object selectedRow = null;
    MerchandiseStore merchStore = new MerchandiseStore();
    Bill currBill = null;
    Double totalBill = 0.0;

    ObservableList<Supplier> supplierList = FXCollections
            .observableArrayList(readData(Constants.SUPPLIER_MODEL, Supplier[].class));
    ObservableList<Delivery> deliveryList = FXCollections
            .observableArrayList(readData(Constants.DELIVERY_MODEL, Delivery[].class));
    ObservableList<Bill> billList = FXCollections
            .observableArrayList(readData(Constants.BILL_MODEL, Bill[].class));
    ObservableList<Merchandise> merchandiseList = FXCollections
            .observableArrayList(readData(Constants.MERCHANDISE_MODEL, Merchandise[].class));

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
            File file = new File(Constants.DATA_FOLDER + model + FILE_EXTENSION);

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
                System.out.println("Data written to " + model + FILE_EXTENSION);

                switch (model) {
                    case Constants.SUPPLIER_MODEL:
                        supplierList.clear();
                        supplierList = FXCollections
                                .observableArrayList(readData(Constants.SUPPLIER_MODEL, Supplier[].class));
                        break;

                    case Constants.DELIVERY_MODEL:
                        deliveryList.clear();
                        deliveryList = FXCollections
                                .observableArrayList(readData(Constants.DELIVERY_MODEL, Delivery[].class));
                        break;

                    case Constants.MERCHANDISE_MODEL:
                        merchandiseList.clear();
                        merchandiseList = FXCollections
                                .observableArrayList(readData(Constants.MERCHANDISE_MODEL, Merchandise[].class));
                        break;

                    case Constants.BILL_MODEL:
                        currBill = (Bill) data.get(0);
                        billList.clear();
                        billList = FXCollections
                                .observableArrayList(readData(Constants.BILL_MODEL, Bill[].class));
                        deliveryList = FXCollections
                                .observableArrayList(readData(Constants.DELIVERY_MODEL, Delivery[].class));
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
                }
            }
        }

        return false;
    }

    protected <T> List<T> readData(String model, Class<T[]> clazz) {
        List<T> data = new ArrayList<>();

        String filename = Constants.DATA_FOLDER + model + FILE_EXTENSION;

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

        if (this.isRemovable(model, data)) {
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

    protected <T> boolean isRemovable(String model, T current) {
        Map<String, Function<T, Boolean>> modelCheckers = new HashMap<>();
        modelCheckers.put(Constants.SUPPLIER_MODEL, this::isCurrentLinkedToSupplier);
        modelCheckers.put(Constants.DELIVERY_MODEL, this::isCurrentLinkedToDelivery);
        modelCheckers.put(Constants.BILL_MODEL, this::isCurrentLinkedToBill);

        for (Map.Entry<String, Function<T, Boolean>> checker : modelCheckers.entrySet()) {
            if (!checker.getKey().equals(model) && Boolean.TRUE.equals(checker.getValue().apply(current))) {
                return false;
            }
        }
        return true;
    }

    protected void printBillOutput(TextArea textArea) throws FileNotFoundException {
        Printer printer = Printer.getDefaultPrinter();

        if (printer == null) {
            alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("Aucune imprimante disponible. Un fichier pdf sera créé.");
            saveAsPdf(textArea.getText());
        }

        PrinterJob printerJob = PrinterJob.createPrinterJob(printer);
        if (printerJob != null && printerJob.showPrintDialog(null)) {
            Boolean success = printerJob.printPage(textArea);

            if (Boolean.TRUE.equals(success)) {
                printerJob.endJob();
                alert = new Alert(AlertType.INFORMATION);
                alert.setContentText("Facture imprimée.");
            } else {
                System.out.println("Printing failed.");
            }
        } else {
            System.out.println("Printing cancelled.");
        }
    }

    protected void saveAsPdf(String output) throws FileNotFoundException {
        String[] lines = output.split("\n");

        try (PDDocument document = new PDDocument()) {
            // Create a new page
            PDPage page = new PDPage();
            document.addPage(page);
    
            // Start a content stream
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 7);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700); // Set initial text position

                for (String line : lines) {
                    contentStream.showText(line);
                    contentStream.newLineAtOffset(0, -15); // Move down for the next line
                }
    
                contentStream.endText();
            }
    
            // Save the document to a file
            document.save("bills/Facture-" + System.currentTimeMillis() + ".pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    // Helper methods for checking relationships
    private boolean isCurrentLinkedToSupplier(Object current) {
        List<Supplier> suppliers = readData(Constants.SUPPLIER_MODEL, Supplier[].class);
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
        List<Delivery> deliveries = readData(Constants.DELIVERY_MODEL, Delivery[].class);
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
        List<Bill> bills = readData(Constants.BILL_MODEL, Bill[].class);
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
