package drinkshop.service;


import drinkshop.domain.Order;
import drinkshop.domain.Product;
import drinkshop.repository.AbstractRepository;
import drinkshop.repository.Repository;
import drinkshop.service.validator.OrderValidator;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
