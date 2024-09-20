package com.devway.ui.controllers;

import javafx.collections.transformation.FilteredList;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.devway.model.Bill;
import com.devway.model.Delivery;
import com.devway.model.Supplier;
import javafx.util.StringConverter;

public class MainController extends BaseController {

    @FXML
    public TableColumn<Supplier, String> idSupplierCol;

    @FXML
    public TableColumn<Supplier, String> nameSupplierCol;

    @FXML
    public TableColumn<Supplier, String> contactSupplierCol;

    @FXML
    public TableColumn<Supplier, String> addrSupplierCol;

    @FXML
    public TableColumn<Delivery, String> idDeliveryCol;

    @FXML
    public TableColumn<Delivery, String> descriptionDeliveryCol;

    @FXML
    public TableColumn<Delivery, String> supplierDeliveryCol;

    @FXML
    public TableColumn<Delivery, String> amountDeliveryCol;

    @FXML
    public TableColumn<Delivery, String> dateDeliveryCol;

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

    @FXML
    private Button clearDeliveryFields;

    @FXML
    private TableView<Delivery> deliveriesTableView;

    private FilteredList<Delivery> filteredDeliveriesData;

    // Bill elements
    private FilteredList<Bill> filteredBillsData;

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
            writeToFile(SUPPLIER_MODEL, Arrays.asList(supplier), Supplier[].class, (selectedRow != null));
        }
    }

    @FXML
    private void clearSupplierFields() throws IOException {
        contactSupplier.setText(null);
        nameSupplier.setText(null);
        addressSupplier.setText(null);
    }

    @FXML
    private void delSupplier() throws IOException {
        try {
            Supplier data = suppliersTableView.getSelectionModel().getSelectedItem();
            boolean isRemoved = removeOne(SUPPLIER_MODEL, Supplier[].class, data);

            Alert alert;
            if (isRemoved) {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setContentText("L'enregistrement a été supprimé.");
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setContentText("Une erreur s'est produite lors de la suppression de l'enregistrement.");
            }
            alert.showAndWait();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    // Delivery
    @FXML
    private void addDelivery() throws IOException {
        if (deliveryDescription != null && deliveryAmount != null && addressSupplier != null) {
            Delivery delivery = new Delivery();

            delivery.setData(Double.parseDouble(deliveryAmount.getText()), deliveryDescription.getText(),
                    deliveryDate.getValue().format(formatter), comboSuppliers.getSelectionModel().getSelectedItem());
            writeToFile(DELIVERY_MODEL, Arrays.asList(delivery), Delivery[].class, false);
        }
    }

    @FXML
    private void delDelivery() throws IOException {
        try {
            Delivery data = deliveriesTableView.getSelectionModel().getSelectedItem();
            boolean isRemoved = removeOne(DELIVERY_MODEL, Delivery[].class, data);

            Alert alert;
            if (isRemoved) {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setContentText("L'enregistrement a été supprimé.");
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setContentText("Une erreur s'est produite lors de la suppression de l'enregistrement.");
            }
            alert.showAndWait();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    // init method
    @FXML
    public void initialize() {
        screenList = Arrays.asList(homeScreen, supplierScreen, deliveryScreen);
        filteredSuppliersData = new FilteredList<>(supplierList, n -> true);
        filteredDeliveriesData = new FilteredList<>(deliveryList, n -> true);

        suppliersTableView.setRowFactory(tv -> {
            TableRow<Supplier> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    selectedRow = row.getItem();
                }
            });

            return row;
        });

        deliveriesTableView.setRowFactory(tv -> {
            TableRow<Delivery> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    selectedRow = row.getItem();
                }
            });

            return row;
        });

        if (selectedRow != null) {
            if (selectedRow instanceof Supplier) {
                nameSupplier.setText(((Supplier) selectedRow).getName());
                contactSupplier.setText(((Supplier) selectedRow).getContact());
                addressSupplier.setText(((Supplier) selectedRow).getAddress());
            } else if (selectedRow instanceof Delivery) {
                deliveryDescription.setText(((Delivery) selectedRow).getDescription());
                deliveryAmount.setText(String.valueOf(((Delivery) selectedRow).getAmount()));
                deliveryDate.setValue(LocalDate.parse(((Delivery) selectedRow).getDescription()));
                comboSuppliers.getSelectionModel().select(((Delivery) selectedRow).getSupplier());
            }
        }

        idSupplierCol.setCellValueFactory(new PropertyValueFactory<>("supplierID"));
        nameSupplierCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        contactSupplierCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        addrSupplierCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        suppliersTableView.setItems(filteredSuppliersData);

        idDeliveryCol.setCellValueFactory(new PropertyValueFactory<>("deliveryID"));
        descriptionDeliveryCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateDeliveryCol.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));
        amountDeliveryCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        supplierDeliveryCol.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        deliveriesTableView.setItems(filteredDeliveriesData);

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
