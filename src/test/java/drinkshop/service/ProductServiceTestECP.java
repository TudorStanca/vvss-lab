package drinkshop.service;

import drinkshop.domain.Product;
import drinkshop.repository.AbstractRepository;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductServiceTestECP {

    private static final int PRODUCT_ID = 1;
    private static Repository<Integer, Product> productRepo;
    private ProductService productService;

    @BeforeAll
    static void beforeAll() {
        productRepo = new AbstractRepository<>() {
            @Override
            protected Integer getId(Product entity) {
                return entity.getId();
            }
        };
    }

    @AfterAll
    static void afterAll() {
        productRepo = null;
    }

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepo, new ProductValidator());
        productRepo.save(new Product(PRODUCT_ID, "Cappucino", 10.40, "CLASSIC_COFFE", "SIMPLE"));
    }

    @AfterEach
    void tearDown() {
        productRepo.delete(PRODUCT_ID);
        productService = null;
    }

    @Test
    void updateProduct_tc1_validInput_updatesProduct() {
        // Arrange
        Product updated = new Product(PRODUCT_ID, "Cappucino", 10.40, "CLASSIC_COFFE", "SIMPLE");

        // Act
        productService.updateProduct(updated);
        Product result = productService.findById(PRODUCT_ID);

        // Assert
        assertEquals("Cappucino", result.getNume());
        assertEquals(10.40, result.getPret());
        assertEquals("CLASSIC_COFFE", result.getCategorie());
        assertEquals("SIMPLE", result.getTip());
    }

    @Test
    void updateProduct_tc2_emptyName_throwsValidationException() {
        // Arrange
        Product updated = new Product(PRODUCT_ID, "", 10.40, "CLASSIC_COFFE", "SIMPLE");

        // Act
        ValidationException exception = assertThrows(ValidationException.class, () -> productService.updateProduct(updated));

        // Assert
        assertEquals("Invalid name!\n", exception.getMessage());
    }

    @Test
    void updateProduct_tc3_negativePrice_throwsValidationException() {
        // Arrange
        Product updated = new Product(PRODUCT_ID, "Cappucino", -40.00, "CLASSIC_COFFE", "SIMPLE");

        // Act
        ValidationException exception = assertThrows(ValidationException.class, () -> productService.updateProduct(updated));

        // Assert
        assertEquals("Invalid price!\n", exception.getMessage());
    }

    @Test
    void updateProduct_tc4_emptyCategory_throwsValidationException() {
        // Arrange
        Product updated = new Product(PRODUCT_ID, "Cappucino", 40.00, "", "SIMPLE");

        // Act
        ValidationException exception = assertThrows(ValidationException.class, () -> productService.updateProduct(updated));

        // Assert
        assertEquals("Invalid category!\n", exception.getMessage());
    }

    @Test
    void updateProduct_tc5_emptyType_throwsValidationException() {
        // Arrange
        Product updated = new Product(PRODUCT_ID, "Cappucino", 40.00, "CLASSIC_COFFE", "");

        // Act
        ValidationException exception = assertThrows(ValidationException.class, () -> productService.updateProduct(updated));

        // Assert
        assertEquals("Invalid type!\n", exception.getMessage());
    }
}