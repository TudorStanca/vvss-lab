package drinkshop.service;


import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;
import drinkshop.repository.AbstractRepository;
import drinkshop.repository.Repository;
import drinkshop.service.validator.OrderValidator;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderServiceTestWBT {
    private OrderService orderService;
    private Repository<Integer, Order> orderRepo;
    private Repository<Integer, Product> productRepo;

    @BeforeEach
    void setUp() {
        orderRepo = new AbstractRepository<> () {
            @Override
            protected Integer getId(Order entity) {
                return entity.getId();
            }
        };

        productRepo = new AbstractRepository<Integer, Product>() {
            @Override
            protected Integer getId(Product entity) {
                return entity.getId();
            }
        };

        orderService = new OrderService(orderRepo, productRepo, new OrderValidator());
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    @Tag("F02_TC01")
    @DisplayName("F02_TC01 - Null order should return 0€")
    void computeTotal_nullOrder_returns0(){
        // Arrange
        Order order = null;

        // Act
        double result = orderService.computeTotal(order);

        // Assert
        assertEquals(0.0, result);
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    @Tag("F02_TC02")
    @DisplayName("F02_TC02 - Empty order should return 0€")
    void computeTotal_emptyOrder_returns0() {
        // Arrange
        Order order = new Order(1);

        // Act
        double result = orderService.computeTotal(order);

        // Assert
        assertEquals(0.0, result);
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    @Tag("F02_TC03")
    @DisplayName("F02_TC03 - Unknown product should throw error")
    void computeTotal_unknownProduct_throwsError() {
        // Arrange
        Order order = new Order(1);
        Product unknownProduct = new Product(1, "NonExistent", 10.0, "categoria", "tip");
        order.addItem(new OrderItem(unknownProduct, 1));

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.computeTotal(order));

        // Assert
        assertEquals("Order has unknown items", exception.getMessage());
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    @Tag("F02_TC04")
    @DisplayName("F02_TC04 - Negative quantity should throw error")
    void computeTotal_negativeQuantity_throwsError() {
        // Arrange
        Order order = new Order(1);
        Product lapte = new Product(1, "Lapte", 10.0, "categoria", "tip");
        productRepo.save(lapte);
        order.addItem(new OrderItem(lapte, -1));

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.computeTotal(order));

        // Assert
        assertEquals("Product appears in order negative times", exception.getMessage());
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    @Tag("F02_TC05")
    @DisplayName("F02_TC05 - Negative price should throw error")
    void computeTotal_negativePrice_throwsError() {
        // Arrange
        Order order = new Order(1);
        Product cafea = new Product(1, "Cafea", -10.0, "categoria", "tip");
        productRepo.save(cafea);
        order.addItem(new OrderItem(cafea, 2));

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.computeTotal(order));

        // Assert
        assertEquals("Product has negative price", exception.getMessage());
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    @Tag("F02_TC06")
    @DisplayName("F02_TC06 - Valid items should compute total")
    void computeTotal_validItems_returnsTotal() {
        // Arrange
        Order order = new Order(1);
        Product apa = new Product(1, "Apa", 12.0, "categoria", "tip");
        Product gutui = new Product(2, "Gutui", 14.0, "categoria", "tip");
        productRepo.save(apa);
        productRepo.save(gutui);
        order.addItem(new OrderItem(apa, 1));
        order.addItem(new OrderItem(gutui, 2));

        // Act
        double result = orderService.computeTotal(order);

        // Assert
        assertEquals(40.0, result);
    }

    @Test
    @org.junit.jupiter.api.Order(7)
    @Tag("F02_TC07")
    @DisplayName("F02_TC07 - Single item should compute total")
    void computeTotal_singleItem_returnsTotal() {
        // Arrange
        Order order = new Order(1);
        Product miere = new Product(1, "Miere", 2.0, "categoria", "tip");
        productRepo.save(miere);
        order.addItem(new OrderItem(miere, 2));

        // Act
        double result = orderService.computeTotal(order);

        // Assert
        assertEquals(4.0, result);
    }

    @Test
    @org.junit.jupiter.api.Order(8)
    @Tag("F02_TC08")
    @DisplayName("F02_TC08 - Zero price should throw error")
    void computeTotal_zeroPrice_throwsError() {
        // Arrange
        Order order = new Order(1);
        Product apa = new Product(1, "Apa", 2.0, "categoria", "tip");
        Product piersici = new Product(2, "Piersici", 0.0, "categoria", "tip");
        productRepo.save(apa);
        productRepo.save(piersici);
        order.addItem(new OrderItem(apa, 1));
        order.addItem(new OrderItem(piersici, 1));

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.computeTotal(order));

        // Assert
        assertEquals("Product has negative price", exception.getMessage());
    }
}
