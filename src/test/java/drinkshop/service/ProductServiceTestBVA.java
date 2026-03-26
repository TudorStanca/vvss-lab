package drinkshop.service;

import drinkshop.domain.Product;
import drinkshop.repository.AbstractRepository;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("ProductService BVA tests for updateProduct")
@Tag("BVA")
@TestMethodOrder(OrderAnnotation.class)
class ProductServiceTestBVA {

    private static final int PRODUCT_ID = 1;
    private Repository<Integer, Product> productRepo;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepo = new AbstractRepository<>() {
            @Override
            protected Integer getId(Product entity) {
                return entity.getId();
            }
        };

        productService = new ProductService(productRepo, new ProductValidator());
        productRepo.save(new Product(PRODUCT_ID, "Cappucino", 10.40, "CLASSIC_COFFE", "SIMPLE"));
    }

    @AfterEach
    void tearDown() {
        productRepo.delete(PRODUCT_ID);
    }

    @Test
    @Order(1)
    @DisplayName("BVA valid: name length at minimum boundary (1)")
    void updateProduct_bvaValid_nameLengthMin_updatesProduct() {
        // Arrange
        Product updated = new Product(PRODUCT_ID, "A", 10.0, "CLASSIC_COFFE", "SIMPLE");

        // Act
        assertDoesNotThrow(() -> productService.updateProduct(updated));
        Product result = productService.findById(PRODUCT_ID);

        // Assert
        assertEquals("A", result.getNume());
        assertEquals(10.0, result.getPret());
    }

    @Test
    @Order(2)
    @DisplayName("BVA valid: price at minimum positive boundary (1)")
    void updateProduct_bvaValid_priceMinPositive_updatesProduct() {
        // Arrange
        Product updated = new Product(PRODUCT_ID, "Limonada", 1, "CLASSIC_COFFE", "SIMPLE");

        // Act
        assertDoesNotThrow(() -> productService.updateProduct(updated));
        Product result = productService.findById(PRODUCT_ID);

        // Assert
        assertEquals("Limonada", result.getNume());
        assertEquals(1, result.getPret());
    }

    @Test
    @Order(3)
    @DisplayName("BVA invalid: empty name at invalid minimum boundary (0)")
    void updateProduct_bvaInvalid_nameLengthZero_throwsValidationException() {
        // Arrange
        Product updated = new Product(PRODUCT_ID, "", 10.0, "CLASSIC_COFFE", "SIMPLE");

        // Act
        ValidationException exception = assertThrows(ValidationException.class, () -> productService.updateProduct(updated));

        // Assert
        assertEquals("Invalid name!\n", exception.getMessage());
    }

    @Test
    @Order(4)
    @DisplayName("BVA invalid: price at invalid boundary (0.0)")
    void updateProduct_bvaInvalid_priceZero_throwsValidationException() {
        // Arrange
        Product updated = new Product(PRODUCT_ID, "Limonada", 0.0, "CLASSIC_COFFE", "SIMPLE");

        // Act
        ValidationException exception = assertThrows(ValidationException.class, () -> productService.updateProduct(updated));

        // Assert
        assertEquals("Invalid price!\n", exception.getMessage());
    }
}
