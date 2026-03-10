package drinkshop.repository.file;

import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;
import drinkshop.repository.Repository;
import drinkshop.repository.RepositoryException;

import java.util.ArrayList;
import java.util.List;

public class FileOrderRepository
        extends FileAbstractRepository<Integer, Order> {

    private Repository<Integer, Product> productRepository;

    public FileOrderRepository(String fileName, Repository<Integer, Product> productRepository) {
        super(fileName);
        this.productRepository = productRepository;
        loadFromFile();
    }

    @Override
    protected Integer getId(Order entity) {
        return entity.getId();
    }

    @Override
    protected Order extractEntity(String line) {

        // Format: id,productId:qty|productId:qty,total
        String[] parts = line.split(",");

        if (parts.length != 3) {
            throw new RepositoryException("Invalid line format: " + line);
        }

        int id = Integer.parseInt(parts[0].trim());
        double totalPrice = Double.parseDouble(parts[2].trim());

        List<OrderItem> items = new ArrayList<>();
        String productsStr = parts[1].trim();

        if (!productsStr.isEmpty()) {
            String[] products = productsStr.split("\\|");

            for (String product : products) {
                String[] prodParts = product.split(":");

                if (prodParts.length != 2) {
                    throw new RepositoryException("Invalid product format in order: " + product);
                }

                int productId = Integer.parseInt(prodParts[0].trim());
                int quantity = Integer.parseInt(prodParts[1].trim());

                Product p = productRepository.findOne(productId);
                if (p == null) {
                    throw new RepositoryException("Product with id " + productId + " not found");
                }

                items.add(new OrderItem(p, quantity));
            }
        }

        return new Order(id, items, totalPrice);
    }

    @Override
    protected String createEntityAsString(Order entity) {

        StringBuilder sb = new StringBuilder();

        for (OrderItem item : entity.getItems()) {

            if (sb.length() > 0) {
                sb.append("|");
            }

            sb.append(item.getProduct().getId())
                    .append(":")
                    .append(item.getQuantity());
        }

        return entity.getId() + "," +
                sb + "," +
                entity.getTotalPrice();
    }
}
