package com.devway.ui.controllers;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.devway.model.Bill;
import com.devway.model.Delivery;
import com.devway.model.Merchandise;
import com.devway.model.MerchandiseStore;
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
    public Label totalSupplier;

    @FXML
    public Label totalDelivery;

    @FXML
    public Label profit;

    @FXML
    private Label screenName;

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
    @FXML
    private Button addMerch;

    @FXML
    private Button addBill;

    @FXML
    private Button printBill;

    @FXML
    private TextField merchName;

    @FXML
    private TextField merchPrice;

    @FXML
    private TextField merchQt;

    @FXML
    private Label billDateField;

    @FXML
    private ComboBox<Supplier> homeComboSuppliers;

    @FXML
    private Button homeClearFields;

    @FXML
    private TextField billTotal;

    private List<AnchorPane> screenList;

    // Methods
    // Navigators
    @FXML
    private void switchToHome() throws IOException {
        screenName.setText("Accueil");

        switchTo(homeScreen, screenList);
    }

    @FXML
    private void switchToSupplierScreen() throws IOException {
        screenName.setText("Gestion des fournisseurs");

        switchTo(supplierScreen, screenList);
    }

    @FXML
    private void switchToDeliveryScreen() throws IOException {
        screenName.setText("Gestion des livraisons");

        switchTo(deliveryScreen, screenList);
    }

    @FXML
    private void switchToBillScreen() throws IOException {
        screenName.setText("Gestion des factures");
    }

    // Authentication
    @FXML
    private void logout() throws IOException {
        //
    }

    // Main methods
    @FXML
    private void addMerch() {
        if (merchName.getText() == null || merchPrice.getText() == null || merchQt.getText() == null) {
            alert = new Alert(AlertType.WARNING);
            alert.setContentText("Tous les champs doivent être renseignés.");
        } else {
            try {
                Double price = Double.parseDouble(merchPrice.getText());
                int qt = Integer.parseInt(merchQt.getText());

                Merchandise merch = new Merchandise(merchName.getText(), price, qt);
                merchStore.addMerchandise("BUTM-" + System.currentTimeMillis(), merch);
                homeClearFields();

                totalBill += price * qt;
            } catch (Exception e) {
                // System.err.println(e);
                if (e instanceof NumberFormatException) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setContentText("Le prix et la quantité doivent être des nombres.");
                }
            }
        }

        if (alert != null) alert.showAndWait();
        billTotal.setText(String.valueOf(totalBill) + " F");
    }

    @FXML
    private void addBill() {
        if (homeComboSuppliers.getSelectionModel().getSelectedItem() != null) {
            String deliveryDesc = "";
            Double amount = 0.0;
            Delivery delivery = new Delivery();
            Supplier supplier = homeComboSuppliers.getSelectionModel().getSelectedItem();

            for (Map.Entry<String, Merchandise> entry : merchStore.getStore().entrySet()) {
                deliveryDesc += " " + entry.getValue().getName() + ": " + entry.getValue().getQuantity() + ";";
                amount += entry.getValue().getPrice() * entry.getValue().getQuantity();
            }

            delivery.setData(amount, deliveryDesc, billDateField.getText(), supplier);
            writeToFile(DELIVERY_MODEL, List.of(delivery), Delivery[].class, false);

            Bill bill = new Bill();
            bill.setData(supplier, billDateField.getText(), delivery);
            writeToFile(BILL_MODEL, List.of(bill), Bill[].class, false);

            alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("Facture enregistrée.");
        } else {
            alert = new Alert(AlertType.WARNING);
            alert.setContentText("Sélectionnez un fournisseur.");
        }

        homeClearFields();
        alert.showAndWait();
        totalBill = 0.0;
    }

    @FXML
    private void printBill() {
        merchStore.displayMerchandise(totalBill, 1000.0);
    }

    @FXML
    private void homeClearFields() {
        merchName.setText(null);
        merchPrice.setText(null);
        merchQt.setText(null);
        homeComboSuppliers.getSelectionModel().clearSelection();
    }

    // Supplier
    @FXML
    private void addSupplier() throws IOException {
        if (nameSupplier != null && contactSupplier != null && addressSupplier != null) {
            Supplier supplier = new Supplier();

            supplier.setData(nameSupplier.getText(), contactSupplier.getText(), addressSupplier.getText());
            writeToFile(SUPPLIER_MODEL, List.of(supplier), Supplier[].class,
                    (selectedRow instanceof Supplier));
            clearSupplierFields();
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

        clearSupplierFields();
    }

    // Delivery
    @FXML
    private void addDelivery() throws IOException {
        if (deliveryDescription != null && deliveryAmount != null && addressSupplier != null) {
            Delivery delivery = new Delivery();

            delivery.setData(Double.parseDouble(deliveryAmount.getText()), deliveryDescription.getText(),
                    deliveryDate.getValue().format(formatter), comboSuppliers.getSelectionModel().getSelectedItem());
            writeToFile(DELIVERY_MODEL, List.of(delivery), Delivery[].class,
                    (selectedRow instanceof Delivery));
            clearDeliveryFields();
        }
    }

    @FXML
    private void delDelivery() throws IOException {
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

        clearDeliveryFields();
    }

    @FXML
    private void clearDeliveryFields() throws IOException {
        contactSupplier.setText(null);
        nameSupplier.setText(null);
        addressSupplier.setText(null);
    }

    // init method
    @FXML
    public void initialize() {
        Double moneyMade = deliveryList.stream().map(
                Delivery::getAmount).reduce(0.0, Double::sum);

        billDateField.setText(LocalDate.now().toString());
        screenName.setText("Accueil");
        totalSupplier.setText(String.valueOf(supplierList.size()));
        totalDelivery.setText(String.valueOf(deliveryList.size()));
        profit.setText(String.valueOf(moneyMade));

        screenList = Arrays.asList(homeScreen, supplierScreen, deliveryScreen);
        filteredSuppliersData = new FilteredList<>(supplierList, n -> true);
        filteredDeliveriesData = new FilteredList<>(deliveryList, n -> true);

        suppliersTableView.setRowFactory(tv -> {
            TableRow<Supplier> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    selectedRow = row.getItem();
                    showSelectedData();
                }
            });

            return row;
        });

        deliveriesTableView.setRowFactory(tv -> {
            TableRow<Delivery> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    selectedRow = row.getItem();
                    showSelectedData();
                }
            });

            return row;
        });

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
        homeComboSuppliers.setItems(filteredSuppliersData);
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
        homeComboSuppliers.setConverter(new StringConverter<Supplier>() {
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
        searchDelivery.setOnKeyReleased(e -> search(DELIVERY_MODEL, searchDelivery, filteredDeliveriesData));

        addMerch.setOnKeyPressed(ev -> {
            if (ev.getCode().equals(KeyCode.ENTER)) {
                addMerch.fire();
                ev.consume();
            }
        });
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

    private void showSelectedData() {
        if (selectedRow instanceof Supplier) {
            nameSupplier.setText(((Supplier) selectedRow).getName());
            contactSupplier.setText(((Supplier) selectedRow).getContact());
            addressSupplier.setText(((Supplier) selectedRow).getAddress());
        } else if (selectedRow instanceof Delivery) {
            deliveryDescription.setText(((Delivery) selectedRow).getDescription());
            deliveryAmount.setText(String.valueOf(((Delivery) selectedRow).getAmount()));
            deliveryDate.setValue(LocalDate.parse(((Delivery) selectedRow).getDeliveryDate(), formatter));
            comboSuppliers.getSelectionModel().select(((Delivery) selectedRow).getSupplier());
        }
    }
}
