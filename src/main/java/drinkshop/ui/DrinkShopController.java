package drinkshop.ui;

import drinkshop.domain.*;
import drinkshop.repository.RepositoryException;
import drinkshop.service.DrinkShopService;
import drinkshop.service.validator.ValidationException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DrinkShopController {

    private DrinkShopService service;

    // ---------- PRODUCT ----------
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> colProdId;
    @FXML private TableColumn<Product, String> colProdName;
    @FXML private TableColumn<Product, Double> colProdPrice;
    @FXML private TableColumn<Product, String> colProdCategorie;
    @FXML private TableColumn<Product, String> colProdTip;
    @FXML private TextField txtProdName, txtProdPrice;
    @FXML private ComboBox<String> comboProdCategorie;
    @FXML private ComboBox<String> comboProdTip;

    // ---------- RETETE ----------
    @FXML private TableView<Reteta> retetaTable;
    @FXML private TableColumn<Reteta, Integer> colRetetaId;
    @FXML private TableColumn<Reteta, String> colRetetaDesc;

    @FXML private TableView<IngredientReteta> newRetetaTable;
    @FXML private TableColumn<IngredientReteta, String> colNewIngredName;
    @FXML private TableColumn<IngredientReteta, Double> colNewIngredCant;
    @FXML private TextField txtNewIngredName, txtNewIngredCant;

    // ---------- ORDER (CURRENT) ----------
    @FXML private TableView<OrderItem> currentOrderTable;
    @FXML private TableColumn<OrderItem, String> colOrderProdName;
    @FXML private TableColumn<OrderItem, Integer> colOrderQty;

    @FXML private ComboBox<Integer> comboQty;
    @FXML private Label lblOrderTotal;
    @FXML private TextArea txtReceipt;

    @FXML private Label lblTotalRevenue;

    // ---------- TIPURI ----------
    @FXML private TableView<TipBautura> tipTable;
    @FXML private TableColumn<TipBautura, Integer> colTipId;
    @FXML private TableColumn<TipBautura, String> colTipName;
    @FXML private TextField txtTipName;

    // ---------- CATEGORII ----------
    @FXML private TableView<CategorieBautura> categorieTable;
    @FXML private TableColumn<CategorieBautura, Integer> colCategorieId;
    @FXML private TableColumn<CategorieBautura, String> colCategorieName;
    @FXML private TextField txtCategorieName;

    private final ObservableList<Product> productList = FXCollections.observableArrayList();
    private final ObservableList<Reteta> retetaList = FXCollections.observableArrayList();
    private final ObservableList<IngredientReteta> newRetetaList = FXCollections.observableArrayList();
    private final ObservableList<OrderItem> currentOrderItems = FXCollections.observableArrayList();
    private final ObservableList<TipBautura> tipList = FXCollections.observableArrayList();
    private final ObservableList<CategorieBautura> categorieList = FXCollections.observableArrayList();

    private Order currentOrder = new Order(1);

    public void setService(DrinkShopService service) {
        this.service = service;
        initData();
    }

    @FXML
    private void initialize() {

        // PRODUCTS
        colProdId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProdName.setCellValueFactory(new PropertyValueFactory<>("nume"));
        colProdPrice.setCellValueFactory(new PropertyValueFactory<>("pret"));
        colProdCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        colProdTip.setCellValueFactory(new PropertyValueFactory<>("tip"));
        productTable.setItems(productList);

        // RETETE
        colRetetaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRetetaDesc.setCellValueFactory(data -> {
            Reteta r = data.getValue();
            String desc = r.getIngrediente().stream()
                    .map(i -> i.getDenumire() + " (" + i.getCantitate() + ")")
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(desc);
        });
        retetaTable.setItems(retetaList);

        colNewIngredName.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        colNewIngredCant.setCellValueFactory(new PropertyValueFactory<>("cantitate"));
        newRetetaTable.setItems(newRetetaList);

        // CURRENT ORDER TABLE
        colOrderProdName.setCellValueFactory(data -> {
            int prodId = data.getValue().getProduct().getId();
            Product p = productList.stream().filter(pr -> pr.getId() == prodId).findFirst().orElse(null);
            return new SimpleStringProperty(p != null ? p.getNume() : "N/A");
        });
        colOrderQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        currentOrderTable.setItems(currentOrderItems);

        comboQty.setItems(FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10));

        // TIPURI
        colTipId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTipName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tipTable.setItems(tipList);

        // CATEGORII
        colCategorieId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCategorieName.setCellValueFactory(new PropertyValueFactory<>("name"));
        categorieTable.setItems(categorieList);
    }

    private void initData() {
        productList.setAll(service.getAllProducts());
        retetaList.setAll(service.getAllRetete());
        lblTotalRevenue.setText("Daily Revenue: " + service.getDailyRevenue());
        updateOrderTotal();
        refreshComboBoxes();
        tipList.setAll(service.getAllTipuri());
        categorieList.setAll(service.getAllCategorii());
    }

    private void refreshComboBoxes() {
        List<String> tipNames = new ArrayList<>();
        tipNames.add("ALL");
        service.getAllTipuri().forEach(t -> tipNames.add(t.getName()));
        comboProdTip.setItems(FXCollections.observableArrayList(tipNames));

        List<String> categorieNames = new ArrayList<>();
        categorieNames.add("ALL");
        service.getAllCategorii().forEach(c -> categorieNames.add(c.getName()));
        comboProdCategorie.setItems(FXCollections.observableArrayList(categorieNames));
    }

    // ---------- PRODUCT ----------
    @FXML
    private void onAddProduct() {
        int newId = service.getAllProducts().stream().mapToInt(Product::getId).max().orElse(0) + 1;
        Product p = new Product(newId,
                txtProdName.getText(),
                Double.parseDouble(txtProdPrice.getText()),
                comboProdCategorie.getValue(),
                comboProdTip.getValue());
        try {
            service.addProduct(p);
        } catch (ValidationException | RepositoryException e) {
            showError(e.getMessage(), "Eroare la adaugare produs");
        }
        initData();
    }

    @FXML
    private void onUpdateProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        Product updated = new Product(selected.getId(), txtProdName.getText(),
                Double.parseDouble(txtProdPrice.getText()),
                comboProdCategorie.getValue(), comboProdTip.getValue());
        service.updateProduct(updated);
        initData();
    }

    @FXML
    private void onDeleteProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        service.deleteProduct(selected.getId());
        initData();
    }

    @FXML
    private void onFilterCategorie() {
        productList.setAll(service.filtreazaDupaCategorie(comboProdCategorie.getValue()));
    }

    @FXML
    private void onFilterTip() {
        productList.setAll(service.filtreazaDupaTip(comboProdTip.getValue()));
    }

    // ---------- RETETA NOUA ----------
    @FXML
    private void onAddNewIngred() {
        newRetetaList.add(new IngredientReteta(txtNewIngredName.getText(),
                Double.parseDouble(txtNewIngredCant.getText())));
    }

    @FXML
    private void onDeleteNewIngred() {
        IngredientReteta sel = newRetetaTable.getSelectionModel().getSelectedItem();
        if (sel != null) newRetetaList.remove(sel);
    }

    @FXML
    private void onAddNewReteta() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showError("Selectați un produs din tabel pentru care adăugați rețeta.", "Produs nevalid");
            return;
        }
        Reteta r = new Reteta(selectedProduct, new ArrayList<>(newRetetaList));
        try {
            service.addReteta(r);
        } catch (ValidationException | RepositoryException e) {
            showError(e.getMessage(), "Eroare la adaugare reteta");
            return;
        }
        newRetetaList.clear();
        initData();
    }

    @FXML
    private void onClearNewRetetaIngredients() {
        newRetetaTable.getItems().clear();
        txtNewIngredName.clear();
        txtNewIngredCant.clear();
    }

    // ---------- CURRENT ORDER ----------
    @FXML
    private void onAddOrderItem() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        Integer qty = comboQty.getValue();

        if (selected == null) {
            showError("Selectează un produs din listă.", "Produs nevalid");
            return;
        }
        if (qty == null) {
            showError("Selectează cantitatea.", "Cantitate nevalidă");
            return;
        }

        currentOrderItems.add(new OrderItem(selected, qty));
        updateOrderTotal();
    }

    @FXML
    private void onDeleteOrderItem() {
        OrderItem sel = currentOrderTable.getSelectionModel().getSelectedItem();
        if (sel != null) {
            currentOrderItems.remove(sel);
            updateOrderTotal();
        }
    }

    @FXML
    private void onFinalizeOrder() {
        currentOrder.getItems().clear();
        currentOrder.getItems().addAll(currentOrderItems);
        currentOrder.computeTotalPrice();

        service.addOrder(currentOrder);
        txtReceipt.setText(service.generateReceipt(currentOrder));

        currentOrderItems.clear();
        currentOrder = new Order(currentOrder.getId() + 1);
        updateOrderTotal();
    }

    private void updateOrderTotal() {
        currentOrder.getItems().clear();
        currentOrder.getItems().addAll(currentOrderItems);
        double total = service.computeTotal(currentOrder);
        lblOrderTotal.setText("Total: " + total);
    }

    // ---------- TIPURI ----------
    @FXML
    private void onAddTip() {
        String name = txtTipName.getText().trim();
        if (name.isEmpty()) { showError("Introduceți un nume.", "Tip invalid"); return; }
        int newId = service.getAllTipuri().stream().mapToInt(TipBautura::getId).max().orElse(0) + 1;
        try {
            service.addTip(new TipBautura(newId, name));
        } catch (RepositoryException e) {
            showError(e.getMessage(), "Eroare");
            return;
        }
        txtTipName.clear();
        initData();
    }

    @FXML
    private void onUpdateTip() {
        TipBautura selected = tipTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showError("Selectați un tip.", "Tip nevalid"); return; }
        String name = txtTipName.getText().trim();
        if (name.isEmpty()) { showError("Introduceți un nume.", "Tip invalid"); return; }
        try {
            service.updateTip(new TipBautura(selected.getId(), name));
        } catch (RepositoryException e) {
            showError(e.getMessage(), "Eroare");
            return;
        }
        txtTipName.clear();
        initData();
    }

    @FXML
    private void onDeleteTip() {
        TipBautura selected = tipTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showError("Selectați un tip.", "Tip nevalid"); return; }
        try {
            service.deleteTip(selected.getId());
        } catch (RepositoryException e) {
            showError(e.getMessage(), "Eroare");
            return;
        }
        initData();
    }

    // ---------- CATEGORII ----------
    @FXML
    private void onAddCategorie() {
        String name = txtCategorieName.getText().trim();
        if (name.isEmpty()) { showError("Introduceți un nume.", "Categorie invalidă"); return; }
        int newId = service.getAllCategorii().stream().mapToInt(CategorieBautura::getId).max().orElse(0) + 1;
        try {
            service.addCategorie(new CategorieBautura(newId, name));
        } catch (RepositoryException e) {
            showError(e.getMessage(), "Eroare");
            return;
        }
        txtCategorieName.clear();
        initData();
    }

    @FXML
    private void onUpdateCategorie() {
        CategorieBautura selected = categorieTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showError("Selectați o categorie.", "Categorie nevalidă"); return; }
        String name = txtCategorieName.getText().trim();
        if (name.isEmpty()) { showError("Introduceți un nume.", "Categorie invalidă"); return; }
        try {
            service.updateCategorie(new CategorieBautura(selected.getId(), name));
        } catch (RepositoryException e) {
            showError(e.getMessage(), "Eroare");
            return;
        }
        txtCategorieName.clear();
        initData();
    }

    @FXML
    private void onDeleteCategorie() {
        CategorieBautura selected = categorieTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showError("Selectați o categorie.", "Categorie nevalidă"); return; }
        try {
            service.deleteCategorie(selected.getId());
        } catch (RepositoryException e) {
            showError(e.getMessage(), "Eroare");
            return;
        }
        initData();
    }

    // ---------- EXPORT + REVENUE ----------
    @FXML
    private void onExportOrdersCsv() {
        service.exportCsv("orders.csv");
    }

    @FXML
    private void onDailyRevenue() {
        lblTotalRevenue.setText("Daily Revenue: " + service.getDailyRevenue());
    }

    private void showError(String msg, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
