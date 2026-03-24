package drinkshop.domain;

import java.util.List;

public class Reteta {

    private Product product;
    private List<IngredientReteta> ingrediente;

    public Reteta(Product product, List<IngredientReteta> ingrediente) {
        this.product = product;
        this.ingrediente = ingrediente;
    }

    public int getId() { return product.getId(); }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public List<IngredientReteta> getIngrediente() { return ingrediente; }
    public void setIngrediente(List<IngredientReteta> ingrediente) { this.ingrediente = ingrediente; }

    @Override
    public String toString() {
        return "Reteta{" +
                "product=" + product.getNume() +
                ", ingrediente=" + ingrediente +
                '}';
    }
}
