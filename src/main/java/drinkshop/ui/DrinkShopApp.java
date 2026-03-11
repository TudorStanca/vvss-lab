package drinkshop.ui;

import drinkshop.domain.*;
import drinkshop.repository.Repository;
import drinkshop.repository.file.*;
import drinkshop.reports.DailyReportService;
import drinkshop.service.*;
import drinkshop.service.validator.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class DrinkShopApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        try {
            // ---------- Initializare Repository-uri ----------
            Repository<Integer, Product> productRepo = new FileProductRepository("data/products.txt");
            Repository<Integer, Order> orderRepo = new FileOrderRepository("data/orders.txt", productRepo);
            Repository<Integer, Reteta> retetaRepo = new FileRetetaRepository("data/retete.txt", productRepo);
            Repository<Integer, Stoc> stocRepo = new FileStocRepository("data/stocuri.txt");
            Repository<Integer, TipBautura> tipRepo = new FileTipBauturaRepository("data/tipuri.txt");
            Repository<Integer, CategorieBautura> categorieRepo = new FileCategorieBauturaRepository("data/categorii.txt");

            // ---------- Initializare Service-uri (cu validatori injectati) ----------
            ProductService productService = new ProductService(productRepo, new ProductValidator());
            RetetaService retetaService = new RetetaService(retetaRepo, new RetetaValidator());
            OrderService orderService = new OrderService(orderRepo, new OrderValidator());
            StocService stocService = new StocService(stocRepo, new StocValidator());
            DailyReportService dailyReport = new DailyReportService(orderRepo);
            TipBauturaService tipBauturaService = new TipBauturaService(tipRepo);
            CategorieBauturaService categorieBauturaService = new CategorieBauturaService(categorieRepo);

            // ---------- Initializare DrinkShopService cu service-uri injectate ----------
            DrinkShopService service = new DrinkShopService(
                    productService, orderService, retetaService,
                    stocService, dailyReport, tipBauturaService, categorieBauturaService
            );

            // ---------- Incarcare FXML ----------
            FXMLLoader loader = new FXMLLoader(getClass().getResource("drinkshop.fxml"));
            Scene scene = new Scene(loader.load());

            // ---------- Setare Service in Controller ----------
            DrinkShopController controller = loader.getController();
            controller.setService(service);

            // ---------- Afisare Fereastra ----------
            stage.setTitle("Coffee Shop Management");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occurred while starting the application." + e.getMessage());
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
