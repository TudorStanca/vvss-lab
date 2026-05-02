package drinkshop.service;

import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;
import drinkshop.repository.Repository;
import drinkshop.service.validator.Validator;

import java.util.List;

public class OrderService {

    private final Repository<Integer, Order> orderRepo;
    private final Repository<Integer, Product> productRepo;
    private final Validator<Order> validator;

    public OrderService(Repository<Integer, Order> orderRepo,
                        Repository<Integer, Product> productRepo,
                        Validator<Order> validator) {
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.validator = validator;
    }

    public void addOrder(Order o) {
        validator.validate(o);
        orderRepo.save(o);
    }

    public void updateOrder(Order o) {
        orderRepo.update(o);
    }

    public void deleteOrder(int id) {
        orderRepo.delete(id);
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public Order findById(int id) {
        return orderRepo.findOne(id);
    }

    public double computeTotal(Order o) {
        if (o == null)
            return 0;

        double sum = 0;
        for (var item : o.getItems()) {
            var p = productRepo.findOne(item.getProduct().getId());

            if(p == null)
                throw new RuntimeException("Order has unknown items");

            if (item.getQuantity() <= 0)
                throw new RuntimeException("Product appears in order negative times");

            if (p.getPret() <= 0)
                throw new RuntimeException("Product has negative price");

            sum += p.getPret() * item.getQuantity();
        }

        return sum;
    }

    public void addItem(Order o, OrderItem item) {
        o.getItems().add(item);
        orderRepo.update(o);
    }

    public void removeItem(Order o, OrderItem item) {
        o.getItems().remove(item);
        orderRepo.update(o);
    }
}
