package drinkshop.repository.file;

import drinkshop.domain.IngredientReteta;
import drinkshop.domain.Product;
import drinkshop.domain.Reteta;
import drinkshop.repository.Repository;
import drinkshop.repository.RepositoryException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileRetetaRepository
        extends FileAbstractRepository<Integer, Reteta> {

    private final Repository<Integer, Product> productRepo;

    public FileRetetaRepository(String fileName, Repository<Integer, Product> productRepo) {
        super(fileName);
        this.productRepo = productRepo;
        loadFromFile();
    }

    @Override
    protected Integer getId(Reteta entity) {
        return entity.getId();
    }

    @Override
    protected Reteta extractEntity(String line) {

        String[] elems = line.split(",");

        if (elems.length < 2) {
            throw new RepositoryException("Invalid line format: " + line);
        }

        int productId = Integer.parseInt(elems[0]);
        Product product = productRepo.findOne(productId);
        if (product == null) {
            throw new RepositoryException("Product with id " + productId + " not found for reteta");
        }

        List<IngredientReteta> ingrediente = new ArrayList<>();
        int index = 1;
        while (index < elems.length) {
            String ingredientTotal = elems[index++];
            String[] ingredientSeparat = ingredientTotal.split(":");

            if (ingredientSeparat.length != 2) {
                throw new RepositoryException("Invalid ingredient format: " + ingredientTotal);
            }

            String ingredientName = ingredientSeparat[0];
            double ingredientQuantity = Double.parseDouble(ingredientSeparat[1]);
            ingrediente.add(new IngredientReteta(ingredientName, ingredientQuantity));
        }
        return new Reteta(product, ingrediente);
    }

    @Override
    protected String createEntityAsString(Reteta entity) {
        String ingrediente = entity.getIngrediente().stream()
                        .map(entry -> entry.getDenumire() + ":" + entry.getCantitate())
                        .collect(Collectors.joining(","));
        return entity.getId() + "," + ingrediente;
    }
}
