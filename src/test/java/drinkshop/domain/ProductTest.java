package drinkshop.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    Product product;
    private static final String juice = "JUICE";
    private static final String smoothie = "SMOOTHIE";

    private static final String waterBased = "WATER_BASED";
    private static final String basic = "BASIC";

    @BeforeEach
    void setUp() {
        product =new Product(100, "Limonada", 10.0, juice, waterBased);
    }

    @AfterEach
    void tearDown() {
        product = null;
    }

    @Test
    void getId() {
        assert 100 == product.getId();
    }

    @Test
    void getNume() {
        assert "Limonada".equals(product.getNume());
    }

    @Test
    void getPret() {
        assert 10.0 == product.getPret();
    }

    @Test
    void getCategorie() {
        assert juice.equals(product.getCategorie());
    }

    @Test
    void setCategorie() {
        product.setCategorie(smoothie);
        assert smoothie.equals(product.getCategorie());
    }

    @Test
    void getTip() {
        assert waterBased.equals(product.getTip());
    }

    @Test
    void setTip() {
        product.setTip(basic);
        assert basic.equals(product.getTip());
    }

    @Test
    void setNume() {
        product.setNume("newLimonada");
        assert "newLimonada".equals(product.getNume());
    }

    @Test
    void setPret() {
        product.setPret(10.05);
        assert 10.05 == product.getPret();
    }

    @Test
    void testToString() {
        System.out.println(product.toString());
        assert "Limonada (JUICE, WATER_BASED) - 10.0 lei".equals(product.toString());
    }
}