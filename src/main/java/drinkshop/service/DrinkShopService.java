package drinkshop.service;

import drinkshop.domain.*;
import drinkshop.export.CsvExporter;
import drinkshop.receipt.ReceiptGenerator;
import drinkshop.reports.DailyReportService;

import java.util.List;

public class DrinkShopService {

    private final ProductService productService;
    private final OrderService orderService;
    private final RetetaService retetaService;
    private final StocService stocService;
    private final DailyReportService report;
    private final TipBauturaService tipBauturaService;
    private final CategorieBauturaService categorieBauturaService;

    public DrinkShopService(
            ProductService productService,
            OrderService orderService,
            RetetaService retetaService,
            StocService stocService,
            DailyReportService report,
            TipBauturaService tipBauturaService,
            CategorieBauturaService categorieBauturaService
    ) {
        this.productService = productService;
        this.orderService = orderService;
        this.retetaService = retetaService;
        this.stocService = stocService;
        this.report = report;
        this.tipBauturaService = tipBauturaService;
        this.categorieBauturaService = categorieBauturaService;
    }

    // ---------- PRODUCT ----------
    public void addProduct(Product p) {
        productService.addProduct(p);
    }

    public void updateProduct(Product p) {
        productService.updateProduct(p);
    }

    public void deleteProduct(int id) {
        productService.deleteProduct(id);
    }

    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    public List<Product> filtreazaDupaCategorie(String categorie) {
        return productService.filterByCategorie(categorie);
    }

    public List<Product> filtreazaDupaTip(String tip) {
        return productService.filterByTip(tip);
    }

    // ---------- ORDER ----------
    public void addOrder(Order o) {
        orderService.addOrder(o);
    }

    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    public double computeTotal(Order o) {
        return orderService.computeTotal(o);
    }

    public String generateReceipt(Order o) {
        return ReceiptGenerator.generate(o, productService.getAllProducts());
    }

    public double getDailyRevenue() {
        return report.getTotalRevenue();
    }

    public void exportCsv(String path) {
        CsvExporter.exportOrders(orderService.getAllOrders(), path);
    }

    // ---------- STOCK + RECIPE ----------
    public void comandaProdus(Product produs) {
        Reteta reteta = retetaService.findById(produs.getId());

        if (!stocService.areSuficient(reteta)) {
            throw new IllegalStateException("Stoc insuficient pentru produsul: " + produs.getNume());
        }
        stocService.consuma(reteta);
    }

    public List<Reteta> getAllRetete() {
        return retetaService.getAll();
    }

    public void addReteta(Reteta r) {
        retetaService.addReteta(r);
    }

    public void updateReteta(Reteta r) {
        retetaService.updateReteta(r);
    }

    public void deleteReteta(int id) {
        retetaService.deleteReteta(id);
    }

    public boolean productExistsForReteta(int id) {
        return productService.productExistsForReteta(id);
    }

    // ---------- TIP BAUTURA ----------
    public List<TipBautura> getAllTipuri() {
        return tipBauturaService.getAll();
    }

    public void addTip(TipBautura t) {
        tipBauturaService.add(t);
    }

    public void updateTip(TipBautura t) {
        tipBauturaService.update(t);
    }

    public void deleteTip(int id) {
        tipBauturaService.delete(id);
    }

    // ---------- CATEGORIE BAUTURA ----------
    public List<CategorieBautura> getAllCategorii() {
        return categorieBauturaService.getAll();
    }

    public void addCategorie(CategorieBautura c) {
        categorieBauturaService.add(c);
    }

    public void updateCategorie(CategorieBautura c) {
        categorieBauturaService.update(c);
    }

    public void deleteCategorie(int id) {
        categorieBauturaService.delete(id);
    }
}
