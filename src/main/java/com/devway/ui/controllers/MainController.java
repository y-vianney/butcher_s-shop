package com.devway.ui.controllers;

import com.devway.model.Bill;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.devway.model.Delivery;
import com.devway.model.Supplier;
import javafx.util.StringConverter;


public class MainController extends BaseController {

    @FXML
    public TableColumn idSupplierCol;

    @FXML
    public TableColumn nameSupplierCol;

    @FXML
    public TableColumn contactSupplierCol;

    @FXML
    public TableColumn addrSupplierCol;

    @FXML
    public TableColumn idDeliveryCol;

    @FXML
    public TableColumn descriptionDeliveryCol;

    @FXML
    public TableColumn supplierDeliveryCol;

    @FXML
    public TableColumn amountDeliveryCol;

    @FXML
    public TableColumn dateDeliveryCol;

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


    // Delivery elements
    @FXML
    private TextArea deliveryDescription;

    @FXML
    private TextField deliveryAmount;

    @FXML
    private DatePicker deliveryDate;

    @FXML
    private TextField searchDelivery;

    @FXML
    private Button submitDelivery;

    @FXML
    public ComboBox<Supplier> comboSuppliers;

//    @FXML
//    private Button clearDeliveryFields;

    @FXML
    private TableView<Delivery> deliveriesTableView;

    private FilteredList<Delivery> filteredDeliveriesData;


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

    // Delivery
    @FXML
    private void addDelivery() throws IOException {
        if (deliveryDescription != null && deliveryAmount != null && addressSupplier != null) {
            Delivery delivery = new Delivery();

            delivery.setData(Double.parseDouble(deliveryAmount.getText()), deliveryDescription.getText(), deliveryDate.getValue(), comboSuppliers.getSelectionModel().getSelectedItem());
            writeToFile(DELIVERY_MODEL, delivery);
        }
    }

    @FXML
    public void initialize() {
        screenList = Arrays.asList(homeScreen, supplierScreen, deliveryScreen);
        ObservableList<Supplier> supplierList = FXCollections.observableArrayList(readData("supplier", Supplier[].class));
        ObservableList<Delivery> deliveryList = FXCollections.observableArrayList(readData("delivery", Delivery[].class));
//        ObservableList<Bill> billList = FXCollections.observableArrayList(readData("bill", Bill[].class));
        filteredSuppliersData = new FilteredList<>(supplierList, n -> true);
        filteredDeliveriesData = new FilteredList<>(deliveryList, n -> true);

        idSupplierCol.setCellValueFactory(new PropertyValueFactory<>("supplierID"));
        nameSupplierCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        contactSupplierCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        addrSupplierCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        suppliersTableView.setItems(filteredSuppliersData);

        idDeliveryCol.setCellValueFactory(new PropertyValueFactory<>("deliveryID"));
        descriptionDeliveryCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateDeliveryCol.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));
        amountDeliveryCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        supplierDeliveryCol.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        deliveriesTableView.setItems(filteredDeliveriesData);

//        idSupplierCol.setCellValueFactory(new PropertyValueFactory<>("billID"));
//        nameSupplierCol.setCellValueFactory(new PropertyValueFactory<>("date"));
//        contactSupplierCol.setCellValueFactory(new PropertyValueFactory<>("delivery"));
//        addrSupplierCol.setCellValueFactory(new PropertyValueFactory<>("supplier"));
//        suppliersTableView.setItems(filteredSuppliersData);

        comboSuppliers.setItems(filteredSuppliersData);
        comboSuppliers.setConverter(new StringConverter<Supplier>() {
            @Override
            public String toString(Supplier supplier) {
                return supplier != null ? supplier.getName() : "";
            }

            @Override
            public Supplier fromString(String string) {
                return null;
            }
        });
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
