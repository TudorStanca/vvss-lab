package drinkshop.service;

import drinkshop.domain.Product;
import drinkshop.repository.Repository;
import drinkshop.service.validator.Validator;

import java.util.List;
import java.util.stream.Collectors;

public class ProductService {

    private final Repository<Integer, Product> productRepo;
    private final Validator<Product> validator;

    public ProductService(Repository<Integer, Product> productRepo, Validator<Product> validator) {
        this.productRepo = productRepo;
        this.validator = validator;
    }

    public void addProduct(Product p) {
        validator.validate(p);
        productRepo.save(p);
    }

    public void updateProduct(Product updated) {
        productRepo.update(updated);
    }

    public void deleteProduct(int id) {
        productRepo.delete(id);
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product findById(int id) {
        return productRepo.findOne(id);
    }

    public List<Product> filterByCategorie(String categorie) {
        if ("ALL".equals(categorie)) return getAllProducts();
        return getAllProducts().stream()
                .filter(p -> categorie.equals(p.getCategorie()))
                .collect(Collectors.toList());
    }

    public List<Product> filterByTip(String tip) {
        if ("ALL".equals(tip)) return getAllProducts();
        return getAllProducts().stream()
                .filter(p -> tip.equals(p.getTip()))
                .collect(Collectors.toList());
    }

    public boolean productExistsForReteta(int id) {
        return productRepo.findOne(id) != null;
    }
}
