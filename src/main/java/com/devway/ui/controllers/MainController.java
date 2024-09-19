package com.devway.ui.controllers;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import com.devway.model.Delivery;
import com.devway.model.Supplier;


public class MainController extends BaseController {
    // Const
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static final String SUPPLIER_MODEL = "supplier";
    static final String DELIVERY_MODEL = "delivery";

    @FXML
    private AnchorPane homeScreen;

    @FXML
    private AnchorPane supplierScreen;

    @FXML
    private AnchorPane deliveryScreen;

    // Supplier elements
    @FXML
    private TextField nameSupplier;
    
    @FXML
    private TextField contactSupplier;
    
    @FXML
    private TextField addressSupplier;

    @FXML
    private TextField searchSupplier;

    @FXML
    private Button submitSupplier;

    @FXML
    private Button clearSupplierFields;

    @FXML
    private TableView<Supplier> suppliersTableView;

    private FilteredList<Supplier> filteredSuppliersData;


    private List<AnchorPane> screenList;


    // Methods
    // Navigators
    @FXML
    private void switchToHome() throws IOException {
        switchTo(homeScreen, screenList);
    }

    @FXML
    private void switchToSupplierScreen() throws IOException {
        switchTo(supplierScreen, screenList);
    }

    @FXML
    private void switchToDeliveryScreen() throws IOException {
        switchTo(deliveryScreen, screenList);
    }

    @FXML
    private void switchToBillScreen() throws IOException {
        //
    }


    // Authentication
    @FXML
    private void logout() throws IOException {
        //
    }


    // Main methods
    // Supplier
    @FXML
    private void addSupplier() throws IOException {
        if (nameSupplier != null && contactSupplier != null && addressSupplier != null) {
            Supplier supplier = new Supplier();

            supplier.setData(nameSupplier.getText(), contactSupplier.getText(), addressSupplier.getText());
            writeToFile(SUPPLIER_MODEL, supplier);
        }
    }

    @FXML
    private void clearSupplierFields() throws IOException {
        contactSupplier.setText(null);
        nameSupplier.setText(null);
        addressSupplier.setText(null);
    }

    @FXML
    public void initialize() {
        screenList = Arrays.asList(homeScreen, supplierScreen, deliveryScreen);

        searchSupplier.setOnKeyReleased(e -> search(SUPPLIER_MODEL, searchSupplier, filteredSuppliersData));
    }

    /**
     * Filtre les notes en fonction de la valeur du champ de recherche.
     */
    private <T> void search(String model, TextField searchField, FilteredList<T> tableData) {
        tableData.setPredicate(n -> {
            if (searchField.getText() == null || searchField.getText().isEmpty())
                return true;
            
            switch (model) {
                case SUPPLIER_MODEL:
                    return ((Supplier) n).getName().contains(searchField.getText());

                case DELIVERY_MODEL:
                    return ((Delivery) n).getSupplier().getName().contains(searchField.getText()) ||
                        ((Delivery) n).getAmount() == Double.parseDouble(searchField.getText());

                default:
                    return false;
            }
        });
    }
}
